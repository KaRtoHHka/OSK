package com.oleg.oskfin.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.oleg.oskfin.R;
import com.oleg.oskfin.data.FireDatabase;
import com.oleg.oskfin.data.MyCallBackStore;
import com.oleg.oskfin.data.Store;
import com.oleg.oskfin.ui.StoreElementAdapter;

import java.util.ArrayList;

public class AdminItemsFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private ListView list;
    private Button addButton;
    private DialogFragment dialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_items, container, false);

        list = rootView.findViewById(R.id.admin_items_list);
        addButton = rootView.findViewById(R.id.add_item_button);

        addButton.setOnClickListener(this);
        list.setOnItemLongClickListener(this);

        final ArrayList<Store> storeList = new ArrayList<>();
        FireDatabase.getProductList(new MyCallBackStore() {
            String[] names;
            Store[] values;

            @Override
            public void onCallBack(Store value, int iter) {
                storeList.add(value);
                if (iter == storeList.size()) {
                    names = new String[storeList.size()];
                    values = new Store[storeList.size()];
                    for (int i = 0; i < storeList.size(); i++) {
                        names[i] = storeList.get(i).getNameOfProduct();
                        values[i] = storeList.get(i);
                    }
                    StoreElementAdapter storeElementAdapter = new StoreElementAdapter(getContext(), names, values, 1000000, false);
                    list.setAdapter(storeElementAdapter);
                }
            }
        });


        return rootView;
    }

    @Override
    public void onClick(View view) {
        dialog = new DialogAddItem();
        dialog.setTargetFragment(AdminItemsFragment.this, 1);
        dialog.show(getFragmentManager(), dialog.getClass().getName());
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        dialog = new DialogLongClick();
        Bundle bundle = new Bundle();
        bundle.putString("fragment", "items");
        bundle.putSerializable("item", StoreElementAdapter.product[i]);
        dialog.setArguments(bundle);
        dialog.setTargetFragment(AdminItemsFragment.this, 1);
        dialog.show(getActivity().getSupportFragmentManager(), dialog.getClass().getName());
        return false;
    }
}

