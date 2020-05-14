package com.example.truongle.btlandroid_appraoban.Chat.presenter;

import com.example.truongle.btlandroid_appraoban.Chat.model.Message;
import com.example.truongle.btlandroid_appraoban.Chat.view.ChatView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by truongle on 25/04/2017.
 */

public class ChatImplPresenter implements ChatLogicPresenter {
    ChatView view;
    private DatabaseReference mData;
   // private DatabaseReference mDataChat;

    public ChatImplPresenter(ChatView view) {
        this.view = view;
        mData  = FirebaseDatabase.getInstance().getReference().child("Chat");

    }

    @Override
    public void send(Message mess, String from, String to) {
        mData.child(from).child(to).push().setValue(mess);
        mData.child(from).child(to).child("LastMessage").setValue(mess);
        view.resetText();
    }


}
