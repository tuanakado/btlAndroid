package com.example.truongle.btlandroid_appraoban.Chat.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.truongle.btlandroid_appraoban.Adapter.UserContactAdapter;
import com.example.truongle.btlandroid_appraoban.Chat.model.UserContact;
import com.example.truongle.btlandroid_appraoban.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserContactActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference mData, mDataUser;
    private FirebaseAuth mAuth;
    private ArrayList<UserContact> list = new ArrayList<>() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contact);
        listView = (ListView) findViewById(R.id.ListViewUserChatList);
        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance().getReference().child("Chat").child(mAuth.getCurrentUser().getUid());
        mDataUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String key = dataSnapshot.getKey();

                mData.child(key).child("LastMessage").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String lastMessage = (String) dataSnapshot.child("mess").getValue();
                        mDataUser.child(key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String name = (String) dataSnapshot.child("name").getValue();
                                UserContact userContact = new UserContact("111",name, lastMessage,mAuth.getCurrentUser().getUid(),key);

                                if(name != null)
                                    list.add(userContact);

                                if(list.size()>=2){
                                    for(int i=0;i<list.size();i++){
                                        for(int j=i+1;j<list.size();j++){
                                            if(list.get(i).getName().equals(list.get(j).getName())) {
                                                list.set(i, list.get(j));
                                                list.remove(list.size()-1);
                                            }

                                        }
                                    }
                                }

                                UserContactAdapter adapter = new UserContactAdapter(UserContactActivity.this, list);
                                listView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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
}
