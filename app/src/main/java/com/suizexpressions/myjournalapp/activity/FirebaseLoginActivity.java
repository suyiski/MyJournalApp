package com.suizexpressions.myjournalapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.suizexpressions.myjournalapp.R;

public class FirebaseLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mButtonLogin;
    private Button mButtonRegister;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_login);
        mAuth = FirebaseAuth.getInstance();

        mButtonLogin = findViewById(R.id.btnLogin);
        mButtonRegister = findViewById(R.id.btnRegister);
        mEditTextEmail = findViewById(R.id.edit_text_email);
        mEditTextPassword = findViewById(R.id.edit_text_password);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(FirebaseLoginActivity.this, FirebaseActivity.class));
                }
            }
        };


        mButtonLogin.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn() {
        String emailAddress = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();

        if (TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)) {
            Toast.makeText(FirebaseLoginActivity.this, "Please enter a valid email or password",
                    Toast.LENGTH_LONG).show();

        } else {

            mAuth.signInWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(FirebaseLoginActivity.this, "There was a problem signing in",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void registerUser() {
        String newUserEmail = mEditTextEmail.getText().toString();
        String newUserPassword = mEditTextPassword.getText().toString();

        if (TextUtils.isEmpty(newUserEmail) || TextUtils.isEmpty(newUserPassword)) {
            Toast.makeText(FirebaseLoginActivity.this, "Please enter a valid email or password",
                    Toast.LENGTH_LONG).show();

        } else {
            mAuth.createUserWithEmailAndPassword(newUserEmail, newUserPassword).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                Toast.makeText(FirebaseLoginActivity.this, "There was a problem creating the account",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogin:
                startSignIn();
                break;
            case R.id.btnRegister:
                registerUser();
                break;
        }

    }

}

