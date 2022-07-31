package com.identitymanager.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.identitymanager.R;
import com.identitymanager.utilities.security.Cryptography;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class NewAccountFragment extends Fragment {

    public static final String SAVE = "save";
    public static final String ID_KEY = "id";
    public static final String NAME_ACCOUNT_KEY = "accountName";
    public static final String USERNAME_KEY = "username";
    public static final String EMAIL_KEY = "email";
    public static final String PASSWORD_KEY = "password";
    public static final String STRENGTH_KEY = "passwordStrength";
    public static final String AUTHENTICATION_KEY = "2fa";
    public static final String CATEGORY_KEY = "category";
    public static final String TIME_KEY = "lastUpdate";

    String strengthPassword;
    String authentication;
    String categorySelected;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settView = inflater.inflate(R.layout.fragment_new_account, container, false);

        // Getting the userDocumentId
        Bundle bundle = getActivity().getIntent().getExtras();
        String idUserLoggedIn = bundle.getString("userDocumentId");

        EditText account_name = settView.findViewById(R.id.editTextAccountName);
        EditText username = settView.findViewById(R.id.editTextUsername);
        EditText email = settView.findViewById(R.id.editTextEmail);
        EditText password = settView.findViewById(R.id.editTextPassword);

        // Methods for checking each field
        setupAccountName(settView, account_name);
        setupUsername(settView, username);
        setupEmail(settView, email);
        setupPassword(settView, password);
        setupSuggest(settView, password);
        setup2FA(settView);
        setupCategory(settView);

        // Method to confirm the entry in the database
        setupSave(settView, account_name, username, email, password, idUserLoggedIn);

        return settView;
    }

    public boolean setupAccountName(View settView, EditText account_name) {
        String accountNameInput = account_name.getText().toString();

        if (!accountNameInput.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean setupUsername(View settView, EditText username) {
        String usernameInput = username.getText().toString();

        if (!usernameInput.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean setupEmail(View settView, EditText email) {
        String emailInput = email.getText().toString();

        Pattern EMAIL_PATTERN = Pattern.compile("^" +
                "(?=.*[a-zA-Z])" +           //any letter
                "(?=.*[@])" +                //at least 1 use of @
                "(?=\\S+$)" +                //no white spaces
                ".{6,}" +                    //at least 6 characters
                "$");

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!EMAIL_PATTERN.matcher(s).matches()) {
                    email.setError("Invalid email");
                }
            }
        });

        if (!emailInput.isEmpty() && EMAIL_PATTERN.matcher(emailInput).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public void setupSuggest(View settView, EditText password) {
        Button suggest = settView.findViewById(R.id.buttonSuggest);

        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = ThreadLocalRandom.current().nextInt(8, 14);
                password.setText(generatePassword(i));
            }
        });
    }

    public String generatePassword(int lenght) {
        char[] chars1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] chars2 = "0123456789".toCharArray();
        char[] chars3 = "@#$%^&+=_.?!".toCharArray();
        Random r = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i<lenght; i++) {
            char c1 = chars1[r.nextInt(chars1.length)];
            sb.append(c1);
            i++;
            char c2 = chars2[r.nextInt(chars2.length)];
            sb.append(c2);
            i++;
            if (i<lenght) {
                char c3 = chars3[r.nextInt(chars3.length)];
                sb.append(c3);
            }
        }

        return sb.toString();
    }

    public boolean setupPassword(View settView, EditText password) {
        String passwordInput = password.getText().toString();
        TextView strength = settView.findViewById(R.id.textViewStrenght);
        TextView error = settView.findViewById(R.id.textPasswordError);

        Pattern PASSWORD_PATTERN_MEDIUM_1 = Pattern.compile("^" +
                "(?=.*[0-9])" +            //at least 1 digit
                "(?=.*[a-zA-Z])" +         //any letter
                "(?=\\S+$)" +              //no white spaces
                ".{6,24}" +                //between 6 and 24 characters
                "$");

        Pattern PASSWORD_PATTERN_MEDIUM_2 = Pattern.compile("^" +
                "(?=.*[a-zA-Z])" +         //any letter
                "(?=.*[@#$%^&+=_.?!])" +   //at least 1 special character
                "(?=\\S+$)" +              //no white spaces
                ".{6,24}" +                //between 6 and 24 characters
                "$");

        Pattern PASSWORD_PATTERN_STRONG = Pattern.compile("^" +
                "(?=.*[0-9])" +              //at least 1 digit
                "(?=.*[a-zA-Z])" +           //any letter
                "(?=.*[@#$%^&+=_.?!])" +     //at least 1 special character
                "(?=\\S+$)" +                //no white spaces
                ".{8,24}" +                  //between 8 and 24 characters
                "$");

        Pattern PASSWORD_SPACES = Pattern.compile("^" +
                "(?=\\S+$)" +                //no white spaces
                ".{1,24}" +                  //between 1 and 24 characters
                "$");

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (PASSWORD_PATTERN_MEDIUM_1.matcher(s).matches() || PASSWORD_PATTERN_MEDIUM_2.matcher(s).matches()) {
                    error.setText(" ");
                    strength.setText("MEDIUM");
                    strength.setTextColor(Color.parseColor("#FFA500"));
                    strengthPassword = "medium";
                }
                if (PASSWORD_PATTERN_STRONG.matcher(s).matches()) {
                    error.setText(" ");
                    strength.setText("STRONG");
                    strength.setTextColor(Color.GREEN);
                    strengthPassword = "strong";
                }
                if (!PASSWORD_PATTERN_MEDIUM_1.matcher(s).matches() && !PASSWORD_PATTERN_MEDIUM_2.matcher(s).matches() && !PASSWORD_PATTERN_STRONG.matcher(s).matches()) {
                    error.setText(" ");
                    strength.setText("WEAK");
                    strength.setTextColor(Color.RED);
                    strengthPassword = "weak";
                }
                if (!PASSWORD_SPACES.matcher(s).matches()) {
                    error.setText("No white spaces allowed");
                    strength.setText("ERROR");
                    strength.setTextColor(Color.RED);
                }
                if (password.length() > 24) {
                    error.setText("Password too long");
                    strength.setText("ERROR");
                    strength.setTextColor(Color.RED);
                }
            }
        });

        if (!passwordInput.isEmpty() && PASSWORD_SPACES.matcher(passwordInput).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean setup2FA(View settView) {
        RadioGroup radioGroup2FA = settView.findViewById(R.id.radioGroup2FA);

        radioGroup2FA.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButton2FA1) {
                    authentication = "Yes";
                } else {
                    authentication = "No";
                }
            }
        });

        if (radioGroup2FA.getCheckedRadioButtonId() == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean setupCategory(View settView) {
        String[] items = {"Social", "Music", "Entertainment", "Sport", "Film"};
        AutoCompleteTextView auto_completeTxt;
        ArrayAdapter<String> adapterItems;

        auto_completeTxt = settView.findViewById(R.id.autoCompleteTxt);
        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.category_list, items);
        auto_completeTxt.setAdapter(adapterItems);

        auto_completeTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                categorySelected = adapterView.getItemAtPosition(i).toString();
            }
        });

        if (TextUtils.isEmpty(auto_completeTxt.getText())) {
            return false;
        } else {
            return true;
        }
    }

    public void setupSave(View settView, EditText account_name, EditText username, EditText email, EditText password, String idUserLoggedIn) {
        Button save = settView.findViewById(R.id.buttonSave);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check == 1 -> no errors detected
                // Check == 0 -> errors detected
                int check = 1;

                if (!setupAccountName(settView, account_name)) {
                    check = 0;
                }

                if (!setupUsername(settView, username)) {
                    check = 0;
                }

                if (!setupEmail(settView, email)) {
                    check = 0;
                }

                if (!setupPassword(settView, password)) {
                    check = 0;
                }

                if (!setup2FA(settView)) {
                    check = 0;
                }

                if (!setupCategory(settView)) {
                    check = 0;
                }

                // Connection with the database
                if (check == 1) {
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
                                            Toast.makeText(getContext(), "Name account already exists ", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                // Add a new document with a generated ID
                                db.collection("accounts")
                                        .add(account)
                                        .addOnSuccessListener(documentReference -> {
                                            Log.d(SAVE, "DocumentSnapshot added with ID: " + documentReference.getId());
                                            Toast.makeText(getContext(), "Account added", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.w(SAVE, "Error adding document", e);
                                            Toast.makeText(getContext(), "Unable to create user", Toast.LENGTH_SHORT).show();
                                        });
                                } else {
                                    Log.w(SAVE, "Error getting documents", task.getException());
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "Error, fill in the fields correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}