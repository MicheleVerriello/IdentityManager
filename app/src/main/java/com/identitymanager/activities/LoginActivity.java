package com.identitymanager.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.identitymanager.R;
import com.identitymanager.utilities.security.Cryptography;

import java.util.concurrent.Executor;


public class LoginActivity extends AppCompatActivity {

    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String LOGIN = "login"; //for logs
    private String userId;

    //Biometric prompt
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    public void login(View view) {
        EditText login_username_value = findViewById(R.id.login_username_value);
        EditText login_password_value = findViewById(R.id.login_password_value);
        String login_username_value_text = login_username_value.getText().toString();
        String login_password_value_text = login_password_value.getText().toString();
        String hashedPassword = Cryptography.hashString(login_password_value_text);


        db.collection("users") //get all the users
        .get()
        .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if(task.getResult() != null) {
                    for (QueryDocumentSnapshot document : task.getResult()) { //check if the username and password exist into the db
                        if (document.getData().get(USERNAME_KEY).equals(login_username_value_text) && document.getData().get(PASSWORD_KEY).equals(hashedPassword)) {
                            this.userId = document.getId();
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
        switchActivityIntent.putExtra("userDocumentId", this.userId);
        startActivity(switchActivityIntent);
    }

    public void goToSignUpActivity(View view) {
        Intent switchActivityIntent = new Intent(this, SignUpActivity.class);
        startActivity(switchActivityIntent);
    }
}