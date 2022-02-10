package com.identitymanager.activities;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.identitymanager.R;
import com.identitymanager.fragments.DashboardFragment;
import com.identitymanager.fragments.NewAccountFragment;
import com.identitymanager.fragments.ProfileFragment;
import com.identitymanager.fragments.SettingsFragment;
import com.identitymanager.shared.LanguageManager;

public class MainActivity extends AppCompatActivity {

    SharedPreferences.Editor editorLanguage;
    SharedPreferences.Editor editorTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        int idFragment = bundle.getInt("fragment");
        int idLoad = bundle.getInt("load", 0);

        SharedPreferences sharedLanguage = getSharedPreferences("language", 0);
        int refresh = sharedLanguage.getInt("refresh", 0);
        editorLanguage = sharedLanguage.edit();

        SharedPreferences sharedTheme = getSharedPreferences("color", 0);
        int theme = sharedTheme.getInt("theme", 0);
        editorTheme = sharedTheme.edit();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);
        Fragment fragment = null;
        View view = null;

        switch (idFragment) {
            case 2:
                fragment = new NewAccountFragment();
                view = bottomNav.findViewById(R.id.nav_newAccount);
                view.performClick();
                break;
            case 3:
                fragment = new ProfileFragment();
                view = bottomNav.findViewById(R.id.nav_profile);
                view.performClick();
                break;
            case 4:
                view = bottomNav.findViewById(R.id.nav_settings);
                view.performClick();
                break;
            default:
                fragment = new DashboardFragment();
                view = bottomNav.findViewById(R.id.nav_dashboard);
                view.performClick();

                if (idLoad == 0) {
                    identifyModePreference(theme);
                    getIntent().putExtra("load", 1);
                    recreate();
                }
                if (theme == 2) {
                    darkModeActionBar();
                }
                if (idLoad == 1) {
                    identifyLanguagePreference(refresh);
                    getIntent().putExtra("load", 2);
                    recreate();
                }
                break;
        }

        if (idFragment != 4) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }

    private  NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Bundle bundle = getIntent().getExtras();
            int idChange = bundle.getInt("change_value");
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                 case R.id.nav_dashboard:
                     selectedFragment = new DashboardFragment();
                     break;
                 case R.id.nav_newAccount:
                     selectedFragment = new NewAccountFragment();
                     break;
                 case R.id.nav_profile:
                     selectedFragment = new ProfileFragment();
                     break;
                 case R.id.nav_settings:
                     selectedFragment = new SettingsFragment();
                     break;
            }

            if (idChange != 4) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }

            restoreChangeValue();

            return true;
        }
    };

   @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        theme.applyStyle(R.style.DarkTheme, true);
        return theme;
    }

    public void darkModeActionBar() {
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.purple_500)));
    }

    public void restoreChangeValue() {
        getIntent().putExtra("change_value", 0);
    }

    public void identifyLanguagePreference(int refresh) {
        if (refresh == 1) {
            LanguageManager lang = new LanguageManager(getBaseContext());
            lang.updateResources("en");
        } else if (refresh == 2) {
            LanguageManager lang = new LanguageManager(getBaseContext());
            lang.updateResources("it");
        }
    }

    public void setChangeLanguageEnglish() {
        editorLanguage.putInt("refresh", 1);
        editorLanguage.commit();
    }

    public void setChangeLanguageItalian() {
        editorLanguage.putInt("refresh", 2);
        editorLanguage.commit();
    }

    public void identifyModePreference(int theme) {
        if (theme == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (theme == 2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.DarkTheme);
        }
    }

    public void setLightTheme() {
        editorTheme.putInt("theme", 1);
        editorTheme.commit();
    }

    public void setDarkTheme() {
        editorTheme.putInt("theme", 2);
        editorTheme.commit();
    }
}