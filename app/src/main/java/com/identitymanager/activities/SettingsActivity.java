package com.identitymanager.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.identitymanager.R;

public class SettingsActivity extends Fragment {

    private SwitchCompat aSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settFrag = inflater.inflate(R.layout.activity_settings,container,false);

        darkMode(settFrag);

        return settFrag;
    }

    public void darkMode(View settFrag) {

        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES) {
            getActivity().setTheme(R.style.DarkTheme);
            ((MainActivity) getActivity()).darkModeColours();
        } else {
            getActivity().setTheme(R.style.Theme_IdentityManager);
        }

        aSwitch = settFrag.findViewById(R.id.s1);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            aSwitch.setChecked(true);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (aSwitch.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

                remainInSettings();
            }
        });
    }

    public void remainInSettings() {
        getActivity().getIntent().putExtra("fragment", 4);
        getActivity().recreate();
    }
}