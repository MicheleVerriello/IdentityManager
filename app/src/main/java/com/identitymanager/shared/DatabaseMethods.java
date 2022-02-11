package com.identitymanager.shared;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.identitymanager.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseMethods {

    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    /**
     * This method is used to get users on login and check if there is the wanted user
     * If username and password are correct the method will return the user else it will return user with null fields
     *
     * @param firebaseFirestore is the database instance
     * @return the user logged in, else user with null fields
     */
    public static User getUserForLogin(FirebaseFirestore firebaseFirestore, String usernameFromLogin, String hashedPasswordFromLogin) {

        User user = new User();

        firebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String usernameFromDb = (String) document.getData().get(USERNAME_KEY);
                            String hashedPasswordFromDb = (String) document.getData().get(PASSWORD_KEY); //the hashed password that is stored into db

                            /*
                             * Check if the usernames are equals and the hashedPasswords are the same
                             * If true set the user fields else the user fields will be null
                             */
                            if (usernameFromDb.equals(usernameFromLogin) && hashedPasswordFromDb.equals(hashedPasswordFromLogin)) {

                            }

                        }
                    } else {
                        Log.w("getUserForLogin()", "Error getting documents.", task.getException());
                    }
                });

        System.out.println("user.getId() " + user.getId());
        System.out.println("document.getUsername() " + user.getUsername());

        return user;
    }

    /**
     * Method used in the signUp Activity, allow to add a new user into the users collection
     * It checks also some parameters and return different values based on the realtime situation
     *
     * @param firebaseFirestore   the database instance
     * @param usernameToAdd       the username of the new user
     * @param hashedPasswordToAdd the hashed password of the new user
     * @return 0 if it can' t get the document, 1 if the user already exists, 2 if it can't add the document, 3 if is all OK
     */
    public static int insertUserOnSignUp(FirebaseFirestore firebaseFirestore, String usernameToAdd, String hashedPasswordToAdd) {

        System.out.println("HASHED PASSWORD TO INSERT: " + hashedPasswordToAdd);
        // userAdded possible values: see the method documentation on @return
        AtomicInteger userAdded = new AtomicInteger(0);

        Map<String, Object> user = new HashMap<>();
        user.put(USERNAME_KEY, usernameToAdd);
        user.put(PASSWORD_KEY, hashedPasswordToAdd);

        firebaseFirestore.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getData().get(USERNAME_KEY).equals(usernameToAdd)) {
                                userAdded.set(1);
                            }
                        }

                        if (userAdded.get() == 0) {
                            // Add a new document with a generated ID
                            firebaseFirestore.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.d("insertUserOnSignUp", "DocumentSnapshot added with ID: " + documentReference.getId());
                                        userAdded.set(3);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w("insertUserOnSignUp", "Error adding document", e);
                                        userAdded.set(2);
                                    });
                        }
                    } else {
                        Log.w("insertUserOnSignUp", "Error getting documents.", task.getException());
                    }
                });

        System.out.println("userAdded: " + userAdded.get());

        return userAdded.get();
    }
}
