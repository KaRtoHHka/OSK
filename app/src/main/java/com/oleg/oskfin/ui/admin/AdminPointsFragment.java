package com.oleg.oskfin.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.oleg.oskfin.R;
import com.oleg.oskfin.data.FireDatabase;
import com.oleg.oskfin.data.MyCallBackArray;
import com.oleg.oskfin.ui.StoreElementAdapter;

import java.util.ArrayList;
import java.util.Objects;

public class AdminPointsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private ListView list;
    private DialogFragment dialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_points, container, false);

        list = rootView.findViewById(R.id.admin_points_list);
        list.setOnItemLongClickListener(this);
        Button addButton = rootView.findViewById(R.id.add_point_button);
        addButton.setOnClickListener(this);

        final ArrayList<String> stringArrayList = new ArrayList<>();

        FireDatabase.getPoints(new MyCallBackArray() {
            @Override
            public void onCallBackArray(String values, int iter) {
                stringArrayList.add(values);
                if (iter == stringArrayList.size()) {
                    String[] points = stringArrayList.toArray(new String[0]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(), R.layout.point_item, points);
                    list.setAdapter(adapter);
                }
            }
        });


        return rootView;
    }

    @Override
    public void onClick(View view) {
        dialog = new DialogAddPoint();
        dialog.setTargetFragment(AdminPointsFragment.this, 1);
        dialog.show(getActivity().getSupportFragmentManager(), dialog.getClass().getName());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        dialog = new DialogLongClick();
        Bundle bundle = new Bundle();
        bundle.putString("fragment", "points");
        bundle.putString("nameOfPoint", ((TextView) view).getText().toString());
        dialog.setArguments(bundle);
        dialog.setTargetFragment(AdminPointsFragment.this, 1);
        dialog.show(getActivity().getSupportFragmentManager(), dialog.getClass().getName());
        return false;
    }
}
