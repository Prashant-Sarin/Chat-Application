package com.sarindev.heychat.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sarindev.heychat.R;
import com.sarindev.heychat.adapter.ChatAdapter;
import com.sarindev.heychat.common.ChatUtils;
import com.sarindev.heychat.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ImageView send_btn;
    EditText user_input;
    RecyclerView chatRecylerView;
    ChatAdapter chatAdapter;
    ArrayList<ChatMessage> chatMessages;
    ArrayList<Integer> messageTypes;

    private String mUserName;

    // Firebase variables
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserName = ChatUtils.ANONYMOUS;
        dateFormat = new SimpleDateFormat("hh:mm aa");
        // getting firebase database reference
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        chatMessages = new ArrayList<>();
        messageTypes = new ArrayList<>();
        chatRecylerView = findViewById(R.id.chat_recycler_view);
        chatRecylerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(this, chatMessages, messageTypes);
        chatRecylerView.setAdapter(chatAdapter);
        user_input = findViewById(R.id.user_input);
        send_btn = findViewById(R.id.send);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Enable send button when user_input is not empty
                if (user_input.getText().toString().trim().length() > 0) {
                    pushChatMessage(user_input.getText().toString());
                    user_input.setText("");
                }
            }
        });

        //implement authStateListener
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // user is signed in
                    Toast.makeText(MainActivity.this, "You are signed in now..!!", Toast.LENGTH_SHORT).show();
                    onSignedInInitialize(user.getDisplayName());
                } else {
                    // user is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    ))
                                    .build(), ChatUtils.RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChatUtils.RC_SIGN_IN){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
            }else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Sign In Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    // function to push Chat Messages to firebase Database
    void pushChatMessage(String s) {
        ChatMessage msg = new ChatMessage(s, mUserName, null, dateFormat.format(new Date()).toString());
        mMessagesDatabaseReference.push().setValue(msg);
    }

    // function to update UI for sent message
    void showSentMessage(ChatMessage msg) {
        messageTypes.add(ChatUtils.SEND);
        chatMessages.add(msg);
        chatAdapter.notifyDataSetChanged();
        chatRecylerView.smoothScrollToPosition(chatMessages.size() - 1);
    }

    // function to update UI for received message
    void showReceivedMessage(String s) {
        ChatMessage msg = new ChatMessage(s, mUserName, null, dateFormat.format(new Date()).toString());
        messageTypes.add(ChatUtils.RECEIVE);
        chatMessages.add(msg);
        chatAdapter.notifyDataSetChanged();
        chatRecylerView.smoothScrollToPosition(chatMessages.size() - 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        detachDatabaseEventListener();
        chatMessages.clear();
    }

    void onSignedInInitialize(String displayName){
        mUserName = displayName;
        // method to read messages from database
        attachDatabaseEventListener();
    }

    void onSignedOutCleanup(){
        mUserName = ChatUtils.ANONYMOUS;
        detachDatabaseEventListener();
        chatMessages.clear();
    }

    void attachDatabaseEventListener(){
        if (mChildEventListener == null) {
            //implement childEventListener for databaseReference to read messages
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    Log.i(TAG, "ChatMessage Updated in Firebase Value = " + chatMessage);
                    // on addition of new message in DB, show it on UI
                    showSentMessage(chatMessage);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    void detachDatabaseEventListener(){
        if (mChildEventListener != null) {
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.appbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void logout(){
        AuthUI.getInstance().signOut(this);
    }
}
