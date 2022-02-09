package com.identitymanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.identitymanager.R;



public class LoginActivity extends AppCompatActivity {

    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String LOGIN = "login"; //for logs

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        EditText login_username_value = findViewById(R.id.login_username_value);
        EditText login_password_value = findViewById(R.id.login_password_value);
        String login_username_value_text = login_username_value.getText().toString();
        String login_password_value_text = login_password_value.getText().toString();

        db.collection("users")
        .get()
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if(task.getResult() != null) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(LOGIN, document.getId() + " => " + document.getData().get(USERNAME_KEY));
                        Log.d(LOGIN, document.getId() + " => " + document.getData().get(PASSWORD_KEY));

                        if (document.getData().get(USERNAME_KEY).equals(login_username_value_text) && document.getData().get(PASSWORD_KEY).equals(login_password_value_text)) {
                            this.goToDashboardFragment();
                            return;
                        }
                    }
                }
                Toast.makeText(getApplicationContext(), "Error on login", Toast.LENGTH_SHORT).show();
            } else {
                Log.w(LOGIN, "Error getting documents.", task.getException());
            }
        });
    }

    public void goToDashboardFragment() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        switchActivityIntent.putExtra("fragment", 1);
        startActivity(switchActivityIntent);
    }

    public void goToSignUpActivity(View view) {
        Intent switchActivityIntent = new Intent(this, SignUpActivity.class);
        startActivity(switchActivityIntent);
    }
}