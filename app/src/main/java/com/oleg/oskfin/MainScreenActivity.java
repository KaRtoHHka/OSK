package com.oleg.oskfin;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.oleg.oskfin.data.FireDatabase;
import com.oleg.oskfin.data.MyCallBack;
import com.oleg.oskfin.data.Users;

public class MainScreenActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private MenuItem mMenuAdminItem;
    private MenuItem mMenuSenderItem;
    private static DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        Menu menu = navigationView.getMenu();
        mMenuAdminItem = menu.findItem(R.id.nav_admin);
        mMenuSenderItem = menu.findItem(R.id.nav_sender);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_sender, R.id.nav_store, R.id.nav_profile, R.id.nav_admin, R.id.nav_exit)
                .setDrawerLayout(drawer)
                .build();
        FireDatabase.getData(new MyCallBack() {
            @Override
            public void onCallBack(Users value) {
                setMenuItemInvisible(value);
            }
        });
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sync, menu);
        return true;
    }

    private void setMenuItemInvisible(Users value) {
        mMenuAdminItem.setVisible(value.isAdmin());
        mMenuSenderItem.setVisible(value.isSender());
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            recreate();
        }
        return true;
    }

    public static void mOpenDrawer(){
        drawer.openDrawer(GravityCompat.START);
    }
}
