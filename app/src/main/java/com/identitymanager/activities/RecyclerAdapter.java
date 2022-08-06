package com.identitymanager.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.identitymanager.R;
import com.identitymanager.models.data.Account;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    Context context;
    ArrayList<Account> list;

    public RecyclerAdapter(Context context, ArrayList<Account> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);

        return new RecyclerHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {

        Account account = list.get(position);

        holder.accountName.setText(account.getAccountName());
        holder.email.setText(account.getEmail());
        holder.username.setText(account.getUsername());
        holder.password.setText(account.getPassword());
        holder.passwordStrength.setText(account.getPasswordStrength());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder {

        TextView accountName, email, username, password, passwordStrength;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);

            accountName = itemView.findViewById(R.id.account_name);
            email = itemView.findViewById(R.id.account_email);
            username = itemView.findViewById(R.id.account_username);
            password = itemView.findViewById(R.id.account_password);
            passwordStrength = itemView.findViewById(R.id.account_password_strength);

        }
    }

}
