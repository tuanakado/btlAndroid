package com.example.truongle.btlandroid_appraoban;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.truongle.btlandroid_appraoban.Adapter.DsSpLuuAdapter;
import com.example.truongle.btlandroid_appraoban.model.DSSP;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class DsSanPhamBan extends AppCompatActivity {

    private DatabaseReference mDataProduct;
    private FirebaseAuth mAuth;
    private ListView listView ;
    private FirebaseUser mCurrentUser;
    private String product_key;

    private ArrayList<DSSP> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_san_pham_ban);
        listView = (ListView) findViewById(R.id.ListViewDanhSachBan);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mDataProduct = FirebaseDatabase.getInstance().getReference().child("Product");
        addEvent("Đồ Cá Nhân");
        addEvent("Đồ Thể Thao");
        addEvent("Đồ Điện Tử");

    }

    private void addEvent(final String category) {
        Query query = mDataProduct.child(category).orderByChild("user_id").equalTo(mCurrentUser.getUid());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String name = (String) dataSnapshot.child("name").getValue();
                String address = (String) dataSnapshot.child("city").getValue();
                String image = (String) dataSnapshot.child("image").getValue();
                DSSP sp = new DSSP(name,image, address);
                sp.setProduct_key(dataSnapshot.getKey());
                sp.setCategory(category);
                list.add(sp);
                DsSpLuuAdapter adapter  = new DsSpLuuAdapter(DsSanPhamBan.this,list);
                listView.setAdapter(adapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_update){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        }
        else if(item.getItemId()==R.id.action_home){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }


        return super.onOptionsItemSelected(item);
    }
}
