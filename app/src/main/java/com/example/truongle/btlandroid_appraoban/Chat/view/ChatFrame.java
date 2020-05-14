package com.example.truongle.btlandroid_appraoban.Chat.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.truongle.btlandroid_appraoban.Adapter.ChatAdapter;
import com.example.truongle.btlandroid_appraoban.Chat.model.Message;
import com.example.truongle.btlandroid_appraoban.Chat.presenter.ChatImplPresenter;
import com.example.truongle.btlandroid_appraoban.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by truongle on 07/05/2017.
 */

public class ChatFrame implements ChatView{
    Context context;
    private Dialog chatDialog;
    private ListView listViewChat;
    EditText edtMessage;
    ImageButton btnSend1;
    private DatabaseReference mDataChat;

    public ChatFrame(Context context) {
        this.context = context;
    }

    public void Chat(final String from, final String to){
        final ChatImplPresenter presenter = new ChatImplPresenter(this);
        chatDialog = new Dialog(context);
        //set dialog
        chatDialog.setContentView(R.layout.chat_activity);
        chatDialog.setTitle("Chat Dialog");
        chatDialog.show();
        listViewChat = (ListView) chatDialog.findViewById(R.id.ListViewChat);
        createFrameChaṭ̣̣(from, to);
        edtMessage = (EditText) chatDialog.findViewById(R.id.editTextMessage);
        btnSend1= (ImageButton) chatDialog.findViewById(R.id.imageButtonSend1);
        btnSend1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Date date = new Date();
                        String strDateFormat = "dd/MM/yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);

                        Calendar calendar = Calendar.getInstance();
                        String strTimeFormat = "hh:mm";
                        SimpleDateFormat sdf2 = new SimpleDateFormat(strTimeFormat);

                        Message message = new Message(edtMessage.getText().toString(),sdf.format(date),sdf2.format(calendar.getTime()) ,from);
                        presenter.send(message,from,to);
                        presenter.send(message,to,from);

                    }



        });
    }
    private void createFrameChaṭ̣̣(final String from , String to){
        mDataChat = FirebaseDatabase.getInstance().getReference().child("Chat").child(from).child(to);
        final ArrayList<Message> list2 = new ArrayList<Message>();

                mDataChat.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (!dataSnapshot.getKey().equals("LastMessage")) {
                            Message message = dataSnapshot.getValue(Message.class);

                            if (message.getCurrent_user().equals(from))
                                message.setPosition(false);
                            else message.setPosition(true);

                            list2.add(message);
                            ChatAdapter adapter = new ChatAdapter(context, list2);
                            listViewChat.setAdapter(adapter);

                        }
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
                });



    }

    @Override
    public void resetText() {
        edtMessage.setText("");
    }
}
