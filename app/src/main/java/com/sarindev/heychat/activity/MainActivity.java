package com.sarindev.heychat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sarindev.heychat.R;
import com.sarindev.heychat.adapter.ChatAdapter;
import com.sarindev.heychat.common.ChatUtils;
import com.sarindev.heychat.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    SimpleDateFormat dateFormat ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUserName = ChatUtils.ANONYMOUS;
        dateFormat = new SimpleDateFormat("hh:mm aa");
        // getting firebase database reference
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");
        chatMessages = new ArrayList<>();
        messageTypes = new ArrayList<>();
        chatRecylerView = findViewById(R.id.chat_recycler_view);
        chatRecylerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(this,chatMessages,messageTypes);
        chatRecylerView.setAdapter(chatAdapter);
        user_input = findViewById(R.id.user_input);
        send_btn = findViewById(R.id.send);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Enable send button when user_input is not empty
                if (user_input.getText().toString().trim().length()>0) {
                    showSentMessage(user_input.getText().toString());
                    user_input.setText("");
                }
            }
        });
    }

    // function to update UI for sent message
    void showSentMessage(String s){
        ChatMessage msg = new ChatMessage(s,mUserName,null,dateFormat.format(new Date()).toString());
        mMessagesDatabaseReference.push().setValue(msg);
        messageTypes.add(ChatUtils.SEND);
        chatMessages.add(msg);
        chatAdapter.notifyDataSetChanged();
        chatRecylerView.smoothScrollToPosition(chatMessages.size()-1);
    }

    // function to update UI for received message
    void showReceivedMessage(String s){
        ChatMessage msg = new ChatMessage(s,mUserName,null,dateFormat.format(new Date()).toString());
        messageTypes.add(ChatUtils.RECEIVE);
        chatMessages.add(msg);
        chatAdapter.notifyDataSetChanged();
        chatRecylerView.smoothScrollToPosition(chatMessages.size()-1);
    }
}
