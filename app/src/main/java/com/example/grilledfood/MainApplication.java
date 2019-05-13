package com.example.grilledfood;

import android.app.Application;
import android.content.Context;

import com.example.grilledfood.Helper.LocaleHelper;

public class MainApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }
}
