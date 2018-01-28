package com.sarindev.heychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sarindev.heychat.R;
import com.sarindev.heychat.common.ChatUtils;
import com.sarindev.heychat.model.ChatMessage;

import java.util.ArrayList;

/**
 * Created by SARIN on 1/27/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ChatMessage> chatMessages;
    private ArrayList<Integer> messageType;

    public ChatAdapter(Context context, ArrayList<ChatMessage> chatMessages, ArrayList<Integer> messageType) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.messageType = messageType;
    }

    class TextHolder extends RecyclerView.ViewHolder {

        TextView msg,time;

        public TextHolder(View itemView) {
            super(itemView);
            msg = itemView.findViewById(R.id.text_view);
            time = itemView.findViewById(R.id.time_stamp);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View sendView = LayoutInflater.from(context).inflate(R.layout.message_sent,parent,false);
        View receiveView = LayoutInflater.from(context).inflate(R.layout.message_received,parent,false);

        switch (viewType){
            case ChatUtils.SEND :
                return new TextHolder(sendView);
            case ChatUtils.RECEIVE:
                return new TextHolder(receiveView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case ChatUtils.SEND:
                ChatMessage sendMessage = chatMessages.get(position);
                TextHolder sendHolder = (TextHolder)holder;
                sendHolder.msg.setText(sendMessage.getMessageText());
                sendHolder.time.setText(sendMessage.getMessageTime());
                break;
            case ChatUtils.RECEIVE:
                ChatMessage recMessage = chatMessages.get(position);
                TextHolder recHolder = (TextHolder)holder;
                recHolder.msg.setText(recMessage.getMessageText());
                recHolder.time.setText(recMessage.getMessageTime());
                break;
        }

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return messageType.get(position);
    }
}
