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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DsSanPhamLuu extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataProduct;
    ListView listView;

    String category_product;
    private String current_user;
    private ArrayList<DSSP> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_san_pham_luu);

        listView = (ListView) findViewById(R.id.ListViewDsSpLuu);

        mAuth = FirebaseAuth.getInstance();
        current_user = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Save").child(current_user);
        mDataProduct= FirebaseDatabase.getInstance().getReference().child("Product");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String category = (String) dataSnapshot.child("category").getValue();
                final String product_key = (String) dataSnapshot.child("product_key").getValue();
                mDataProduct.child(category).child(product_key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = (String) dataSnapshot.child("name").getValue();
                        String address = (String) dataSnapshot.child("city").getValue();
                        String image = (String) dataSnapshot.child("image").getValue();
                        DSSP sp = new DSSP(name,image, address);
                        sp.setProduct_key(product_key);
                        sp.setCategory(category);
                        list.add(sp);
                        DsSpLuuAdapter adapter  = new DsSpLuuAdapter(DsSanPhamLuu.this,list);
                        listView.setAdapter(adapter);
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
