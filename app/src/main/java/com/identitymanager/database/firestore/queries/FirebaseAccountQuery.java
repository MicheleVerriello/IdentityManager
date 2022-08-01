package com.identitymanager.database.firestore.queries;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.identitymanager.models.data.Account;
import com.identitymanager.database.firestore.callbacks.GetAccountsCallback;
import com.identitymanager.utilities.LogTags.LogTags;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FirebaseAccountQuery {

    static final String ACCOUNTS_COLLECTION_PATH = "accounts";

    public static void getAccountsByUserId(String userId, FirebaseFirestore db, GetAccountsCallback getAccountsCallback) {

        List<Account> accountList = new ArrayList<>();

        db.collection(ACCOUNTS_COLLECTION_PATH) //get all the accounts
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

    public static Account getAccountById(String accountId, FirebaseFirestore db) {

        AtomicReference<Account> account = new AtomicReference<>(new Account());

        DocumentReference docRef = db.collection(ACCOUNTS_COLLECTION_PATH).document(accountId);
        docRef.get().addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        account.set(document.toObject(Account.class));
                        Log.d(LogTags.QUERY, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(LogTags.QUERY, "No such document");
                    }
                } else {
                    Log.d(LogTags.QUERY, "get failed with ", task.getException());
                }
        });

        return account.get();
    }
}
