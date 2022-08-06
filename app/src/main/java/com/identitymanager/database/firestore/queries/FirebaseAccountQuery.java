package com.identitymanager.database.firestore.queries;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.identitymanager.models.data.Account;
import com.identitymanager.database.firestore.callbacks.GetAccountsCallback;
import com.identitymanager.utilities.LogTags.LogTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class FirebaseAccountQuery extends Fragment {

    static final String ACCOUNTS_COLLECTION_PATH = "accounts";
    static Account account;

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

    public static void createAccount(FirebaseFirestore db, Map<String, Object> accountToInsert, Context context) {


        db.collection("accounts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getData().get("accountName").equals(accountToInsert.get("accountName"))) {
                                Toast.makeText(context, "Name account already exists ", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        // Add a new document with a generated ID
                        db.collection("accounts")
                                .add(accountToInsert)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d("SAVE", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Toast.makeText(context, "Account added", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.w("SAVE", "Error adding document", e);
                                    Toast.makeText(context, "Unable to create user", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Log.w("SAVE", "Error getting documents", task.getException());
                    }
                });

    }

    public static void listAccountCategory(FirebaseFirestore db, String idUserLoggedIn, ArrayList<String> accounts, ArrayAdapter<String> adapterAccounts) {

        ArrayList<String> categoryCopy = new ArrayList<>();

        db.collection(ACCOUNTS_COLLECTION_PATH) //get all the category of an ID
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            account = document.toObject(Account.class);
                            if (account.getFkIdUser().equals(idUserLoggedIn) && (!categoryCopy.contains(account.getcategory()))) {
                                accounts.add(account.getcategory());
                                categoryCopy.add(account.getcategory());
                                adapterAccounts.notifyDataSetChanged();
                            }
                            Log.d("QUERY OK", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("QUERY", "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    public static List<Account> getAccountsByUserId(FirebaseFirestore db, String userId) {

        List<Account> accounts = new ArrayList<>();


        db.collection(ACCOUNTS_COLLECTION_PATH)
            .whereEqualTo("fkIdUser", userId)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        accounts.add(document.toObject(Account.class));
                    }
                }
            });

        return accounts;
    }
}


