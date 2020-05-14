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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class UpdateProduct extends AppCompatActivity {

    private DatabaseReference mDataProduct;
    private EditText edtName, edtCost, edtDesc, edtPhone;
    private Spinner spnLoai, spnTP, spnLoaiMatHang;
    private ArrayList<String> listSpinnerLoai= new ArrayList<>();
    private ArrayList<String> listSpinnerLoaiMH= new ArrayList<>();
    private ArrayList<String > listSpinnerTP= new ArrayList<>();
    ImageButton imgHinh;
    Button btnEdit;
    private ProgressDialog mProgress;
    private Uri imageUri;
    private Bitmap bitmap;
    private UploadTask uploadTask;
    private int choose = 0;
    private StorageReference mStorage;
    private DatabaseReference root;
    private String product_key;
    private static int GALLERY_REQUEST =1;
    private static int CAMERA_REQUEST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        AddUI();
        InitSpinnerDiaChi();
        InitSpinnerLoaiMatHang();
        mProgress = new ProgressDialog(this);
        Intent intent = getIntent();
         product_key = intent.getStringExtra("product_key");
        String tab_value = intent.getStringExtra("tab_value");
        mStorage = FirebaseStorage.getInstance().getReference();
        mDataProduct = FirebaseDatabase.getInstance().getReference().child("Product").child(tab_value);
        root = FirebaseDatabase.getInstance().getReference().child("Product");
        mDataProduct.child(product_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                String cost = (String) dataSnapshot.child("cost").getValue();
                String linkImage = (String) dataSnapshot.child("image").getValue();
                String desc = (String) dataSnapshot.child("desc").getValue();
                String address = (String) dataSnapshot.child("city").getValue();
                String phone = (String) dataSnapshot.child("desc").getValue();
                edtName.setText(name);
                edtCost.setText(cost);
                edtDesc.setText(desc);
                edtPhone.setText(phone);
                Picasso.with(getApplicationContext()).load(linkImage).into(imgHinh);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imgHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProduct();
            }
        });
    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                UpdateProduct.this);
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
    private void EditProduct() {
        mProgress.setMessage("Edit product .....");
        mProgress.show();
        final String name = edtName.getText().toString().trim();
        final String cost = edtCost.getText().toString().trim();
        final String desc = edtDesc.getText().toString().trim();
        final String phone = edtPhone.getText().toString().trim();
        final String category = spnLoai.getSelectedItem().toString();
        final String city = spnTP.getSelectedItem().toString();
        Calendar calendar = Calendar.getInstance();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(cost)) {
            if (choose == 0) {
                mDataProduct.child(product_key).child("name").setValue(name);
                mDataProduct.child(product_key).child("cost").setValue(cost);
                mDataProduct.child(product_key).child("desc").setValue(desc);
                mDataProduct.child(product_key).child("phone").setValue(phone);
                mDataProduct.child(product_key).child("city").setValue(city);

                mProgress.dismiss();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                if (choose == 2 && imageUri != null) {
                    StorageReference filepath = mStorage.child("ProductImage").child(calendar.getTimeInMillis() + imageUri.getLastPathSegment());
                    uploadTask = filepath.putFile(imageUri);
                } else if (choose == 1 && bitmap != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();
                    StorageReference mountainsRef = mStorage.child("ImageCamera").child(calendar.getTimeInMillis() + "image.png");

                    uploadTask = mountainsRef.putBytes(data); Log.d("AAAA", "8: ");
                }
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Uri dowloadUri = taskSnapshot.getDownloadUrl();
                        mDataProduct.child(product_key).child("name").setValue(name);
                        mDataProduct.child(product_key).child("cost").setValue(cost);
                        mDataProduct.child(product_key).child("desc").setValue(desc);
                        mDataProduct.child(product_key).child("phone").setValue(desc);
                        mDataProduct.child(product_key).child("city").setValue(city);
                        mDataProduct.child(product_key).child("image").setValue(""+dowloadUri);
                        mProgress.dismiss();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                });
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data!= null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imgHinh.setImageBitmap(bitmap);
            choose = 1;
        }else if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageUri = data.getData();
            imgHinh.setImageURI(imageUri);
            choose =2;
        }


    }

    private void AddUI() {
        edtName = (EditText) findViewById(R.id.editTextUpdateNameProduct);
        edtCost = (EditText) findViewById(R.id.editTextUpdateCostProduct);
        edtDesc = (EditText) findViewById(R.id.editTextUpdateDesc);
        edtPhone = (EditText) findViewById(R.id.editTextUpdatePhone);
        spnLoai = (Spinner) findViewById(R.id.spinnerUpdateLoaiSP);
        spnLoaiMatHang = (Spinner) findViewById(R.id.spinnerUpdateLoaiMatHang);
        spnTP = (Spinner) findViewById(R.id.spinnerUpdateTP);
        imgHinh = (ImageButton) findViewById(R.id.imageButtonUpdateImage);
        btnEdit = (Button) findViewById(R.id.buttonEdit);
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
}
