package com.identitymanager.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.identitymanager.R;
import com.identitymanager.database.firestore.queries.FirebaseAccountQuery;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View settView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        //Get the user ID
        Bundle bundle = getActivity().getIntent().getExtras();
        String idUserLoggedIn = bundle.getString("userDocumentId");

        ListView accountsListView = settView.findViewById(R.id.accounts_list);
        ArrayList<String> accounts = new ArrayList<>();

        ArrayAdapter<String> adapterAccounts = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, accounts);
        accountsListView.setAdapter(adapterAccounts);

        FirebaseAccountQuery.listAccountCategory(db, idUserLoggedIn, accounts, adapterAccounts);

        clickButton(settView);
        clickCategory(accountsListView);

        return settView;
    }

    private void clickButton(View settView) {
        FloatingActionButton fab;
        fab = settView.findViewById(R.id.add_button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new NewAccountFragment();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }

    private void clickCategory(ListView accountsListView) {

        accountsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment fragment = new AccountListFragment();

                //Select the category chosen by the user
                String categoryValue = (String) accountsListView.getItemAtPosition(i);

                //Save the category chosen by the user
                Bundle bundleCategory = new Bundle();
                bundleCategory.putString("category", categoryValue);
                fragment.setArguments(bundleCategory);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }
}