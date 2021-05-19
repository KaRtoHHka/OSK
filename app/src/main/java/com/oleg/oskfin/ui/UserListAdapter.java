package com.oleg.oskfin.ui;

import android.content.Context;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.oleg.oskfin.R;
import com.oleg.oskfin.data.Users;

import static com.oleg.oskfin.R.layout.adapter_user_list;

public class UserListAdapter extends ArrayAdapter<String> {
    private final Context context;
    public final String[] name;
    public static Users[] users;

    public UserListAdapter(@NonNull Context context, String[] name, Users[] users) {
        super(context, adapter_user_list, name);
        this.context = context;
        this.name = name;
        UserListAdapter.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(adapter_user_list, parent, false);
        TextView textName = rowView.findViewById(R.id.userListName);
        TextView textSurname = rowView.findViewById(R.id.userListSurname);
        TextView textMoney = rowView.findViewById(R.id.userListMoneyValue);
        TextView textPhone = rowView.findViewById(R.id.userListPhone);
        ImageView onLineView = rowView.findViewById(R.id.userListOnLine);

        textName.setText(name[position]);
        textSurname.setText(users[position].getSurname());
        textMoney.setText(users[position].getMoney() + "");
        textPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        textPhone.setText(users[position].getPhoneNumber());

        if (users[position].isInGame())
            onLineView.setVisibility(View.VISIBLE);
        else onLineView.setVisibility(View.INVISIBLE);

        return rowView;
    }

}
