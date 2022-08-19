package com.identitymanager.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.identitymanager.activities.MainActivity;
import com.identitymanager.utilities.language.LanguageManager;
import com.identitymanager.R;

public class SettingsFragment extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorRestart;
    SharedPreferences.Editor editorMode;

    private final Integer REQUEST_ENABLE_BT = 1;

    // New Trusted

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settView = inflater.inflate(R.layout.fragment_settings, container, false);

        changeLanguage(settView);
        darkMode(settView);

        return settView;
    }

    public void changeLanguage(View settView) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("choose", 0);
        int sP = sharedPreferences.getInt("sP", 1);
        editor = sharedPreferences.edit();

        SharedPreferences sharedRestart = getActivity().getSharedPreferences("choose", 0);
        int count = sharedRestart.getInt("count", 0);
        editorRestart = sharedRestart.edit();

        RadioGroup radioGroup = settView.findViewById(R.id.radio_group);
        RadioButton enRb = settView.findViewById(R.id.radio_button1);
        RadioButton itRb = settView.findViewById(R.id.radio_button2);
        LanguageManager lang = new LanguageManager(getContext());

        if (count == 0) {
            enRb.setChecked(true);
        }

        saveLanguagePreferences(sP, count, enRb, itRb, lang);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio_button1) {
                    lang.updateResources("en");
                    editor.putInt("sP", 1);
                    ((MainActivity) getActivity()).setChangeLanguageEnglish();
                } else {
                    lang.updateResources("it");
                    editor.putInt("sP", 2);
                    ((MainActivity) getActivity()).setChangeLanguageItalian();
                }
                editor.commit();

                editorRestart.putInt("count", 1);
                editorRestart.commit();

                remainInSettings();
            }
        });
    }

    public void saveLanguagePreferences(int sP, int count, RadioButton enRb, RadioButton itRb, LanguageManager lang) {
        if (sP == 1 && count == 1) {
            enRb.setChecked(true);
            lang.updateResources("en");
            ((MainActivity) getActivity()).setChangeLanguageEnglish();

            editorRestart.putInt("count", 1);
            editorRestart.commit();
        } else if (sP == 2 && count == 1) {
            itRb.setChecked(true);
            lang.updateResources("it");
            ((MainActivity) getActivity()).setChangeLanguageItalian();

            editorRestart.putInt("count", 1);
            editorRestart.commit();
        }
    }

    public void darkMode(View settView) {
        SharedPreferences sharedMode = getActivity().getSharedPreferences("mode", 0);
        editorMode = sharedMode.edit();

        int check = sharedMode.getInt("theme", 0);

        SwitchCompat aSwitch = settView.findViewById(R.id.s1);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES || check == 2) {
            aSwitch.setChecked(true);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (aSwitch.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editorMode.putInt("theme", 2).apply();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editorMode.putInt("theme", 1).apply();
                }
                editorMode.commit();

                remainInSettingsCoordinationLanguage();
            }
        });
    }

    public void remainInSettings() {
        getActivity().getIntent().putExtra("fragment", 4);
        getActivity().getIntent().putExtra("change_value", 4);
        getActivity().recreate();
    }

    public void remainInSettingsCoordinationLanguage() {
        getActivity().getIntent().putExtra("fragment", 4);
        getActivity().getIntent().putExtra("change_value", 0);
        getActivity().recreate();
    }

//    public void createNewTrustedDialog(View view){
//
//        dialogBuilder = new AlertDialog.Builder(getActivity());
//        final View newTrusted_popupView = getLayoutInflater().inflate(R.layout.new_trusted_popup, null);
//        editTextNickname_newTrusted = newTrusted_popupView.findViewById(R.id.editTextNickname_newTrusted);
//        editTextEmail_newTrusted = newTrusted_popupView.findViewById(R.id.editTextEmail_newTrusted);
//
//        buttonAdd_newTrusted = newTrusted_popupView.findViewById(R.id.buttonAdd_newTrusted);
//        buttonCancel_newTrusted =  newTrusted_popupView.findViewById(R.id.buttonCancel_newTrusted);
//
//        dialogBuilder.setView(newTrusted_popupView);
//        dialog = dialogBuilder.create();
//        dialog.show();
//
//        // activate bluetooth if not active
//        bluetoothManager = getActivity().getSystemService(BluetoothManager.class);
//
//        System.out.println(bluetoothManager.toString());
//
//        bluetoothAdapter = bluetoothManager.getAdapter();
//
//        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }
//
//        // searching for nearby devices
//
//        // click on the device to connect
//
//        // send a trust request to connected device
//
//        // wait for a response
//
//        // if response is OK save the trust into db
//    }
}