package com.example.grilledfood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Helper.LocaleHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.grilledfood.Helper.LocaleHelper.SELECTED_LANGUAGE;

public class FirstPage extends AppCompatActivity {
    TextView chooseYourRegions;
    Button MenuListButton;

    CardView cardView;
    FirebaseDatabase database;
    DatabaseReference GlobalAddress;

    String languageSelected;
    String lang;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.SelectYourRegion);
        setSupportActionBar(toolbar);

        //Init Views
        cardView = findViewById(R.id.card_view);
        chooseYourRegions = findViewById(R.id.region_text);
        MenuListButton = findViewById(R.id.MenuList_Button);

        lang = SELECTED_LANGUAGE;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        languageSelected = sharedPreferences.getString(SELECTED_LANGUAGE, "");

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listIntent = new Intent(FirstPage.this, ChoosingArea.class);
                startActivity(listIntent);
            }
        });

        MenuListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MenuIntent = new Intent(FirstPage.this, Home.class);
                startActivity(MenuIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        chooseYourRegions = findViewById(R.id.region_text);
        chooseYourRegions.setText(Common.mCurrentUser.getGlobalAddress());
        if (languageSelected.equals("en") || languageSelected.equals("ar")) {

        }

    }
}
