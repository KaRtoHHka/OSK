package com.oleg.oskfin.ui.admin;

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
import com.oleg.oskfin.data.Store;

import java.util.Objects;

public class DialogLongClick extends DialogFragment implements View.OnClickListener {

    private Button editButton, deleteButton;
    private Bundle bundle;
    private DialogFragment dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_long_click, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        editButton = view.findViewById(R.id.long_click_edit_button);
        deleteButton = view.findViewById(R.id.long_click_delete_button);
        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        bundle = getArguments();

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.long_click_edit_button):
                switch (Objects.requireNonNull(bundle.getString("fragment"))) {
                    case ("items"):
                        dialog = new DialogAddItem();
                        dialog.setArguments(bundle);
                        dialog.setTargetFragment(DialogLongClick.this, 1);
                        dialog.show(requireActivity().getSupportFragmentManager(), dialog.getClass().getName());
                        dismiss();
                        break;
                    case ("points"):
                        dialog = new DialogAddPoint();
                        dialog.setArguments(bundle);
                        dialog.setTargetFragment(DialogLongClick.this, 1);
                        dialog.show(requireActivity().getSupportFragmentManager(), dialog.getClass().getName());
                        dismiss();
                        break;
                }
                break;
            case (R.id.long_click_delete_button):
                switch (Objects.requireNonNull(bundle.getString("fragment"))) {
                    case ("items"):
                        FireDatabase.removeItem((Store) Objects.requireNonNull(bundle.getSerializable("item")));
                        dismiss();
                        break;
                    case ("points"):
                        FireDatabase.removePoint(bundle.getString("nameOfPoint"));
                        dismiss();
                        break;
                }
                break;
        }
    }
}
