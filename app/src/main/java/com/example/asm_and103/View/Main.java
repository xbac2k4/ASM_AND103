package com.example.asm_and103.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.asm_and103.Adapter.AdapterDistributor;
import com.example.asm_and103.Model.Distributor;
import com.example.asm_and103.R;
import com.example.asm_and103.View.Fragment.FrgDistributor;
import com.example.asm_and103.View.Fragment.FrgFruit;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class Main extends AppCompatActivity implements AdapterDistributor.DistributorClick {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bottomNavigationView = findViewById(R.id.nav_bottom_bar);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        Fragment fragment = new FrgDistributor();
        replaceFrg(fragment);
        getSupportActionBar().setTitle("Distributor");
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.nav_fruit) {
                    fragment = new FrgFruit();
                    toolbar.setTitle(item.getTitle());
                    replaceFrg(fragment);
                } else if (item.getItemId() == R.id.nav_distributor) {
                    fragment = new FrgDistributor();
                    toolbar.setTitle(item.getTitle());
                    replaceFrg(fragment);
                }
                return false;
            }
        });
    }
    public void replaceFrg(Fragment frg) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_layout, frg).commit();
    }

    @Override
    public void delete(Distributor distributor) {

    }

    @Override
    public void edit(Distributor distributor) {

    }
}