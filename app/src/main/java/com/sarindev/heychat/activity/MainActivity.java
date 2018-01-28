package com.sarindev.heychat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.sarindev.heychat.R;
import com.sarindev.heychat.adapter.ChatAdapter;
import com.sarindev.heychat.common.ChatUtils;
import com.sarindev.heychat.model.ChatMessage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView send_btn;
    EditText user_input;
    RecyclerView chatRecylerView;
    ChatAdapter chatAdapter;
    ArrayList<ChatMessage> chatMessages;
    ArrayList<Integer> messageTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                }
            }
        });
    }

    // function to update UI for sent message
    void showSentMessage(String s){
        ChatMessage msg = new ChatMessage(s);
        messageTypes.add(ChatUtils.SEND);
        chatMessages.add(msg);
        chatAdapter.notifyDataSetChanged();
        chatRecylerView.smoothScrollToPosition(chatMessages.size()-1);
    }

    // function to update UI for received message
    void showReceivedMessage(String s){
        ChatMessage msg = new ChatMessage(s);
        messageTypes.add(ChatUtils.RECEIVE);
        chatMessages.add(msg);
        chatAdapter.notifyDataSetChanged();
        chatRecylerView.smoothScrollToPosition(chatMessages.size()-1);
    }
}
