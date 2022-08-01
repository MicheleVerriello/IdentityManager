package com.identitymanager.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.identitymanager.R;
import com.identitymanager.models.data.User;

public class UserDetailsViewFragment extends Fragment {

    private static String USER_DOCUMENT_ID = "users";
    private String userDocumentId;
    private User user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View settView = inflater.inflate(R.layout.fragment_user_details_view, container, false);

        // Getting the userDocumentId
        Bundle bundle = getActivity().getIntent().getExtras();
        userDocumentId = bundle.getString("userDocumentId");

        // Getting the user details by userDocumentId
        DocumentReference docRef = db.collection(USER_DOCUMENT_ID).document(userDocumentId);
        docRef.get().addOnCompleteListener( task ->  {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    user = document.toObject(User.class);

                    // Setting the view
                    TextView nameTextView = settView.findViewById(R.id.name_text_view);
                    nameTextView.setText(user.getName());
                    TextView surnameTextView = settView.findViewById(R.id.surname_text_view);
                    surnameTextView.setText(user.getSurname());
                    TextView usernameTextView = settView.findViewById(R.id.username_text_view);
                    usernameTextView.setText(user.getUsername());
                    TextView emailTextView = settView.findViewById(R.id.email_text_view);
                    emailTextView.setText(user.getEmail());
                    TextView phoneTextView = settView.findViewById(R.id.phone_text_view);
                    phoneTextView.setText(user.getPhone());
                    TextView countryTextView = settView.findViewById(R.id.country_text_view);
                    countryTextView.setText(user.getCountry());
                    TextView birthdayTextView = settView.findViewById(R.id.birthday_text_view);
                    birthdayTextView.setText(user.getBirthDate() != null ?user.getBirthDate().toString() : "");

                } else {
                    Log.d("GET_USER_DETAILS", "No such document");
                }
            } else {
                Log.d("GET_USER_DETAILS", "get failed with ", task.getException());
            }
        });

        goToFragmenUserDetailsEdit(settView);

        return settView;
    }

    public void goToFragmenUserDetailsEdit(View view) {
        Button editButton = view.findViewById(R.id.button_edit);

        editButton.setOnClickListener(v -> {
            Fragment fragment = new UserDetailsEditFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        });
    }
}