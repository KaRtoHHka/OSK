package com.oleg.oskfin.ui.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import com.oleg.oskfin.R;
import com.oleg.oskfin.data.FireDatabase;
import com.oleg.oskfin.data.MyCallBack;
import com.oleg.oskfin.data.Users;

import java.util.Objects;

public class DialogAdminSet extends DialogFragment implements View.OnClickListener {

    private TextView name, phone;
    private EditText money;
    private SwitchCompat inGame;
    private SwitchCompat admin;
    private SwitchCompat sender;
    private Users user;


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View[] view = new View[1];
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle id = getArguments();
        view[0] = inflater.inflate(R.layout.dialog_admin_set, null);

        name = view[0].findViewById(R.id.admin_set_name);
        phone = view[0].findViewById(R.id.admin_set_phone);
        money = view[0].findViewById(R.id.admin_set_money);
        inGame = view[0].findViewById(R.id.admin_set_in_game);
        admin = view[0].findViewById(R.id.admin_set_admin);
        sender = view[0].findViewById(R.id.admin_set_sender);
        Button saveBtn = view[0].findViewById(R.id.admin_set_save_button);
        Button cancelBtn = view[0].findViewById(R.id.admin_set_cancel_button);

        saveBtn.setOnClickListener(DialogAdminSet.this);
        cancelBtn.setOnClickListener(DialogAdminSet.this);
        if (id != null) {
            user = FireDatabase.findData(id.getString("phone"), new MyCallBack() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onCallBack(Users value) {
                    admin.setChecked(value.isAdmin());
                    inGame.setChecked(value.isInGame());
                    sender.setChecked(value.isSender());
                    name.setText(value.getSurname() + " " + value.getName());
                    phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
                    phone.setText(value.getPhoneNumber());
                    money.setText(value.getMoney() + "");
                    user = value;
                }
            });
        }
        builder.setView(view[0]);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.admin_set_save_button):
                user.setInGame(inGame.isChecked());
                user.setAdmin(admin.isChecked());
                user.setSender(sender.isChecked());
                user.setMoney(Integer.parseInt(money.getText().toString()));
                FireDatabase.setOtherData(user);
                requireArguments().clear();
                dismiss();
                break;
            case (R.id.admin_set_cancel_button):
                requireArguments().clear();
                dismiss();
                break;
        }
    }
}
