package com.oleg.oskfin.ui.sender;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oleg.oskfin.MainScreenActivity;
import com.oleg.oskfin.R;
import com.oleg.oskfin.data.FireDatabase;
import com.oleg.oskfin.data.Orders;

import java.text.SimpleDateFormat;

public class SenderFragment extends Fragment implements View.OnTouchListener {

    private FirebaseListAdapter<Orders> adapter;
    private DatabaseReference myRef;
    private float x1, x2, y1, y2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sender, container, false);
        myRef = FirebaseDatabase.getInstance().getReference("Orders");
        ListView orders = root.findViewById(R.id.orderList);
        adapter = new FirebaseListAdapter<Orders>(getActivity(), Orders.class, R.layout.adapter_orders, myRef) {
            @Override
            protected void populateView(View v, Orders model, int position) {
                TextView item, point, time;
                item = v.findViewById(R.id.adapterOrderItem);
                point = v.findViewById(R.id.adapterOrderPoint);
                time = v.findViewById(R.id.adapterOrderTime);

                item.setText(model.getItem());
                point.setText(model.getPoint());
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM");
                time.setText(dateFormat.format(model.getOrderTime()));
            }
        };
        root.setOnTouchListener(this);
        orders.setOnTouchListener(this);
        orders.setAdapter(adapter);

        orders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                FireDatabase.removeOrder(adapter.getItem(i).getOrderTime());
                return false;
            }
        });

        return root;
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
}
