package com.oleg.oskfin.ui.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.oleg.oskfin.R;
import com.oleg.oskfin.data.FireDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class DialogAddPoint extends DialogFragment implements View.OnClickListener {

    private MaterialEditText nameOfPoint;
    private Bundle bundle;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_point, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        nameOfPoint = view.findViewById(R.id.add_point_point_name);
        Button cancelButton = view.findViewById(R.id.add_point_cancel_button);
        Button addButton = view.findViewById(R.id.add_point_add_button);

        bundle = getArguments();
        if (bundle != null)
            nameOfPoint.setText(bundle.getString("nameOfPoint"));

        cancelButton.setOnClickListener(DialogAddPoint.this);
        addButton.setOnClickListener(DialogAddPoint.this);


        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.add_point_cancel_button):
                requireArguments().clear();
                dismiss();
                break;
            case (R.id.add_point_add_button):
                if (!TextUtils.isEmpty(Objects.requireNonNull(nameOfPoint.getText()).toString())) {
                    if (bundle == null)
                        FireDatabase.addPoint(nameOfPoint.getText().toString());
                    else
                        FireDatabase.editPoint(bundle.getString("nameOfPoint"), nameOfPoint.getText().toString());
                } else
                    Toast.makeText(getActivity(), R.string.enter_the_point_name, Toast.LENGTH_LONG).show();
                dismiss();
                break;
        }
    }
}
