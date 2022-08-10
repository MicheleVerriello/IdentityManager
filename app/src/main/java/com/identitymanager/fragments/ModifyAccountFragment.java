package com.identitymanager.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.identitymanager.database.firestore.queries.FirebaseCategoryQuery;
import com.identitymanager.utilities.security.AES;
import com.identitymanager.utilities.security.PasswordStrength;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

public class ModifyAccountFragment extends Fragment {

    PasswordStrength strengthPassword;
    String authentication;
    String categorySelected;

    private final String FK_USER_ID_KEY = "fkIdUser";
    private final String NAME_ACCOUNT_KEY = "accountName";
    private final String USERNAME_KEY = "username";
    private final String EMAIL_KEY = "email";
    private final String PASSWORD_KEY = "password";
    private final String STRENGTH_KEY = "passwordStrength";
    private final String AUTHENTICATION_KEY = "twoFactorAuthentication";
    private final String CATEGORY_KEY = "category";
    private final String TIME_KEY = "lastUpdate";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settView = inflater.inflate(R.layout.fragment_modify_account, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();
        String idUserLoggedIn =  bundle.getString("id");
        String oldAccountNameDetails = bundle.getString("accountName");

        EditText account_name = settView.findViewById(R.id.editTextAccountNameModify);
        EditText username = settView.findViewById(R.id.editTextUsernameModify);
        EditText email = settView.findViewById(R.id.editTextEmailModify);
        EditText password = settView.findViewById(R.id.editTextPasswordModify);

        // Methods for checking each field
        setupAccountName(settView, account_name);
        setupUsername(settView, username);
        setupEmail(settView, email);
        setupPassword(settView, password);
        setupSuggest(settView, password);
        setup2FA(settView);
        setupCategory(settView);

        // Method to confirm the entry in the database
        setupModify(settView, account_name, username, email, password, idUserLoggedIn, oldAccountNameDetails);
        // Method to go back without modifications
        setupGoBack(settView);

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
        Button suggest = settView.findViewById(R.id.buttonSuggestModify);

        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = ThreadLocalRandom.current().nextInt(8, 14);
                password.setText(generatePassword(i));
            }
        });
    }

    public String generatePassword(int lenght) {
        char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
        char[] numbers = "0123456789".toCharArray();
        char[] symbols = "@#$%^&+=_.?!".toCharArray();
        Random r = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i<lenght; i++) {
            char c1 = letters[r.nextInt(letters.length)];
            sb.append(c1);
            i++;
            char c2 = numbers[r.nextInt(numbers.length)];
            sb.append(c2);
            i++;
            if (i<lenght) {
                char c3 = symbols[r.nextInt(symbols.length)];
                sb.append(c3);
            }
        }

        return sb.toString();
    }

    public boolean setupPassword(View settView, EditText password) {
        String passwordInput = password.getText().toString();
        TextView strength = settView.findViewById(R.id.textViewStrenghtModify);
        TextView error = settView.findViewById(R.id.textPasswordErrorModify);

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
                //Warning not required
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                //Warning not required
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (PASSWORD_PATTERN_MEDIUM_1.matcher(s).matches() || PASSWORD_PATTERN_MEDIUM_2.matcher(s).matches()) {
                    error.setText(" ");
                    strength.setText("MEDIUM");
                    strength.setTextColor(Color.parseColor("#FFA500"));
                    strengthPassword = PasswordStrength.MEDIUM;
                }
                if (PASSWORD_PATTERN_STRONG.matcher(s).matches()) {
                    error.setText(" ");
                    strength.setText("STRONG");
                    strength.setTextColor(Color.GREEN);
                    strengthPassword = PasswordStrength.STRONG;
                }
                if (!PASSWORD_PATTERN_MEDIUM_1.matcher(s).matches() && !PASSWORD_PATTERN_MEDIUM_2.matcher(s).matches() && !PASSWORD_PATTERN_STRONG.matcher(s).matches()) {
                    error.setText(" ");
                    strength.setText("WEAK");
                    strength.setTextColor(Color.RED);
                    strengthPassword = PasswordStrength.WEAK;
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
        RadioGroup radioGroup2FA = settView.findViewById(R.id.radioGroup2FAModify);

        radioGroup2FA.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButton2FA1Modify) {
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

        List<String> items = FirebaseCategoryQuery.getAllCategories(db);

        AutoCompleteTextView auto_completeTxt;
        ArrayAdapter<String> adapterItems;

        auto_completeTxt = settView.findViewById(R.id.autoCompleteTxtModify);
        adapterItems = new ArrayAdapter<>(getContext(), R.layout.category_list, items);
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

    public void setupModify(View settView, EditText account_name, EditText username, EditText email, EditText password, String idUserLoggedIn, String oldAccountNameDetails) {
        Button save = settView.findViewById(R.id.buttonSaveModify);

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
                    String AES256Password = AES.encrypt(password_text, idUserLoggedIn);

                    Date last_updated = new Date(System.currentTimeMillis());

                    Map<String, Object> accountToUpdate = new HashMap<>();
                    accountToUpdate.put(FK_USER_ID_KEY, idUserLoggedIn);
                    accountToUpdate.put(NAME_ACCOUNT_KEY, account_name_text);
                    accountToUpdate.put(USERNAME_KEY, username_text);
                    accountToUpdate.put(EMAIL_KEY, email_text);
                    accountToUpdate.put(PASSWORD_KEY, AES256Password);
                    accountToUpdate.put(STRENGTH_KEY, strengthPassword);
                    accountToUpdate.put(AUTHENTICATION_KEY, authentication);
                    accountToUpdate.put(CATEGORY_KEY, categorySelected);
                    accountToUpdate.put(TIME_KEY, last_updated);

                    modifyAccount(db, accountToUpdate, oldAccountNameDetails, getContext());

                } else {
                    Toast.makeText(getContext(), "Error, fill in the fields correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void modifyAccount(FirebaseFirestore db, Map<String, Object> accountToUpdate, String oldAccountNameDetails, Context context) {

        db.collection("accounts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getData().get("accountName").equals(accountToUpdate.get("accountName"))) {
                                Toast.makeText(context, "Name account already exists ", Toast.LENGTH_SHORT).show();
                                return;

                            }
                        }

                        // Update the document with new data
                        db.collection("accounts")
                                .get()
                                .addOnCompleteListener(operation -> {
                                    if (operation.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : operation.getResult()) {
                                            if (document.getData().get("fkIdUser").equals(accountToUpdate.get("fkIdUser")) && document.getData().get("accountName").equals(oldAccountNameDetails)) {
                                                db.collection("accounts").document(document.getData().get("docId").toString()).update("accountName", accountToUpdate.get("accountName"),
                                                        "username", accountToUpdate.get("username"),
                                                        "email", accountToUpdate.get("email"),
                                                        "password", accountToUpdate.get("password"),
                                                        "passwordStrength", accountToUpdate.get("passwordStrength"),
                                                        "twoFactorAuthentication", accountToUpdate.get("twoFactorAuthentication"),
                                                        "category", accountToUpdate.get("category"),
                                                        "lastUpdate", accountToUpdate.get("lastUpdate"));

                                                Toast.makeText(context, "Account modified", Toast.LENGTH_SHORT).show();
                                                Fragment fragment = new DashboardFragment();
                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                            }
                                        }
                                    }
                                });
                    }
                });

    }

    public void setupGoBack(View settview) {
        Button goBack = settview.findViewById(R.id.buttonGoBack);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AccountDetailsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });
    }
}
