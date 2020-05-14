package com.example.truongle.btlandroid_appraoban;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword;
    Button btnRegister;


    private FirebaseAuth mAuth;
    private DatabaseReference root;
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Iniṭ();
        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance().getReference().child("Users");
        mProgress = new ProgressDialog(this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }
    private void startRegister() {
        final String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) ){
            mProgress.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String user_id = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_id = root.child(user_id);
                                current_user_id.child("name").setValue(name);
                                current_user_id.child("image").setValue("default");
                                mProgress.dismiss();

                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            }
                        }
                    });
        }
    }

    private void Iniṭ() {
        edtName = (EditText) findViewById(R.id.editTextName);
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtPassword = (EditText) findViewById(R.id.editTextPassword);
        btnRegister = (Button) findViewById(R.id.buttonRegister);
        //   imgHinhDaiDien = (ImageView) findViewById(R.id.imageViewHinhDaiDien);
    }
}
