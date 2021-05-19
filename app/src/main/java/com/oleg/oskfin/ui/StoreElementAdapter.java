package com.oleg.oskfin.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.oleg.oskfin.R;
import com.oleg.oskfin.data.Store;

public class StoreElementAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] nameOfProduct;
    public static Store[] product;
    private int userMoney;
    private boolean need;
    //    private static Bitmap[] bitmaps;

    public StoreElementAdapter(@NonNull Context context, String[] nameOfProduct, Store[] product, int userMoney, boolean need) {
        super(context, R.layout.adapter_user_list, nameOfProduct);
        this.context = context;
        this.nameOfProduct = nameOfProduct;
        StoreElementAdapter.product = product;
        this.userMoney = userMoney;
        this.need = need;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adapter_store_element, parent, false);
        TextView nameOfProductText = rowView.findViewById(R.id.nameOfProduct);
        TextView aboutProductText = rowView.findViewById(R.id.aboutProduct);
        TextView costOfProductText = rowView.findViewById(R.id.costOfProduct);
        ImageView imageOfProductText = rowView.findViewById(R.id.imageOfProduct);
        RelativeLayout relativeLayout = rowView.findViewById(R.id.adapterStoreElementOne);
        if (userMoney >= product[position].getCostOfProduct() && need){
            relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.not_enough_money_color));
        }
        nameOfProductText.setText(nameOfProduct[position]);
        aboutProductText.setText(product[position].getAboutProduct());
        costOfProductText.setText(product[position].getCostOfProduct() + "");
        Glide.with(context).load(product[position].getImageOfProduct()).into(imageOfProductText);

        return rowView;
    }
}