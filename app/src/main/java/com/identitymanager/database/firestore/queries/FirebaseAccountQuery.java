package com.identitymanager.database.firestore.queries;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.identitymanager.models.data.Account;
import com.identitymanager.database.firestore.callbacks.GetAccountsCallback;
import com.identitymanager.utilities.LogTags.LogTags;
import com.identitymanager.utilities.security.Cryptography;
import com.identitymanager.utilities.security.PasswordStrength;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public static void createAccount(FirebaseFirestore db, EditText account_name, EditText username, EditText email, EditText password,
                                     PasswordStrength strengthPassword, String authentication, String categorySelected,
                                     String idUserLoggedIn) {

        final String SAVE = "save";
        final String ID_KEY = "id";
        final String NAME_ACCOUNT_KEY = "accountName";
        final String USERNAME_KEY = "username";
        final String EMAIL_KEY = "email";
        final String PASSWORD_KEY = "password";
        final String STRENGTH_KEY = "passwordStrength";
        final String AUTHENTICATION_KEY = "2fa";
        final String CATEGORY_KEY = "category";
        final String TIME_KEY = "lastUpdate";

        String account_name_text = account_name.getText().toString();
        String username_text = username.getText().toString();
        String email_text = email.getText().toString();
        String password_text = password.getText().toString();

        Date last_updated = new Date(System.currentTimeMillis());

        Map<String, Object> account = new HashMap<>();
        account.put(ID_KEY, idUserLoggedIn);
        account.put(NAME_ACCOUNT_KEY, account_name_text);
        account.put(USERNAME_KEY, username_text);
        account.put(EMAIL_KEY, email_text);
        account.put(PASSWORD_KEY, Cryptography.hashString(password_text));
        account.put(STRENGTH_KEY, strengthPassword);
        account.put(AUTHENTICATION_KEY, authentication);
        account.put(CATEGORY_KEY, categorySelected);
        account.put(TIME_KEY, last_updated);

        db.collection("accounts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getData().get(NAME_ACCOUNT_KEY).equals(account_name_text)) {
                                Toast.makeText(account_name.getContext(), "Name account already exists ", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        // Add a new document with a generated ID
                        db.collection("accounts")
                                .add(account)
                                .addOnSuccessListener(documentReference -> {
                                    Log.d(SAVE, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    Toast.makeText(account_name.getContext(), "Account added", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.w(SAVE, "Error adding document", e);
                                    Toast.makeText(account_name.getContext(), "Unable to create user", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Log.w(SAVE, "Error getting documents", task.getException());
                    }
                });

    }

    public static List<Account> getAccountsByUserId(FirebaseFirestore db, String userId) {

        List<Account> accounts = new ArrayList<>();

        DocumentReference userDocRef = db.collection("users").document(userId);

        db.collection(ACCOUNTS_COLLECTION_PATH)
                .whereEqualTo("user", userDocRef)
                .get()
                .addOnCompleteListener(task ->  {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //accounts.add(document.toObject(Account.class));
                                Log.d("QUERY OK", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("QUERY", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return accounts;
    }
}
