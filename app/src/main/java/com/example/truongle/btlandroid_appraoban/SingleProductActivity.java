package com.example.truongle.btlandroid_appraoban;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truongle.btlandroid_appraoban.Chat.view.ChatFrame;
import com.example.truongle.btlandroid_appraoban.Chat.view.UserContactActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SingleProductActivity extends AppCompatActivity {

    TextView txtTitleProduct, txtDescProduct, txtCostProduct, txtPhone, txtAddress;
    ImageView imgSingleProduct;
    ImageButton  btnLuu, btnBoLuu;
    ImageButton btnUpdate, btnRemove;
    LinearLayout layout;
    private AlertDialog.Builder b;

    private DatabaseReference mDataProduct;
    private DatabaseReference mDataLuu;
    private DatabaseReference mKtraLuu;
    private DatabaseReference mDeleteSave;

    private Query query;

    private String current_user;
    private String product_key, tab_value;

    private FirebaseAuth mAuth;
    private int checkClickDelete;

    private FloatingActionButton fab,fab2,fab1;
    private Animation move1, back1, move2, back2;

    private boolean isSave;
    private boolean isMove = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        Init();


        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        product_key = intent.getStringExtra("product_key");
        current_user = mAuth.getCurrentUser().getUid();
        tab_value = intent.getStringExtra("tab_value");

        mDataProduct = FirebaseDatabase.getInstance().getReference().child("Product").child(tab_value);
        mDataProduct.child(product_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                String cost = (String) dataSnapshot.child("cost").getValue();
                String linkImage = (String) dataSnapshot.child("image").getValue();
                String userID = (String) dataSnapshot.child("user_id").getValue();
                String address = (String) dataSnapshot.child("city").getValue();
                String desc = (String) dataSnapshot.child("desc").getValue();
                String phone = (String) dataSnapshot.child("phone").getValue();
                txtTitleProduct.setText(name);
                txtCostProduct.setText(cost);
                txtAddress.setText(address);
                txtDescProduct.setText(desc);
                txtPhone.setText(phone);
                Picasso.with(getApplicationContext()).load(linkImage)
                        .placeholder(R.drawable.wait)
                        .into(imgSingleProduct);
                //btnRemove, btnUpdate hien neu la nguoi ban
                if(mAuth.getCurrentUser().getUid().equals(userID) ){
                    btnRemove.setVisibility(View.VISIBLE);
                    btnUpdate.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                }
                //btnSave hien neu ko paj nguoi ban
                if(!mAuth.getCurrentUser().getUid().equals(userID) ){
                    btnLuu.setVisibility(View.VISIBLE);


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDataLuu = FirebaseDatabase.getInstance().getReference().child("Save").child(current_user).child(product_key);
        mKtraLuu = FirebaseDatabase.getInstance().getReference().child("Save").child(current_user);
        mDeleteSave = FirebaseDatabase.getInstance().getReference().child("Save");
        ktraLuu();
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSave = true;
                LuuSP();

            }

        });
        btnBoLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSave = false;
                BoLuuSP();

            }

        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.show();
            }
        });

        b = new AlertDialog.Builder(SingleProductActivity.this);
        b.setTitle("Question");
        b.setMessage("Do you want to delete this product?");
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteProduct();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMove == false){
                    Move();
                    isMove= true;
                }
                else {
                    Back();

                    isMove= false;
                }
            }
        });

         final ChatFrame chatFrame = new ChatFrame(SingleProductActivity.this);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        mDataProduct.child(product_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                               chatFrame.Chat(mAuth.getCurrentUser().getUid(),dataSnapshot.child("user_id").getValue().toString());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                Back();
                isMove= false;
            }

        });



    }

    private void BoLuuSP() {
        if(isSave == false) {
            mKtraLuu.child(product_key).removeValue();
            btnBoLuu.setVisibility(View.INVISIBLE);
            btnLuu.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Bỏ lưu sản phẩm !!", Toast.LENGTH_LONG).show();
        }
    }

    private void ktraLuu() {
        mKtraLuu.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = (String) dataSnapshot.child("product_key").getValue();
                if(key != null) {
                if(key.equals(product_key)){
                    btnLuu.setVisibility(View.INVISIBLE);
                    btnBoLuu.setVisibility(View.VISIBLE);
                    isSave = true;
                }

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

    private void LuuSP() {

        if(isSave == true){
            mDataLuu.child("category").setValue(tab_value);
            mDataLuu.child("product_key").setValue(product_key);
            btnLuu.setVisibility(View.INVISIBLE);
            btnBoLuu.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Lưu sản phẩm thành công", Toast.LENGTH_LONG).show();
    }
    }

    private void updateProduct() {
        Intent intent = new Intent(SingleProductActivity.this, UpdateProduct.class);
        intent.putExtra("product_key", product_key);
        intent.putExtra("tab_value", tab_value);
        startActivity(intent);
    }

    private void deleteProduct() {
        mDataProduct.child(product_key).removeValue();
        mDeleteSave.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String key = dataSnapshot.getKey();
                mDeleteSave.child(key).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String delete_key = dataSnapshot.child("product_key").getValue().toString();
                        if(delete_key.equals(product_key))   mDeleteSave.child(key).child(delete_key).removeValue();

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
        Toast.makeText(getApplicationContext(), "Xóa sản phẩm thành công", Toast.LENGTH_LONG).show();
    }

    private void Move(){
        //fab1
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        params.rightMargin = (int) (fab1.getWidth() *1.2);
        params.bottomMargin= (int) ((fab1.getHeight()) * 0.4);

        fab1.setLayoutParams(params);
        fab1.startAnimation(move1);
        //fab2
        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        params2.rightMargin = (int) (fab2.getWidth() *0.5);
        params2.bottomMargin= (int) (fab2.getHeight() * 1.2);

        fab2.setLayoutParams(params2);
        fab2.startAnimation(move2);

    }
    private void Back(){
        //fab1
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        params.rightMargin -=  (fab1.getWidth() *1.2);
        params.bottomMargin-= (int) (fab1.getHeight() * 0.4);

        fab1.setLayoutParams(params);
        fab1.startAnimation(back1);

        //fab2
        FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        params2.rightMargin -= (int) (fab2.getWidth() *0.5);
        params2.bottomMargin-= (int) (fab2.getHeight() * 1.2);

        fab2.setLayoutParams(params2);
        fab2.startAnimation(back2);

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

        }else if(item.getItemId()==R.id.action_mesage){
            startActivity(new Intent(getApplicationContext(), UserContactActivity.class));

        }else if(item.getItemId()==R.id.action_Add){
            startActivity(new Intent(getApplicationContext(), AddProductActivity.class));

        }


        return super.onOptionsItemSelected(item);
    }


    private void Init() {
        txtTitleProduct = (TextView) findViewById(R.id.textViewNameProduct);
        txtCostProduct = (TextView) findViewById(R.id.textViewCostProduct);
        txtAddress = (TextView) findViewById(R.id.textViewAddress);
        txtDescProduct = (TextView) findViewById(R.id.textViewDescription);
        txtPhone = (TextView) findViewById(R.id.textViewPhone);
        imgSingleProduct = (ImageView) findViewById(R.id.imageViewSingleProduct);
        btnRemove  = (ImageButton) findViewById(R.id.buttonRemove);
        btnUpdate  = (ImageButton) findViewById(R.id.buttonUpdate);
        btnLuu  = (ImageButton) findViewById(R.id.buttonLuu);
        btnBoLuu  = (ImageButton) findViewById(R.id.buttonBoLuu);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);

        move1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move1);
        back1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.back1);

        move2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move2);
        back2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.back2);

    }


}
