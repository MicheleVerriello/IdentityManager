package com.identitymanager.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.identitymanager.R;
import com.identitymanager.fragments.DashboardFragment;
import com.identitymanager.fragments.StatisticsFragment;
import com.identitymanager.fragments.SettingsFragment;
import com.identitymanager.fragments.UserDetailsViewFragment;
import com.identitymanager.services.NotificationService;
import com.identitymanager.utilities.language.LanguageManager;
import com.identitymanager.workers.NotificationWorker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    SharedPreferences.Editor editorLanguage;
    SharedPreferences.Editor editorTheme;
    String idUserLoggedIn;
    private final String CHANNEL_ID = "identityManagerNotification";
    private final String NOTIFICATION_CONTENT_TITLE = "Password need a change";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        startNotificationServiceViaWorker();

        Bundle bundle = getIntent().getExtras();
        int idFragment = bundle.getInt("fragment");
        idUserLoggedIn = bundle.getString("userDocumentId");

        int idLoad = bundle.getInt("load", 0);

        SharedPreferences sharedLanguage = getSharedPreferences("language", 0);
        int refresh = sharedLanguage.getInt("refresh", 0);
        Log.d("refresh", "refresh " + refresh);
        editorLanguage = sharedLanguage.edit();

        SharedPreferences sharedTheme = getSharedPreferences("mode", 0);
        int theme = sharedTheme.getInt("theme", 0);
        editorTheme = sharedTheme.edit();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);
        Fragment fragment = null;
        View view = null;

        switch (idFragment) {
            case 2:
                fragment = new StatisticsFragment();
                view = bottomNav.findViewById(R.id.nav_newAccount);
                view.performClick();
                break;
            case 3:
                fragment = new UserDetailsViewFragment();
                view = bottomNav.findViewById(R.id.nav_user_details);
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
                else if (idLoad == 1) {
                    identifyLanguagePreference(refresh);
                    getIntent().putExtra("load", 2);
                    recreate();
                }

                break;
        }

        if (idFragment != 4) {
            getIntent().putExtra("userDocumentId", idUserLoggedIn);
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
                     getIntent().putExtra("textCheck", 1);
                     break;
                 case R.id.nav_newAccount:
                     selectedFragment = new StatisticsFragment();
                     break;
                 case R.id.nav_user_details:
                     selectedFragment = new UserDetailsViewFragment();
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
       Log.d("theme", "theme: " + theme);
        if (theme == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (theme == 2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.DarkTheme);
        }
    }

    public void startService() {
        if (!NotificationService.isServiceRunning) {
            Intent notificationServiceIntent = new Intent(this, NotificationService.class);
            ContextCompat.startForegroundService(this, notificationServiceIntent);
        }
    }

    public void stopService() {
        if (NotificationService.isServiceRunning) {
            Intent serviceIntent = new Intent(this, NotificationService.class);
            stopService(serviceIntent);
        }
    }

    public void startNotificationServiceViaWorker() {
        String UNIQUE_WORK_NAME = "StartNotificationServiceViaWorker";
        WorkManager workManager = WorkManager.getInstance();

        // As per Documentation: The minimum repeat interval that can be defined is 15 minutes
        // (same as the JobScheduler API), but in practice 15 doesn't work. Using 16 here
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(NotificationWorker.class,1, TimeUnit.DAYS).build();

        // to schedule a unique work, no matter how many times app is opened i.e. startServiceViaWorker gets called
        // do check for AutoStart permission
        workManager.enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request);
    }
}