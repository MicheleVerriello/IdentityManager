package com.identitymanager.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.identitymanager.R;
import com.identitymanager.models.User;
import java.util.HashMap;
import java.util.Map;

public class UserDetailsEditFragment extends Fragment {

    public static final String USERNAME_KEY = "username";
    public static final String BIRTH_DATE_KEY = "birthDate";
    public static final String EMAIL_KEY = "email";
    public static final String PHONE_KEY = "phone";
    public static final String COUNTRY_KEY = "country";
    public static final String NAME_KEY = "name";
    public static final String SURNAME_KEY = "surname";

    private static final String UPDATE_USER = "UPDATE_USER";
    private static String USER_DOCUMENT_ID_KEY = "users";
    private String userDocumentId;
    private User user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View settView = inflater.inflate(R.layout.fragment_user_details_edit, container, false);

        // Getting the userDocumentId
        Bundle bundle = getActivity().getIntent().getExtras();
        userDocumentId = bundle.getString("userDocumentId");

        // Setting the methods
        saveUserDetailsChanges(settView);

        // Setting the view
        EditText nameEditText = settView.findViewById(R.id.editTextName);
        EditText surnameEditText = settView.findViewById(R.id.editTextSurname);
        EditText usernameEditText = settView.findViewById(R.id.editTextUsername);
        EditText emailEditText = settView.findViewById(R.id.editTextEmail);
        EditText phoneEditText = settView.findViewById(R.id.editTextPhone);
        EditText countryEditText = settView.findViewById(R.id.editTextCountry);
        EditText birthdayEditText = settView.findViewById(R.id.editTextBirthday);
        // Getting the user details by userDocumentId
        DocumentReference docRef = db.collection(USER_DOCUMENT_ID_KEY).document(userDocumentId);
        docRef.get().addOnCompleteListener( task ->  {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    user = document.toObject(User.class);
                    if(user != null) {
                        if (user.getSurname() != null && !user.getSurname().isEmpty()) {
                            surnameEditText.setText(user.getSurname());
                        }
                        if (user.getName() != null && !user.getName().isEmpty()) {
                            nameEditText.setText(user.getName());
                        }
                        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                            emailEditText.setText(user.getEmail());
                        }
                        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
                            usernameEditText.setText(user.getUsername());
                        }
                        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                            phoneEditText.setText(user.getPhone());
                        }
                        if (user.getCountry() != null && !user.getCountry().isEmpty()) {
                            countryEditText.setText(user.getCountry());
                        }
                        if (user.getBirthDate() != null) {
                            birthdayEditText.setText(user.getBirthDate() != null ? user.getBirthDate().toString() : "");
                        }
                    }
                } else {
                    Log.d("GET_USER_DETAILS", "No such document");
                }
            } else {
                Log.d("GET_USER_DETAILS", "get failed with ", task.getException());
            }
        });

        return settView;
    }

    public void saveUserDetailsChanges(View view) {
        EditText nameEditText = view.findViewById(R.id.editTextName);
        EditText surnameEditText = view.findViewById(R.id.editTextSurname);
        EditText usernameEditText = view.findViewById(R.id.editTextUsername);
        EditText emailEditText = view.findViewById(R.id.editTextEmail);
        EditText phoneEditText = view.findViewById(R.id.editTextPhone);
        EditText countryEditText = view.findViewById(R.id.editTextCountry);
        EditText birthdayEditText = view.findViewById(R.id.editTextBirthday);

        Editable nameEditTextValue = nameEditText.getText();
        Editable surnameEditTextValue = surnameEditText.getText();
        Editable usernameEditTextValue = usernameEditText.getText();
        Editable emailEditTextValue = emailEditText.getText();
        Editable phoneEditTextValue = phoneEditText.getText();
        Editable countryEditTextValue = countryEditText.getText();
        Editable birthdayEditTextValue = birthdayEditText.getText();

        //Creating the object
        Map<String, Object> userUpdate = new HashMap<>();
        userUpdate.put(USERNAME_KEY, usernameEditTextValue.toString());
        userUpdate.put(EMAIL_KEY, emailEditTextValue.toString());
        userUpdate.put(SURNAME_KEY, surnameEditTextValue.toString());
        userUpdate.put(NAME_KEY, nameEditTextValue.toString());
        userUpdate.put(COUNTRY_KEY, countryEditTextValue.toString());
        userUpdate.put(PHONE_KEY, phoneEditTextValue.toString());
        userUpdate.put(BIRTH_DATE_KEY, birthdayEditTextValue.toString());

        // Add a new document with a generated ID
        db.collection("users").document(userDocumentId)
                .update(userUpdate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(UPDATE_USER, "user Profile is updated for " + userDocumentId);
                        Toast.makeText(getActivity(), "User detail updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(UPDATE_USER, "onFailure: " + e);
                    }
                });


        //return to the userDetailsViewFragment
        Button saveChangesUserDetailsButton = view.findViewById(R.id.button_save_change_user_details);

        saveChangesUserDetailsButton.setOnClickListener(v -> {
            Fragment fragment = new UserDetailsViewFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        });
    }
}