package com.identitymanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
            user.put(PASSWORD_KEY, sign_up_password_value_text);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(SIGN_UP, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(SIGN_UP, "Error adding document", e);
                    }
                });
        }
    }

    public void goToLoginActivity(View view) {
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(switchActivityIntent);
    }
}
