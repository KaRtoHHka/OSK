package com.oleg.oskfin.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.oleg.oskfin.MainScreenActivity;
import com.oleg.oskfin.R;
import com.oleg.oskfin.ui.ViewPagerAdapter;

public class AdminFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin, container, false);

        viewPager = root.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = root.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        return root;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new AdminUsersFragment(), getString(R.string.user));
        adapter.addFragment(new AdminItemsFragment(), getString(R.string.items));
        adapter.addFragment(new AdminPointsFragment(), getString(R.string.points));
        viewPager.setAdapter(adapter);
    }

}
