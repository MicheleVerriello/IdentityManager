package com.identitymanager.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.identitymanager.R;
import com.identitymanager.adapters.RecyclerAdapter;
import com.identitymanager.utilities.security.AES;

public class AccountDetailsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View settView = inflater.inflate(R.layout.fragment_account_details, container, false);

        String id, accountNameDetails, categoryDetails, emailDetails, usernameDetails, passwordDetails, passwordStrengthDetails;

        TextView title, accountName, category, email, username, password, passwordStrength;
        Button modify, delete;

        title = settView.findViewById(R.id.account_details_title);
        accountName = settView.findViewById(R.id.account_name_details);
        email = settView.findViewById(R.id.account_email_details);
        category = settView.findViewById(R.id.account_category_details);
        username = settView.findViewById(R.id.account_username_details);
        password = settView.findViewById(R.id.account_password_details);
        passwordStrength = settView.findViewById(R.id.account_password_strength_details);
        modify = settView.findViewById(R.id.button_modify);
        delete = settView.findViewById(R.id.button_delete);

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

        return settView;
    }
}