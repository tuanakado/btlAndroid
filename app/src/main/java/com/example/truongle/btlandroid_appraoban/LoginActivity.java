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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;
    EditText edtEmail,edtPassword;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Init();
        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(edtEmail.getText().toString(),edtPassword.getText().toString());

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    private void login(String email, String password) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty((password))) {
            mProgress.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Đăng Nhập Thành Công", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                mProgress.dismiss();
                            } else {

                                Toast.makeText(getApplicationContext(), "Đăng Nhập Thất Bại", Toast.LENGTH_LONG).show();
                                mProgress.dismiss();
                            }
                        }
                    });
        }
        else Toast.makeText(getApplicationContext(), "Username, Password không được để trống", Toast.LENGTH_LONG).show();
    }

    private void Init() {
        edtEmail = (EditText) findViewById(R.id.editTextEmail);
        edtPassword = (EditText) findViewById(R.id.editTextPassword);
        btnLogin= (Button) findViewById(R.id.buttonLogin);
        btnRegister= (Button) findViewById(R.id.buttonRegister);

    }
}
