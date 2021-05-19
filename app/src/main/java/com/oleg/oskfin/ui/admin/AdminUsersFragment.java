package com.oleg.oskfin.ui.admin;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.oleg.oskfin.R;
import com.oleg.oskfin.data.FireDatabase;
import com.oleg.oskfin.data.MyCallBack;
import com.oleg.oskfin.data.MyCallBackUsers;
import com.oleg.oskfin.data.Users;
import com.oleg.oskfin.ui.UserListAdapter;
import com.oleg.oskfin.ui.StoreElementAdapter;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Objects;

public class AdminUsersFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView list;
    private MaterialEditText phone;
    private DialogFragment dialog;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_users, container, false);
        list = rootView.findViewById(R.id.admin_user_list);
        phone = rootView.findViewById(R.id.adminPhone);
        Button findButton = rootView.findViewById(R.id.find_button);

        final ArrayList<Users> userList = new ArrayList<>();
        FireDatabase.fillAllUsers(new MyCallBackUsers() {
            String[] names;
            Users[] values;
            @Override
            public void onCallBack(Users value, int iter) {
                userList.add(value);
                if (iter == userList.size()) {
                    names = new String[userList.size()];
                    values = new Users[userList.size()];
                    for (int i = 0; i < userList.size(); i++) {
                        names[i] = userList.get(i).getName();
                        values[i] = userList.get(i);
                    }
                    UserListAdapter userListAdapter = new UserListAdapter(getContext(), names, values);
                    list.setAdapter(userListAdapter);
                }
            }
        });

        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        findButton.setOnClickListener(this);

        list.setOnItemClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.find_button) {
            FireDatabase.findData(Objects.requireNonNull(phone.getText()).toString(), new MyCallBack() {
                @Override
                public void onCallBack(Users value) {
                    String[] names = new String[1];
                    Users[] values = new Users[1];
                    if (value != null) {
                        names[0] = value.getName();
                        values[0] = value;
                        try {
                            UserListAdapter adminSearchAdapter = new UserListAdapter(requireContext(), names, values);
                            list.setAdapter(adminSearchAdapter);
                        } catch (Exception ignored) {
                        }
                    } else {
                        Toast.makeText(getActivity(), R.string.no_one_user, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle idToDialog = new Bundle();
        idToDialog.putString("fragment", "users");
        idToDialog.putString("phone", UserListAdapter.users[i].getPhoneNumber());
        dialog = new DialogAdminSet();
        dialog.setArguments(idToDialog);
        dialog.setTargetFragment(AdminUsersFragment.this, 1);
        dialog.show(getActivity().getSupportFragmentManager(), dialog.getClass().getName());
    }
}
