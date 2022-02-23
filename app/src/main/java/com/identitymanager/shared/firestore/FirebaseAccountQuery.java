package com.identitymanager.shared.firestore;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.identitymanager.models.Account;
import com.identitymanager.shared.interfaces.callbacks.GetAccountsCallback;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAccountQuery {

    public static void getAccountsByUserId(String userId, FirebaseFirestore db, GetAccountsCallback getAccountsCallback) {

        List<Account> accountList = new ArrayList<>();

        db.collection("accounts") //get all the accounts
        .get()
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getString("fkIdUser").equals(userId)) {
                            accountList.add(document.toObject(Account.class));
                        }
                    }
                } else {
                    Log.w("getAccountsByUserId", "Error getting documents.", task.getException());
                }
            }
        });
        getAccountsCallback.onCallback(accountList); //callback to wait for the data in the fragment that calls the method
    }
}
