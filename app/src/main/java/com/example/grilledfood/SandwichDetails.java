package com.example.grilledfood;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Database.Database;
import com.example.grilledfood.Helper.LocaleHelper;
import com.example.grilledfood.Model.Food;
import com.example.grilledfood.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.submitbutton.SubmitButton;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.example.grilledfood.Helper.LocaleHelper.SELECTED_LANGUAGE;

public class SandwichDetails extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference foodList;
    DatabaseReference foodListForArabic;

    DatabaseReference ratingTable;
    int riceButton;
    TextView foodName, foodPrice, foodDescription, taxesText, taxesNumber, riceCost, tahiniCost, nameOfRice;
    ImageView foodImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton ratingButton;
    ElegantNumberButton elegantNumberButton, riceNumber, tahiniNumber;
    RatingBar ratingBar;
    SubmitButton cartButton;
    RadioButton rice;
    ActionBar actionBar;
    Food currentFood;

    String languageSelected;
    String lang;
    String sandwichId = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Arvo-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        setContentView(R.layout.activity_food_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("SandwichCategory");
        foodListForArabic = database.getReference("SandwichCategoryArabic");

        ratingTable = database.getReference("Rating");

        //Init Views
        foodName = findViewById(R.id.food_name);
        cartButton = findViewById(R.id.submit_button);
        foodPrice = findViewById(R.id.food_price);
        foodDescription = findViewById(R.id.food_description);
        foodImage = findViewById(R.id.food_image);
        taxesNumber = findViewById(R.id.tax_number);

        riceCost = findViewById(R.id.costOfRice);
        riceNumber = findViewById(R.id.numberOfRice);

        nameOfRice = findViewById(R.id.nameOfRice);
        rice = findViewById(R.id.radioRiceChoose);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAppbar);

        elegantNumberButton = findViewById(R.id.number_button);


        lang = SELECTED_LANGUAGE;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        languageSelected = sharedPreferences.getString(SELECTED_LANGUAGE, "");


        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ifExists = new Database(getBaseContext()).checkFoodExists(sandwichId, Common.mCurrentUser.getPhone());
                if (!ifExists) {
                    new Database(getBaseContext()).addToCart(new Order(
                            Common.mCurrentUser.getPhone(),
                            sandwichId,
                            currentFood.getNameOfFood(),
                            elegantNumberButton.getNumber(),
                            currentFood.getPrice(),
                            currentFood.getDiscount(),
                            currentFood.getImage(),
                            currentFood.getExtraOrder(),
                            currentFood.getExtraPrice(),
                            riceNumber.getNumber()
                    ));
                } else {
                    new Database(getBaseContext()).increaseCart(sandwichId, Common.mCurrentUser.getPhone());
                }
                Toast.makeText(SandwichDetails.this, getString(R.string.Added_to_Cart_Successfully), Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        // Get Food Id from Home Activity "Intent "
        if (getIntent() != null) {
            sandwichId = getIntent().getStringExtra("SandwichId");
            if (!sandwichId.isEmpty()) {
                if (Common.isConnectionToInternet(this)) {
                    if (languageSelected.equals("en")) {
                        loadListFood(sandwichId);
                    } else if (languageSelected.equals("ar")) {
                        loadListFoodforArabicLanguage(sandwichId);
                    }

                } else {
                    Toast.makeText(SandwichDetails.this, "Please check your Internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (Common.mCurrentUser.getGlobalAddress().equals(getResources().getString(R.string.ElManial)) || Common.mCurrentUser.getGlobalAddress().equals("المنيل")) {
            taxesNumber.setText(getResources().getString(R.string.free));
        } else {
            taxesNumber.setText("9 " + getResources().getString(R.string.egp));
        }
    }

    private void loadListFoodforArabicLanguage(String sandwichId) {

        foodListForArabic.child(sandwichId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                //Set Image
                Picasso.with(getBaseContext())
                        .load(currentFood.getImage())
                        .into(foodImage);

                collapsingToolbarLayout.setTitle(currentFood.getNameOfFood());
                foodPrice.setText(currentFood.getPrice());
                foodName.setText(currentFood.getNameOfFood());
                foodDescription.setText(currentFood.getDescription());
                nameOfRice.setText(currentFood.getExtraOrder());
                riceCost.setText(currentFood.getExtraPrice());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void loadListFood(String sandwichId) {
        foodList.child(sandwichId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                //Set Image
                Picasso.with(getBaseContext())
                        .load(currentFood.getImage())
                        .into(foodImage);

                collapsingToolbarLayout.setTitle(currentFood.getNameOfFood());
                foodPrice.setText(currentFood.getPrice());
                foodName.setText(currentFood.getNameOfFood());
                foodDescription.setText(currentFood.getDescription());
                nameOfRice.setText(currentFood.getExtraOrder());
                riceCost.setText(currentFood.getExtraPrice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Common.mCurrentUser.getGlobalAddress().equals(getResources().getString(R.string.ElManial)) || Common.mCurrentUser.getGlobalAddress().equals("المنيل")) {
            taxesNumber.setText(getResources().getString(R.string.free));

        } else {
            taxesNumber.setText("9 " + this.getResources().getString(R.string.egp));
        }

        if (languageSelected.equals("en")) {
            loadListFood(sandwichId);
        } else if (languageSelected.equals("ar")) {
            loadListFoodforArabicLanguage(sandwichId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Common.mCurrentUser.getGlobalAddress().equals(getResources().getString(R.string.ElManial)) || Common.mCurrentUser.getGlobalAddress().equals("المنيل")) {
            taxesNumber.setText(getResources().getString(R.string.free));

        } else {
            taxesNumber.setText("9 " + this.getResources().getString(R.string.egp));
        }

        if (languageSelected.equals("en")) {
            loadListFood(sandwichId);
        } else if (languageSelected.equals("ar")) {
            loadListFoodforArabicLanguage(sandwichId);
        }


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


}
