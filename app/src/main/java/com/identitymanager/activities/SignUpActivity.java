package com.identitymanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.identitymanager.R;
import com.identitymanager.fragments.UserDetailsFragment;
import com.identitymanager.shared.SecurityMethods;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String SIGN_UP = "sign_up"; //for logs

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void signup(View view) {

        EditText sign_up_username_value = (EditText) findViewById(R.id.sign_up_username_value);
        EditText sign_up_password_value = (EditText) findViewById(R.id.sign_up_password_value);
        EditText sign_up_confirm_password_value = (EditText) findViewById(R.id.sign_up_confirm_password_value);
        String sign_up_username_value_text = sign_up_username_value.getText().toString();
        String sign_up_password_value_text = sign_up_password_value.getText().toString();
        String sign_up_confirm_password_value_text = sign_up_confirm_password_value.getText().toString();


        if (!sign_up_username_value_text.isEmpty() && !sign_up_password_value_text.isEmpty() && sign_up_password_value_text.equals(sign_up_confirm_password_value_text)) {
            Map<String, Object> user = new HashMap<>();
            user.put(USERNAME_KEY, sign_up_username_value_text);
            user.put(PASSWORD_KEY, SecurityMethods.hashString(sign_up_password_value_text));

            db.collection("users")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().get(USERNAME_KEY).equals(sign_up_username_value_text)) {
                            Toast.makeText(getApplicationContext(), "User already exists ", Toast.LENGTH_SHORT).show();
                            this.goToLoginActivity();
                            return;
                        }
                    }

                    // Add a new document with a generated ID
                    db.collection("users")
                    .add(user)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(SIGN_UP, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT).show();
                        this.goToLoginActivity();
                    })
                    .addOnFailureListener(e -> {
                        Log.w(SIGN_UP, "Error adding document", e);
                        Toast.makeText(getApplicationContext(), "Unable to create user", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Log.w(SIGN_UP, "Error getting documents.", task.getException());
                }
            });
        } else if (sign_up_username_value_text.isEmpty() || sign_up_password_value_text.isEmpty() || sign_up_confirm_password_value_text.isEmpty()) {
            Toast.makeText(getApplicationContext(), "All fields should be filled", Toast.LENGTH_SHORT).show();
        } else if (!sign_up_password_value_text.equals(sign_up_confirm_password_value_text)) {
            Toast.makeText(getApplicationContext(), "Password and ConfirmPassword must be equals", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToLoginActivity(View view) {
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(switchActivityIntent);
    }

    public void goToLoginActivity() {
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(switchActivityIntent);
    }

    public void goToUserDetailsActivity() {
        EditText sign_up_username_value = (EditText) findViewById(R.id.sign_up_username_value);
        String sign_up_username_value_text = sign_up_username_value.getText().toString();

        Intent switchActivityIntent = new Intent(this, UserDetailsFragment.class);
        switchActivityIntent.putExtra("username", sign_up_username_value_text);
        switchActivityIntent.putExtra("flagVisibility", 0);
        startActivity(switchActivityIntent);
    }

//    public void goToUserDetailsFragment() {
//
//        Fragment fragment = new UserDetailsActivity();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
//    }
}
