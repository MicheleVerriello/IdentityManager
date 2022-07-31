package com.identitymanager.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.identitymanager.R;
import com.identitymanager.database.firestore.callbacks.GetAccountsCallback;
import com.identitymanager.models.Account;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View settView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        ListView accountsListView = settView.findViewById(R.id.accounts_list);

        List<Account> accounts = new ArrayList<>();
        List<String> accountsString = new ArrayList<>();
        Bundle bundle = getActivity().getIntent().getExtras();
        String idUserLoggedIn = bundle.getString("userDocumentId");
        DocumentReference userDocRef = db.collection("users").document(idUserLoggedIn);


        Log.d("INFO", "Getting documents");
        db.collection("accounts")
                .whereEqualTo("user", userDocRef)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                accounts.add(document.toObject(Account.class));
                                Log.d("QUERY OK", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("QUERY", "Error getting documents: ", task.getException());
                        }
                    }
                });

        for (Account account: accounts) {
            accountsString.add(account.getAccountName());
        }

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                accountsString
        );

        accountsListView.setAdapter(listViewAdapter);

        clickbutton(settView);

        return settView;
    }

    private void clickbutton(View settView) {
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


}
