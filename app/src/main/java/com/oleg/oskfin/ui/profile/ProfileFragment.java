package com.oleg.oskfin.ui.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.oleg.oskfin.MainScreenActivity;
import com.oleg.oskfin.R;
import com.oleg.oskfin.data.FireDatabase;
import com.oleg.oskfin.data.MyCallBack;
import com.oleg.oskfin.data.Users;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ProfileFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private MaterialEditText name, surname, middleName, mail, phoneNumber, password, secondPassword;
    private Users user;
    private DialogFragment dialog;
    private float x1, x2, y1, y2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        root.setOnTouchListener(this);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        name = root.findViewById(R.id.profEditName);
        surname = root.findViewById(R.id.profEditSurname);
        middleName = root.findViewById(R.id.profEditMiddleName);
        phoneNumber = root.findViewById(R.id.profEditPhoneNumber);
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mail = root.findViewById(R.id.profEditMail);
        password = root.findViewById(R.id.profEditPassword);
        secondPassword = root.findViewById(R.id.profEditPasswordCheck);

        Button save = root.findViewById(R.id.save);
        Button delete = root.findViewById(R.id.profile_remove_button);

        FireDatabase.getData(new MyCallBack() {
            @Override
            public void onCallBack(Users value) {
                fillAll(value);
                user = value;
                progressDialog.dismiss();
            }
        });


        save.setOnClickListener(this);
        delete.setOnClickListener(this);
        return root;
    }

    private void fillAll(Users users) {
        if (users.getName() != null) {
            name.setText(users.getName());
        }
        if (users.getSurname() != null) {
            surname.setText(users.getSurname());
        }
        if (users.getMiddleName() != null) {
            middleName.setText(users.getMiddleName());
        }
        if (users.getMail() != null) {
            mail.setText(users.getMail());
        }
        if (users.getPhoneNumber() != null) {
            phoneNumber.setText(users.getPhoneNumber());
        }
        if (users.getPassword() != null) {
            password.setText(users.getPassword());
            secondPassword.setText(users.getPassword());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.save):
                save();
                break;
            case (R.id.profile_remove_button):
                remove();
                break;
        }
    }

    private void remove() {
        dialog = new DialogConfirm();
        Bundle bundle = new Bundle();
        bundle.putString("phone", user.getPhoneNumber() + "");
        dialog.setArguments(bundle);
        dialog.setTargetFragment(ProfileFragment.this, 1);
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                x1 = event.getX();
                y1 = event.getY();break;
            case MotionEvent.ACTION_UP: // отпускание
                x2 = event.getX();
                y2 = event.getY();
                if ((x2 - x1 >= 400.0) && (y2 - y1 <= 200.0) && (y2 - y1 >= -200.0)) {
                    MainScreenActivity.mOpenDrawer();
                }
                break;

        }
        return true;
    }

    private void save() {
        if (!TextUtils.isEmpty(name.getText().toString())) {
            user.setName(name.getText().toString());
        }
        if (!TextUtils.isEmpty(surname.getText().toString())) {
            user.setSurname(surname.getText().toString());
        }
        if (!TextUtils.isEmpty(middleName.getText().toString())) {
            user.setMiddleName(middleName.getText().toString());
        }
        if (!TextUtils.isEmpty(phoneNumber.getText().toString())) {
            user.setPhoneNumber(phoneNumber.getText().toString());
        }
        if (!TextUtils.isEmpty(mail.getText().toString())) {
            user.setMail(mail.getText().toString());
        }
        if (!TextUtils.isEmpty(password.getText().toString())) {
            if (password.getText().toString().equals(secondPassword.getText().toString()))
                user.setPassword(password.getText().toString());
            else {
                Toast.makeText(getActivity(), R.string.error_password_dont_match, Toast.LENGTH_LONG).show();
                return;
            }
        }
        FireDatabase.setData(user);
        Toast.makeText(getActivity(), R.string.complete, Toast.LENGTH_LONG).show();
        fillAll(user);
    }

}
