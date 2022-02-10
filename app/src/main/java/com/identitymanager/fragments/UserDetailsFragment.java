package com.identitymanager.fragments;

import android.os.Bundle;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.identitymanager.R;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
public class UserDetailsFragment extends Fragment {

    private static final String USERNAME_KEY = "username";
    private static final String FIRSTNAME_KEY = "firstname";
    private static final String LASTNAME_KEY = "lastname";
    private static final String PHONE_KEY = "phone";
    private static final String COUNTRY_KEY = "country";
    private static final String BIRTH = "birth";
    private static final String SAVE_CHANGES = "save_changes"; //for logs
    private static final String FLAG_VISIBILITY_KEY = "flagVisibility";

    FirebaseFirestore db;

    private int flagVisibility;
    private String username;

    public UserDetailsFragment() {
        super(R.layout.fragment_user_details);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        showUser();
        setView();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        username = requireArguments().getString(USERNAME_KEY);
        Log.d(SAVE_CHANGES, "username "+ username);
        flagVisibility = requireArguments().getInt(FLAG_VISIBILITY_KEY);
        Log.d(SAVE_CHANGES, "flag "+ flagVisibility);
    }


    public void saveChangesUserDetails(View view) {

        EditText user_details_username_value = view.findViewById(R.id.user_details_username_value);
        EditText user_details_first_name_value = view.findViewById(R.id.user_details_first_name_value);
        EditText user_details_last_name_value = view.findViewById(R.id.user_details_last_name_value);
        EditText user_details_phone_value = view.findViewById(R.id.user_details_phone_value);
        EditText user_details_country_value = view.findViewById(R.id.user_details_country_value);
        DatePicker user_details_birth_value = view.findViewById(R.id.user_details_birth_value);

        String user_details_username_value_text = user_details_username_value.getText().toString();
        String user_details_first_name_value_text = user_details_first_name_value.getText().toString();
        String user_details_last_name_value_text = user_details_last_name_value.getText().toString();
        String user_details_phone_value_text = user_details_phone_value.getText().toString();
        String user_details_country_value_text = user_details_country_value.getText().toString();
        String user_details_birth_value_text = user_details_birth_value.getDayOfMonth()+"."+ (user_details_birth_value.getMonth() + 1)+"."+user_details_birth_value.getYear();

        Map<String, Object> user_details = new HashMap<>();
        user_details.put(USERNAME_KEY, user_details_username_value_text);
        user_details.put(FIRSTNAME_KEY, user_details_first_name_value_text);
        user_details.put(LASTNAME_KEY, user_details_last_name_value_text);
        user_details.put(PHONE_KEY, user_details_phone_value_text);
        user_details.put(COUNTRY_KEY, user_details_country_value_text);
        user_details.put(BIRTH, user_details_birth_value_text);



        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if (document.getData().get(USERNAME_KEY).equals(username)) {
                                String userID = document.getId();


                                db.collection("users").document(userID)
                                        .update(user_details)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {

                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(SAVE_CHANGES, "user Profile is updated for "+ userID);
                                        Toast.makeText(getActivity(), "User detail updated", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(SAVE_CHANGES, "onFailure: " + e);
                                    }
                                });

                            }
                        }


                    } else {
                        Log.w(SAVE_CHANGES, "Error getting documents.", task.getException());
                    }



                    });

        flagVisibility = 1;
        showUser(view);
        setView(view);

    }


    public void edit_user_details_button(View view)  {


        flagVisibility = 0;
        showUser(view);
        setView(view);


    }

    public void setView (View view) {

        EditText user_details_username_value = view.findViewById(R.id.user_details_username_value);
        EditText user_details_first_name_value = view.findViewById(R.id.user_details_first_name_value);
        EditText user_details_last_name_value = view.findViewById(R.id.user_details_last_name_value);
        EditText user_details_phone_value = view.findViewById(R.id.user_details_phone_value);
        EditText user_details_country_value = view.findViewById(R.id.user_details_country_value);
        DatePicker user_details_birth_value = view.findViewById(R.id.user_details_birth_value);


        Button btn = view.findViewById(R.id.save_user_details_button);

        TextView user_details_username = view.findViewById(R.id.user_details_username);
        TextView user_details_first_name = view.findViewById(R.id.user_details_first_name);
        TextView user_details_last_name = view.findViewById(R.id.user_details_last_name);
        TextView user_details_phone = view.findViewById(R.id.user_details_phone);
        TextView user_details_country = view.findViewById(R.id.user_details_country);
        TextView user_details_birth = view.findViewById(R.id.user_details_birth);

        Button btn2 = view.findViewById(R.id.edit_user_details_button);

        if ( flagVisibility ==  1) {


            user_details_username_value.setVisibility(View.INVISIBLE);
            user_details_first_name_value.setVisibility(View.INVISIBLE);
            user_details_last_name_value.setVisibility(View.INVISIBLE);
            user_details_phone_value.setVisibility(View.INVISIBLE);
            user_details_country_value.setVisibility(View.INVISIBLE);
            user_details_birth_value.setVisibility(View.INVISIBLE);
            btn.setVisibility(View.INVISIBLE);

            user_details_username.setVisibility(View.VISIBLE);
            user_details_first_name.setVisibility(View.VISIBLE);
            user_details_last_name.setVisibility(View.VISIBLE);
            user_details_phone.setVisibility(View.VISIBLE);
            user_details_country.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);


        } else {


            user_details_username.setVisibility(View.INVISIBLE);
            user_details_first_name.setVisibility(View.INVISIBLE);
            user_details_last_name.setVisibility(View.INVISIBLE);
            user_details_phone.setVisibility(View.INVISIBLE);
            user_details_country.setVisibility(View.INVISIBLE);
            user_details_birth.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.INVISIBLE);

            user_details_username_value.setVisibility(View.VISIBLE);
            user_details_first_name_value.setVisibility(View.VISIBLE);
            user_details_last_name_value.setVisibility(View.VISIBLE);
            user_details_phone_value.setVisibility(View.VISIBLE);
            user_details_country_value.setVisibility(View.VISIBLE);
            user_details_birth_value.setVisibility(View.VISIBLE);
            btn.setVisibility(View.VISIBLE);


        }


    }



    public void showUser(View view) {

        EditText user_details_username_value = view.findViewById(R.id.user_details_username_value);
        EditText user_details_first_name_value = view.findViewById(R.id.user_details_first_name_value);
        EditText user_details_last_name_value = view.findViewById(R.id.user_details_last_name_value);
        EditText user_details_phone_value = view.findViewById(R.id.user_details_phone_value);
        EditText user_details_country_value = view.findViewById(R.id.user_details_country_value);
        DatePicker user_details_birth_value = view.findViewById(R.id.user_details_birth_value);
        user_details_birth_value.updateDate(1990,1,1);

        TextView user_details_username = view.findViewById(R.id.user_details_username);
        TextView user_details_first_name = view.findViewById(R.id.user_details_first_name);
        TextView user_details_last_name = view.findViewById(R.id.user_details_last_name);
        TextView user_details_phone = view.findViewById(R.id.user_details_phone);
        TextView user_details_country = view.findViewById(R.id.user_details_country);
        TextView user_details_birth = view.findViewById(R.id.user_details_birth);

        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document != null && username != null && document.getData().get(USERNAME_KEY).equals(username)) {

                                user_details_username.setText((String) document.getData().get(USERNAME_KEY));
                                user_details_first_name.setText((String) document.getData().get(FIRSTNAME_KEY));
                                user_details_last_name.setText((String) document.getData().get(LASTNAME_KEY));
                                user_details_phone.setText((String) document.getData().get(PHONE_KEY));
                                user_details_country.setText((String) document.getData().get(COUNTRY_KEY));
                                user_details_birth.setText((String) document.getData().get(BIRTH));

                                user_details_username_value.setText((String) document.getData().get(USERNAME_KEY));
                                user_details_first_name_value.setText((String) document.getData().get(FIRSTNAME_KEY));
                                user_details_last_name_value.setText((String) document.getData().get(LASTNAME_KEY));
                                user_details_phone_value.setText((String) document.getData().get(PHONE_KEY));
                                user_details_country_value.setText((String) document.getData().get(COUNTRY_KEY));

                                String[] date = ((String) document.getData().get(COUNTRY_KEY)).split(".");
                                user_details_birth_value.updateDate(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
                                //user_details_birth_value.setText( (String)document.getData().get(BIRTH));

                            }
                        }


                    } else {
                        Log.w(SAVE_CHANGES, "Error getting documents.", task.getException());
                    }
                });
    }
}