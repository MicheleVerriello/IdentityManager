package com.identitymanager.database.firestore.callbacks;

import com.identitymanager.models.Account;

public interface GetAccountCallback {

    void onCallback(Account account);
}
