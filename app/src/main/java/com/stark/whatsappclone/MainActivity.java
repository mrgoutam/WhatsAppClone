package com.stark.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText mPhoneNumber, mCode;
    private Button mSend;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    String mVerificationCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        //checking if user is already logged in
        userIsLoggedIn();

        mPhoneNumber = findViewById(R.id.phoneNumber);
        mCode = findViewById(R.id.code);

        mSend = findViewById(R.id.send);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVerificationCode != null){
                    verifyPhoneNumberWithCode();
                }else {
                    startPhoneVerification();
                }
            }
        });

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredentials(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                e.printStackTrace();
            }

            @Override
            public void onCodeSent(String verification, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verification, forceResendingToken);
                mVerificationCode = verification;
                mSend.setText("Verify Code");
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Toast.makeText(MainActivity.this, "CodeAutoRetrievalTimeOutOccurred: "+s, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void verifyPhoneNumberWithCode(){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationCode, mCode.getText().toString());
        signInWithPhoneAuthCredentials(credential);
    }

    private void signInWithPhoneAuthCredentials(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    userIsLoggedIn();
                }
            }
        });
    }

    private void userIsLoggedIn() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        // Extra security
        // even if everything was is OK but something gone wrong
        if (fUser != null){
            startActivity(new Intent(this, MainPageActivity.class));
            finish();
            return;
        }
    }

    private void startPhoneVerification() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mPhoneNumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                this,

                //Call back: Failure or Success
                mCallBacks
        );
    }
}