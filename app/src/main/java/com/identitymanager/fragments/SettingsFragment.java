package com.identitymanager.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.identitymanager.activities.ExpandableListDataPump;
import com.identitymanager.activities.CustomExpandableListAdapter;
import com.identitymanager.activities.MainActivity;
import com.identitymanager.utilities.language.LanguageManager;
import com.identitymanager.R;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SettingsFragment extends Fragment {

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorRestart;
    SharedPreferences.Editor editorMode;

    // New Trusted
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText editTextNickname_newTrusted, editTextEmail_newTrusted;
    private Button buttonAdd_newTrusted, buttonCancel_newTrusted;


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
        trust(settView);
        setup_newTrusted(settView);

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
            ((MainActivity) getActivity()).setChangeLanguageItalian();
        }

        saveLanguagePreferences(sP, count, enRb, itRb, lang);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio_button1) {
                    lang.updateResources("en");
                    editor.putInt("sP", 1);
                } else {
                    lang.updateResources("it");
                    editor.putInt("sP", 2);
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
        int check = sharedMode.getInt("check", 0);
        editorMode = sharedMode.edit();

        SwitchCompat aSwitch = settView.findViewById(R.id.s1);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES || check == 2) {
            aSwitch.setChecked(true);
            ((MainActivity) getActivity()).darkModeActionBar();
            ((MainActivity) getActivity()).setDarkTheme();
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (aSwitch.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    ((MainActivity) getActivity()).setDarkTheme();
                    editorMode.putInt("check", 2);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    ((MainActivity) getActivity()).setLightTheme();
                    editorMode.putInt("check", 1);
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




    public void trust(View settView){

        ExpandableListView expandableListView;
        ExpandableListAdapter expandableListAdapter;
        List<String> expandableListTitle;
        HashMap<String, List<String>> expandableListDetail;


        expandableListView = (ExpandableListView) settView.findViewById(R.id.youTrust);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }

    public void createNewTrustedDialog(){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View newTrusted_popupView = getLayoutInflater().inflate(R.layout.new_trusted_popup, null);
        editTextNickname_newTrusted = (EditText) newTrusted_popupView.findViewById(R.id.editTextNickname_newTrusted);
        editTextEmail_newTrusted = (EditText) newTrusted_popupView.findViewById(R.id.editTextEmail_newTrusted);

        buttonAdd_newTrusted = (Button) newTrusted_popupView.findViewById(R.id.buttonAdd_newTrusted);
        buttonCancel_newTrusted = (Button) newTrusted_popupView.findViewById(R.id.buttonCancel_newTrusted);

        dialogBuilder.setView(newTrusted_popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        buttonAdd_newTrusted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //salva su database
            }
        });

        buttonCancel_newTrusted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //salva su database
            }
        });


    }

    public void setup_newTrusted(View settView){

        Button newTrusted = settView.findViewById(R.id.button_newTrusted);

        newTrusted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewTrustedDialog();
            }
        });

    }
}