package com.silhocompany.ideo.knews.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.silhocompany.ideo.knews.R;
import com.silhocompany.ideo.knews.Models.ChatMessage;

import java.util.ArrayList;

/**
 * Created by Samuel on 12/06/2017.
 */

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.ChatMessageViewHolder> {

    private final Context mContext;
    private final ArrayList<ChatMessage> mChatMessagesList;

    public ChatMessagesAdapter(Context context, ArrayList<ChatMessage> chatMessagesList){
        mContext = context;
        mChatMessagesList = chatMessagesList;
    }
    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_messages, parent, false);
        return new ChatMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatMessageViewHolder holder, int position) {
        holder.bindChatMessage(mChatMessagesList.get(position));
    }

    @Override
    public int getItemCount() {
        return mChatMessagesList.size();
    }

    public void add(ChatMessage chatMessage) {
        mChatMessagesList.add(chatMessage);
        notifyDataSetChanged();
    }

    public void clear() {
        mChatMessagesList.clear();
        notifyDataSetChanged();
    }

    public class ChatMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView mMessageTextView;
        private TextView mAuthorTextView;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);

            mMessageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.authorTextView);
        }

        public void bindChatMessage(ChatMessage chatMessage) {
            mMessageTextView.setText(chatMessage.getMessage());
            mAuthorTextView.setText(chatMessage.getAuthor());
        }
    }
}
