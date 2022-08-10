package com.identitymanager.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.identitymanager.R;
import com.identitymanager.utilities.security.AES;

public class AccountDetailsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settView = inflater.inflate(R.layout.fragment_account_details, container, false);

        String id, accountNameDetails, categoryDetails, emailDetails, usernameDetails, passwordDetails, passwordStrengthDetails;

        TextView title, accountName, category, email, username, password, passwordStrength;
        Button modify;

        title = settView.findViewById(R.id.account_details_title);
        accountName = settView.findViewById(R.id.account_name_details);
        email = settView.findViewById(R.id.account_email_details);
        category = settView.findViewById(R.id.account_category_details);
        username = settView.findViewById(R.id.account_username_details);
        password = settView.findViewById(R.id.account_password_details);
        passwordStrength = settView.findViewById(R.id.account_password_strength_details);
        modify = settView.findViewById(R.id.button_modify);

        Bundle bundle = getActivity().getIntent().getExtras();
        id =  bundle.getString("id");
        accountNameDetails = bundle.getString("accountName");
        categoryDetails = bundle.getString("email");
        emailDetails = bundle.getString("category");
        usernameDetails = bundle.getString("username");
        passwordDetails = bundle.getString("password");
        passwordStrengthDetails = bundle.getString("passwordStrength");

        accountName.setText(accountNameDetails);
        category.setText(categoryDetails);
        email.setText(emailDetails);
        username.setText(usernameDetails);
        password.setText(AES.decrypt(passwordDetails, id));
        passwordStrength.setText(passwordStrengthDetails);

        deleteAccount(settView, accountNameDetails, getContext());

        return settView;
    }

    public void deleteAccount(View settView, String accountNameDetails, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Button delete = settView.findViewById(R.id.button_delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new DashboardFragment();
                deleteAccount(db, fragment, accountNameDetails);

            }
        });
    }

    public void deleteAccount(FirebaseFirestore db, Fragment fragment, String accountNameDetails) {

        db.collection("accounts")
                .whereEqualTo("accountName", accountNameDetails)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("accounts").document(document.getId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("DELETE OK", "DocumentSnapshot successfully deleted!");
                                            Toast.makeText(getContext(), "Account deleted", Toast.LENGTH_SHORT).show();
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("DELETE FAILURE", "Error deleting document");
                                            Toast.makeText(getActivity(), "Unable to delete account", Toast.LENGTH_SHORT).show();
                                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                        }
                                    });
                        }
                    }
                });
    }
}