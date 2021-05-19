package com.oleg.oskfin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            FirebaseAuth.getInstance().signOut();
        } catch (Exception ignored){

        }


        auth = FirebaseAuth.getInstance();

    }

    public void signUp(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void signIn(View view) {
        MaterialEditText mail = findViewById(R.id.MailText);
        MaterialEditText pass = findViewById(R.id.PasswordText);

        if (TextUtils.isEmpty(mail.getText().toString()) && TextUtils.isEmpty(pass.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.error_empty, Toast.LENGTH_LONG).show();
            return;
        }

        auth.signInWithEmailAndPassword(mail.getText().toString(), pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), R.string.invalid_data_to_sign_in, Toast.LENGTH_LONG).show();
            }
        });
    }
}