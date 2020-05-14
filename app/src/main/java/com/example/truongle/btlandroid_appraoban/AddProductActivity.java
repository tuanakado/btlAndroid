package com.example.truongle.btlandroid_appraoban;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class AddProductActivity extends AppCompatActivity {

    ImageView imgView;
    EditText edtName, edtCost, edtPhone, edtDesc;
    Button btnAdd;
    Spinner spnLoai, spnTP, spnLoaiMatHang;
    private ArrayList<String> listSpinnerLoai ;
    private ArrayList<String> listSpinnerLoaiMH = new ArrayList<>();
    private ArrayList<String> listSpinnerTP = new ArrayList<>();

    private static int GALLERY_REQUEST =1;
    private static int CAMERA_REQUEST = 0;
    private int choose = 0;
    private Intent pictureActionIntent = null;
    private Uri imageUri;
    private Bitmap bitmap;
    private UploadTask uploadTask;
    private StorageReference mStorage;
    private DatabaseReference root;
    private ProgressDialog mProgress;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mProgress = new ProgressDialog(this);
        mStorage = FirebaseStorage.getInstance().getReference();
        root = FirebaseDatabase.getInstance().getReference().child("Product");

        mAuth= FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        Init();
        InitSpinnerDiaChi();
        InitSpinnerLoaiMatHang();

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();
            }
        });
    }
    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                AddProductActivity.this);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(
                                pictureActionIntent,
                                GALLERY_REQUEST);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,
                                CAMERA_REQUEST);

                    }
                });
        myAlertDialog.show();
    }
    private void startPosting() {
        mProgress.setMessage("Posting to product .....");
        mProgress.show();
        final String name = edtName.getText().toString().trim();
        final String cost = edtCost.getText().toString().trim();
        final String phone = edtPhone.getText().toString().trim();
        final String loaiMH = spnLoaiMatHang.getSelectedItem().toString();
        final String category = spnLoai.getSelectedItem().toString();
        final String city = spnTP.getSelectedItem().toString();
        final String desc = edtDesc.getText().toString();
        Calendar calendar = Calendar.getInstance();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(cost) && !TextUtils.isEmpty(category) ){

            if(choose == 2 && imageUri !=null){
            StorageReference filepath = mStorage.child("ProductImage").child(calendar.getTimeInMillis()+imageUri.getLastPathSegment());
            uploadTask=filepath.putFile(imageUri);
            }
           else if(choose == 1 && bitmap!= null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();
                StorageReference mountainsRef = mStorage.child("ImageCamera").child(calendar.getTimeInMillis()+"image.png"); Log.d("AAAA", "9: ");
                uploadTask = mountainsRef.putBytes(data);
            }
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri dowloadUri = taskSnapshot.getDownloadUrl();
                    mProgress.dismiss();
                    final DatabaseReference newProduct = root.child(loaiMH).push();

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newProduct.child("name").setValue(name);
                            newProduct.child("name_lower").setValue(name.toLowerCase());
                            newProduct.child("cost").setValue(cost);
                            newProduct.child("phone").setValue(phone);
                            newProduct.child("desc").setValue(desc);
                            newProduct.child("city").setValue(city);
                            newProduct.child("category").setValue(category);
                            newProduct.child("image").setValue(dowloadUri.toString());

                            newProduct.child("user_id").setValue(mCurrentUser.getUid());
                            newProduct.child("username").setValue(dataSnapshot.child("name").getValue());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mProgress.dismiss();
                    startActivity( new Intent(getApplicationContext(),MainActivity.class));
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data!= null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imgView.setImageBitmap(bitmap);
            choose = 1;
        }else if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            imgView.setImageURI(imageUri);
            choose =2;
        }


    }

    private void Init() {
        imgView = (ImageView) findViewById(R.id.imageButtonImage);
        edtName = (EditText) findViewById(R.id.editTextNameProduct);
        edtCost = (EditText) findViewById(R.id.editTextCostProduct);
        edtPhone = (EditText) findViewById(R.id.editTextPhone);
        edtDesc = (EditText) findViewById(R.id.editTextDesc);
        btnAdd = (Button) findViewById(R.id.buttonAdd);

        spnLoai = (Spinner) findViewById(R.id.spinnerLoaiSP);
        spnLoaiMatHang = (Spinner) findViewById(R.id.spinnerLoaiMatHang);
        spnTP = (Spinner) findViewById(R.id.spinnerTP);
    }
    private void InitSpinnerLoaiMatHang(){

        listSpinnerLoaiMH.add("--Chọn Mặt Hàng--");
        listSpinnerLoaiMH.add("Đồ Điện Tử");
        listSpinnerLoaiMH.add("Đồ Cá Nhân");
        listSpinnerLoaiMH.add("Đồ Thể Thao");
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listSpinnerLoaiMH);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoaiMatHang.setAdapter(adapter1);
        spnLoaiMatHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position ==1){
                    InitSpinnerDoDienTu();
                }else if(position ==2){
                    InitSpinnerDoCaNhan();
                }
                else if(position ==3){
                    InitSpinnerDoTheThao();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void InitSpinnerDoDienTu(){
        listSpinnerLoai = new ArrayList<>();
        listSpinnerLoai.add("Laptop");
        listSpinnerLoai.add("Điện Thoại");
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listSpinnerLoai);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoai.setAdapter(adapter1);
    }
    private void InitSpinnerDoCaNhan(){
        listSpinnerLoai = new ArrayList<>();
        listSpinnerLoai.add("Đồng Hồ");
        listSpinnerLoai.add("sss");
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listSpinnerLoai);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoai.setAdapter(adapter1);
    }
    private void InitSpinnerDoTheThao(){
        listSpinnerLoai = new ArrayList<>();
        listSpinnerLoai.add("Giày");
        listSpinnerLoai.add("Áo Thể Thao");
        listSpinnerLoai.add("Vợt");
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listSpinnerLoai);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLoai.setAdapter(adapter1);
    }
    private void InitSpinnerDiaChi(){
        listSpinnerTP.add("Hà Nội");
        listSpinnerTP.add("TP.Hcm");
        listSpinnerTP.add("Bắc Ninh");
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listSpinnerTP);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTP.setAdapter(adapter1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_update){
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));

        }
        else if(item.getItemId()==R.id.action_home){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }


        return super.onOptionsItemSelected(item);
    }
}
