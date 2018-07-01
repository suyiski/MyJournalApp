package com.suizexpressions.myjournalapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{

    private static String TAG = LoginActivity.class.getSimpleName();

    private SignInButton mGoogleSignInButton;
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_CODE = 1000;
    static GoogleSignInAccount mGoogleSignInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mGoogleSignInButton = findViewById(R.id.btn_google_sign_in);
        mGoogleSignInButton.setOnClickListener(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_google_sign_in) {
            if (isNetworkAvailable()) {
                signIn();
            } else {
                Toast.makeText(LoginActivity.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        }

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this, "Error connecting to the internet", Toast.LENGTH_LONG).show();
    }

    private void signIn() {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, REQUEST_CODE);
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
            }
        });

    }

    public void handleResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            Toast.makeText(LoginActivity.this, "Sign in Successful", Toast.LENGTH_LONG).show();
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("username", googleSignInAccount.getDisplayName());
            intent.putExtra("userEmail", googleSignInAccount.getEmail());
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "Error Logging in", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
