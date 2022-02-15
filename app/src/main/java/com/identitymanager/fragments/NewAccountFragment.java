package com.identitymanager.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.identitymanager.R;

import java.util.Random;
import java.util.regex.Pattern;

public class NewAccountFragment extends Fragment {

    //prendere l' id dello user
    //verra' poi creata l' interfaccia
    //particolare attenzione alle categorie
    //dropdown con la lista delle categorie che ci sono gia' nel db
    //una volta selezionata la categoria nell' oggetto dell' account andra' solo il documentId della categoria
    //ovviamente sara' l' utente a digitare i campi username, password, mail

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settView = inflater.inflate(R.layout.fragment_new_account, container, false);

        Bundle bundle = getActivity().getIntent().getExtras();
        String idUserLoggedIn = bundle.getString("userDocumentId");

        EditText account_name = settView.findViewById(R.id.editTextAccountName);
        EditText username = settView.findViewById(R.id.editTextUsername);
        EditText email = settView.findViewById(R.id.editTextEmail);
        EditText password = settView.findViewById(R.id.editTextPassword);

        setupAccountName(settView, account_name);
        setupUsername(settView, username);
        setupEmail(settView, email);
        setupPassword(settView, password);
        setupSuggest(settView, password);
        setup2FA(settView);
        setupCategory(settView);
        setupSave(settView, idUserLoggedIn);

        return settView;
    }

    public int check(int valid) {
        int check = 1;
        check = valid;

        return check;
    }

    public void setupAccountName(View settView, EditText account_name) {
        Pattern ACCOUNT_NAME_PATTERN = Pattern.compile("^" +
                "(?=.*[a-zA-Z])" +           //any letter
                "(?=\\S+$)" +                //no white spaces
                ".{4,16}" +                  //between 4 and 16 characters
                "$");

        account_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 4) {
                    account_name.setError("Account name too short");
                } else if (s.length() > 16) {
                    account_name.setError("Account name too long");
                } else if (!ACCOUNT_NAME_PATTERN.matcher(s).matches()) {
                    account_name.setError("No white spaces allowed");
                }
            }
        });
    }

    public void setupUsername(View settView, EditText username) {
        Pattern USERNAME_PATTERN = Pattern.compile("^" +
                "(?=.*[a-zA-Z])" +           //any letter
                "(?=\\S+$)" +                //no white spaces
                ".{4,16}" +                  //between 4 and 16 characters
                "$");

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 4) {
                    username.setError("Username too short");
                } else if (s.length() > 16) {
                    username.setError("Username too long");
                } else if (!USERNAME_PATTERN.matcher(s).matches()) {
                    username.setError("No white spaces allowed");
                }
            }
        });
    }

    public void setupEmail(View settView, EditText email) {
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
    }

    public void setupSuggest(View settView, EditText password) {
        Button suggest = settView.findViewById(R.id.buttonSuggest);

        suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setText(generatePassword(8));
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

    public void setupPassword(View settView, EditText password) {
        Pattern PASSWORD_PATTERN_MEDIUM = Pattern.compile("^" +
                "(?=.*[0-9])" +            //at least 1 digit
                "(?=.*[a-zA-Z])" +         //any letter
                "(?=\\S+$)" +              //no white spaces
                ".{5,24}" +                //between 5 and 24 characters
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
                ".{1,24}" +                  //between 8 and 24 characters
                "$");

        TextView Strenght = settView.findViewById(R.id.textViewStrenght);

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (PASSWORD_PATTERN_MEDIUM.matcher(s).matches()) {
                    Strenght.setText("MEDIUM");
                    Strenght.setTextColor(Color.parseColor("#FFA500"));
                }
                if (PASSWORD_PATTERN_STRONG.matcher(s).matches()) {
                    Strenght.setText("STRONG");
                    Strenght.setTextColor(Color.GREEN);
                }
                if (!PASSWORD_PATTERN_MEDIUM.matcher(s).matches() && !PASSWORD_PATTERN_STRONG.matcher(s).matches()) {
                    Strenght.setText("WEAK");
                    Strenght.setTextColor(Color.RED);
                }
                if (!PASSWORD_SPACES.matcher(s).matches()) {
                    password.setError("No white spaces allowed");
                    Strenght.setText("ERROR");
                    Strenght.setTextColor(Color.RED);
                }
                if (password.length() > 24) {
                    password.setError("Password too long");
                    Strenght.setText("ERROR");
                    Strenght.setTextColor(Color.RED);
                }
            }
        });
    }

    public void setup2FA(View settView) {
        RadioGroup radioGroup2FA = settView.findViewById(R.id.radioGroup2FA);

        radioGroup2FA.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButton2FA1) {
                    //Caso YES
                } else {
                    //Caso NO
                }
            }
        });
    }

    public void setupCategory(View settView) {
        String[] items = {"Social", "Music", "Entertainment", "Sport", "Film"};
        AutoCompleteTextView auto_completeTxt;
        ArrayAdapter<String> adapterItems;

        auto_completeTxt = settView.findViewById(R.id.autoCompleteTxt);
        adapterItems = new ArrayAdapter<String>(getContext(), R.layout.category_list, items);
        auto_completeTxt.setAdapter(adapterItems);
    }

    public void setupSave(View settView, String idUserLoggedIn) {
        Button save = settView.findViewById(R.id.buttonSave);

        //Se tutti i campi sono validi (utilizzare un flag ogni volta che c'è un errore di controllo) si va avanti
        //Verificare l'idUserLoggedIn
        //Verificare che non esista un account con lo stesso nome
        //Salvare l'account creato creando un collegamento con il database (2FA, Category)
    }
}