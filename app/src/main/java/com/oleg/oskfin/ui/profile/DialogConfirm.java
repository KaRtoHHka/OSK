package com.oleg.oskfin.ui.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.oleg.oskfin.R;
import com.oleg.oskfin.data.FireDatabase;
import com.oleg.oskfin.ui.admin.DialogAdminSet;

import java.util.Objects;

public class DialogConfirm extends DialogFragment implements View.OnClickListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_confirm, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Button yes = view.findViewById(R.id.confirm_button_yes);
        Button no = view.findViewById(R.id.confirm_button_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.confirm_button_no):
                dismiss();
                break;
            case (R.id.confirm_button_yes):
                Bundle bundle = getArguments();
                FireDatabase.removeUser(bundle.getString("phone"));
        }
    }
}
