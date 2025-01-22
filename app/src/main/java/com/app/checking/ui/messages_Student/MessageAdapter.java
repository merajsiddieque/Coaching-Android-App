package com.app.checking.ui.messages_Student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.checking.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> messages;

    // Constructor to initialize message list
    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    // Create the ViewHolder for each item
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);  // Inflating item_message layout
        return new MessageViewHolder(view);
    }

    // Bind the data to the view holder
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageTextView.setText(message.getMessage());
        holder.messageDateTime.setText(message.getDateTime());  // Set the timestamp (date and time)
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // ViewHolder for the message item
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView messageDateTime;  // TextView for displaying the date and time

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);  // Initialize message TextView
            messageDateTime = itemView.findViewById(R.id.messageDateTime);  // Initialize dateTime TextView
        }
    }
}
