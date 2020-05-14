package com.example.truongle.btlandroid_appraoban;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateAcount extends AppCompatActivity {

    ImageView imgHinhDaiDien;
    EditText edtNickName;
    Button btnSubmit;
    private static final int GALERY_REQUEST = 1234;
    private Uri resultUri = null;
    private Uri imageUri = null;

    private DatabaseReference mDataUser;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_acount);

        mProgress = new ProgressDialog(this);
        mDataUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference().child("profile_Image");
        Init();
        imgHinhDaiDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galery = new Intent();
                galery.setAction(Intent.ACTION_GET_CONTENT);
                galery.setType("image/*");
                startActivityForResult(galery,GALERY_REQUEST);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpdate();
            }
        });
    }

    private void startUpdate() {
        mProgress.show();
        final String name = edtNickName.getText().toString().trim();
        final String user_id = mAuth.getCurrentUser().getUid();
        if(!TextUtils.isEmpty(name)){
            StorageReference filePath = mStorage.child(imageUri.getLastPathSegment());
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String dowloadUri = taskSnapshot.getDownloadUrl().toString();
                    mDataUser.child(user_id).child("name").setValue(name);
                    mDataUser.child(user_id).child("image").setValue(dowloadUri);

                    mProgress.dismiss();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== GALERY_REQUEST && resultCode == RESULT_OK){
             imageUri  = data.getData();
//            CropImage.activity(imageUri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1, 1)
//                    .start(this);
            imgHinhDaiDien.setImageURI(imageUri);
            Log.d("AAAA", "onActivityResult: 1111");
        }
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//                imgHinhDaiDien.setImageURI(resultUri);
//            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Exception error = result.getError();
//            }
//        }
    }

    private void Init() {
        imgHinhDaiDien = (ImageView) findViewById(R.id.imageViewHinh);
        edtNickName = (EditText) findViewById(R.id.editTextNickname);
        btnSubmit = (Button) findViewById(R.id.buttonSubmitSetUp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_update){
            startActivity(new Intent(getApplicationContext(), UpdateAcount.class));

        }
        else if(item.getItemId()==R.id.action_home){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }


        return super.onOptionsItemSelected(item);
    }

}
