package com.identitymanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.identitymanager.R;

import java.util.ArrayList;

public class InstagramWeakPasswordsActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_weak_passwords);

        listView = (ListView)findViewById(R.id.instagram_weak_passwords);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("\nUsername:Paul\nPassword:Fruit99\n");
        arrayList.add("\nUsername:Luna\nPassword:Space70\n");
        arrayList.add("\nUsername:Hector\nPassword:Circle83\n");
        arrayList.add("\nUsername:Mario\nPassword:Red54\n");
        arrayList.add("\nUsername:Laura\nPassword:Qwerty33\n");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
    }
}