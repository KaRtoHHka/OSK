package com.oleg.oskfin.ui.store;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.oleg.oskfin.R;
import com.oleg.oskfin.data.FireDatabase;
import com.oleg.oskfin.data.MyCallBackArray;
import com.oleg.oskfin.data.Store;
import com.oleg.oskfin.data.Users;

import java.util.ArrayList;
import java.util.Objects;

public class DialogChoosePoint extends DialogFragment {

    private ListView pointList;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view;
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final Bundle info = getArguments();
        view = inflater.inflate(R.layout.dialog_choose_point, null);
        pointList = view.findViewById(R.id.pointList);

        final ArrayList<String> stringArrayList = new ArrayList<>();

        FireDatabase.getPoints(new MyCallBackArray() {
            @Override
            public void onCallBackArray(String values, int iter) {
                stringArrayList.add(values);
                if (iter == stringArrayList.size()) {
                    String[] points = stringArrayList.toArray(new String[0]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(), R.layout.point_item, points);
                    pointList.setAdapter(adapter);
                }
            }
        });

        pointList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FireDatabase.purchase((Users) Objects.requireNonNull(info.getSerializable("user")), (Store) Objects.requireNonNull(info.getSerializable("item")), ((TextView) view).getText().toString());
                Toast.makeText(getActivity(), R.string.succesfulPurchase, Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }
}
