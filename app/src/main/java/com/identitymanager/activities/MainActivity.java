package com.identitymanager.activities;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.identitymanager.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        int idFragment = bundle.getInt("fragment");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Fragment fragment = null;

        switch (idFragment) {
            case 2:
                fragment = new NewAccountActivity();
                break;
            case 3:
                fragment = new ProfileActivity();
                break;
            case 4:
                fragment = new SettingsActivity();
                break;
            default:
                fragment = new DashboardActivity();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                   Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_dashboard:
                            selectedFragment = new DashboardActivity();
                            break;
                        case R.id.nav_newAccount:
                            selectedFragment = new NewAccountActivity();
                            break;
                        case R.id.nav_profile:
                            selectedFragment = new ProfileActivity();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new SettingsActivity();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };
    }
