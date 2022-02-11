package com.identitymanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.identitymanager.R;

public class NewAccountFragment extends Fragment {

    //prendere l' id dello user
    //verra' poi creata l' interfaccia
    //particolare attenzione alle categorie
    //dropdown con la lista delle categorie che ci sono gia' nel db
    //una volta selezionata la categoria nell' oggetto dell' account andra' solo il documentId della categoria
    //ovviamente sara' l' utente a digitare i campi username, password, mail

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_account, container, false);
    }
}
