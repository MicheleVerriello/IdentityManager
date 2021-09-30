package com.identitymanager.shared;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private Context ct;

    public LanguageManager(Context ctx) {
        ct = ctx;
    }

    public void updateResources(String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        ct.getResources().updateConfiguration(configuration, ct.getResources().getDisplayMetrics());
    }
}
