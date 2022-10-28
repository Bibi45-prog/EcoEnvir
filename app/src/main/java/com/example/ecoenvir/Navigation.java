package com.example.ecoenvir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navigation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        Fragment defaultFragment = new HomePage();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,defaultFragment).commit();



    }
    private final BottomNavigationView.OnItemSelectedListener navListener = new BottomNavigationView.OnItemSelectedListener()    {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment= null;
            switch (item.getItemId()){
                case R.id.menu_home:
                    selectedFragment= new HomePage();
                    break;
                case R.id.menu_info:
                    selectedFragment= new Info();
                    break;
                case R.id.menu_recycle:
                    selectedFragment= new Recycle();
                    break;
                case R.id.menu_profile:
                    selectedFragment= new Profile();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,selectedFragment).commit();
            return true;
        }

    };
}