package com.oleg.oskfin.ui.store;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.oleg.oskfin.MainScreenActivity;
import com.oleg.oskfin.R;
import com.oleg.oskfin.data.FireDatabase;
import com.oleg.oskfin.data.MyCallBack;
import com.oleg.oskfin.data.MyCallBackStore;
import com.oleg.oskfin.data.Store;
import com.oleg.oskfin.data.Users;
import com.oleg.oskfin.ui.StoreElementAdapter;

import java.util.ArrayList;

public class StoreFragment extends Fragment implements View.OnTouchListener {

    private ListView list;
    private LinearLayout layout;
    private TextView money;
    private Users users;
    private DialogFragment dialog;
    private float x1, x2, y1, y2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_store, container, false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        root.setOnTouchListener(this);
        money = root.findViewById(R.id.storeMoney);
        layout = root.findViewById(R.id.fragmentStoreLayout);
        list = root.findViewById(R.id.productList);
        list.setOnTouchListener(this);

        FireDatabase.getData(new MyCallBack() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCallBack(Users value) {
                users = value;
                if (!users.isInGame()) {
                    layout.addView(getLayoutInflater().inflate(R.layout.inactive_store, null));
                    progressDialog.dismiss();
                } else {
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
                                StoreElementAdapter storeElementAdapter = new StoreElementAdapter(getContext(), names, values, users.getMoney(), true);
                                list.setAdapter(storeElementAdapter);
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
                money.setText(users.getMoney() + "");
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (users.getMoney() >= StoreElementAdapter.product[position].getCostOfProduct()) {
                    users.setMoney(users.getMoney() - StoreElementAdapter.product[position].getCostOfProduct());
                    Bundle infoToDialog = new Bundle();
                    infoToDialog.putSerializable("user", users);
                    infoToDialog.putSerializable("item", StoreElementAdapter.product[position]);

                    dialog = new DialogChoosePoint();
                    dialog.setArguments(infoToDialog);
                    dialog.setTargetFragment(StoreFragment.this, 1);
                    dialog.show(getFragmentManager(), dialog.getClass().getName());
                } else {
                    Toast.makeText(getContext(), getString(R.string.not_enough_money), Toast.LENGTH_LONG).show();
                }

            }
        });
        return root;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                x1 = event.getX();
                y1 = event.getY();
                break;
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
}
