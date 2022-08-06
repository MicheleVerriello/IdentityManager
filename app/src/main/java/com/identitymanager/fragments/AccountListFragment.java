package com.identitymanager.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.identitymanager.R;
import com.identitymanager.adapters.RecyclerAdapter;
import com.identitymanager.models.data.Account;

import java.util.ArrayList;

public class AccountListFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<Account> list;
    FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settView = inflater.inflate(R.layout.activity_account_list_fragment, container, false);

        //Get the user ID
        Bundle bundle = getActivity().getIntent().getExtras();
        String idUserLoggedIn = bundle.getString("userDocumentId");

        //Get the category chosen by the user
        String categoryValue = getArguments().getString("category");

        recyclerView = settView.findViewById(R.id.accounts_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();
        list = new ArrayList<Account>();
        recyclerAdapter = new RecyclerAdapter(getContext(), list);
        recyclerView.setAdapter(recyclerAdapter);

        EventChangeListener(idUserLoggedIn, categoryValue);

        return settView;
    }

    private void EventChangeListener(String idUserLoggedIn, String categoryValue) {

        db.collection("accounts") //get all account details
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Account account = document.toObject(Account.class);
                                if ((account.getFkIdUser().equals(idUserLoggedIn)) && (account.getcategory().equals(categoryValue))) {
                                    list.add(account);
                                    recyclerAdapter.notifyDataSetChanged();
                                }
                                Log.d("QUERY OK", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("QUERY", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}