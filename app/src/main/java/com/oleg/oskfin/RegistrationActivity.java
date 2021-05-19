package com.oleg.oskfin;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oleg.oskfin.data.Users;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private MaterialEditText name;
    private MaterialEditText surname;
    private MaterialEditText phoneNumber;
    private MaterialEditText mail;
    private MaterialEditText password;
    private MaterialEditText secondPassword;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");
        name = findViewById(R.id.editName);
        surname = findViewById(R.id.editSurname);
        phoneNumber = findViewById(R.id.editPhoneNumber);
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mail = findViewById(R.id.editMail);
        password = findViewById(R.id.editPassword);
        secondPassword = findViewById(R.id.editPasswordCheck);
    }

    public void signUpAdd(View view) {
        if (TextUtils.isEmpty(Objects.requireNonNull(name.getText()).toString())
                || TextUtils.isEmpty(Objects.requireNonNull(surname.getText()).toString())
                || TextUtils.isEmpty(Objects.requireNonNull(mail.getText()).toString())
                || TextUtils.isEmpty(Objects.requireNonNull(password.getText()).toString())
                || TextUtils.isEmpty(Objects.requireNonNull(phoneNumber.getText()).toString())) {
            Toast.makeText(getApplicationContext(), R.string.error_empty, Toast.LENGTH_LONG).show();
        } else if (!password.getText().toString().equals(Objects.requireNonNull(secondPassword.getText()).toString())) {
            Toast.makeText(getApplicationContext(), R.string.error_password_dont_match, Toast.LENGTH_LONG).show();
        } else {
            auth.createUserWithEmailAndPassword(mail.getText().toString(), password.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Users user = new Users();
                            user.setName(name.getText().toString());
                            user.setSurname(surname.getText().toString());
                            user.setPhoneNumber(phoneNumber.getText().toString());
                            user.setMail(mail.getText().toString());
                            user.setId(Objects.requireNonNull(auth.getCurrentUser()).getUid());
                            if (password.getText().toString().length() <= 5) {
                                Toast.makeText(getApplicationContext(), R.string.invalid_password, Toast.LENGTH_LONG).show();
                                return;
                            }
                            user.setPassword(password.getText().toString());
                            user.setAdmin(false);
                            user.setInGame(false);
                            user.setSender(false);
                            users.child(auth.getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), R.string.registration_ok, Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void cancel(View view) {
        this.finish();
    }
}