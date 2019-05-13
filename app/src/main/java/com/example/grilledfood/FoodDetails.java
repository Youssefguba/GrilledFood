package com.example.grilledfood;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Database.Database;
import com.example.grilledfood.Helper.LocaleHelper;
import com.example.grilledfood.Model.Food;
import com.example.grilledfood.Model.Order;
import com.example.grilledfood.Model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.submitbutton.SubmitButton;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.example.grilledfood.Helper.LocaleHelper.SELECTED_LANGUAGE;

public class FoodDetails extends AppCompatActivity  implements RatingDialogListener {

    FirebaseDatabase database;
    DatabaseReference foodList;
    DatabaseReference ratingTable;
    DatabaseReference foodListForArabic;
    public TextView taxesNumber;
    ImageView foodImage;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView foodName, foodPrice, foodDescription, riceCost, nameOfRice, halfName, halfCost, quarterName, quarterCost;
    ElegantNumberButton elegantNumberButton, riceNumber, halfButton, quarterButton;
    SubmitButton cartButton;
    ActionBar actionBar;
    Food currentFood;
    RadioButton rice, halfChoice, quarterChoice;

    String languageSelected;
    String lang;
    String categoryId = "";

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

        setContentView(R.layout.activity_food_details_category_fragment);
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
        foodList = database.getReference("Category");
        foodListForArabic = database.getReference("CategoryArabic");
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

        halfButton = findViewById(R.id.numberOfHalf);
        halfChoice = findViewById(R.id.radiohalfChoose);
        halfCost = findViewById(R.id.costofHalf);
        halfName = findViewById(R.id.nameOfHalf);

        quarterButton = findViewById(R.id.numberOfQuarter);
        quarterChoice = findViewById(R.id.radioQuarterChoose);
        quarterCost = findViewById(R.id.costofQuarter);
        quarterName = findViewById(R.id.nameOfQuarter);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandAppbar);

        elegantNumberButton = findViewById(R.id.number_button);


        lang = SELECTED_LANGUAGE;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        languageSelected = sharedPreferences.getString(SELECTED_LANGUAGE, "");

        cartButton.setOnClickListener(v -> {
            boolean ifExists = new Database(getBaseContext()).checkFoodExists(categoryId, Common.mCurrentUser.getPhone());
            if (!ifExists) {
                new Database(getBaseContext()).addToCart(new Order(
                        Common.mCurrentUser.getPhone(),
                        categoryId,
                        currentFood.getNameOfFood(),
                        elegantNumberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount(),
                        currentFood.getImage(),
                        currentFood.getExtraOrder(),
                        currentFood.getExtraPrice(),
                        riceNumber.getNumber(),
                        currentFood.getHalfOrderName(),
                        currentFood.getHalfOrderPrice(),
                        halfButton.getNumber(),
                        currentFood.getQuarterOrderName(),
                        currentFood.getQuarterOrderPrice(),
                        quarterButton.getNumber()

                ));
            } else {
                new Database(getBaseContext()).increaseCart(categoryId, Common.mCurrentUser.getPhone());
            }
            Toast.makeText(FoodDetails.this, getString(R.string.Added_to_Cart_Successfully), Toast.LENGTH_SHORT).show();
            finish();
        });


        // Get Food Id from Home Activity "Intent "
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
            if (!categoryId.isEmpty()) {
                if (Common.isConnectionToInternet(this)) {
                    if (languageSelected.equals("en")) {
                        loadListFood(categoryId);
                    } else if (languageSelected.equals("ar")) {
                        loadListFoodforArabicLanguage(categoryId);
                    }

                } else {
                    Toast.makeText(FoodDetails.this, "Please check your Internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (Common.mCurrentUser.getGlobalAddress().equals(getResources().getString(R.string.ElManial)) || Common.mCurrentUser.getGlobalAddress().equals("المنيل")) {
            taxesNumber.setText(getResources().getString(R.string.free));

        } else {
            taxesNumber.setText("9 " + this.getResources().getString(R.string.egp));
        }
    }

    private void loadListFood(String categoryId) {
        foodList.child(categoryId).addValueEventListener(new ValueEventListener() {
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
                halfName.setText(currentFood.getHalfOrderName());
                halfCost.setText(currentFood.getHalfOrderPrice());
                quarterName.setText(currentFood.getQuarterOrderName());
                quarterCost.setText(currentFood.getQuarterOrderPrice());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Ok", "Very Good", "Excellent"))
                .setDefaultRating(1)
                .setTitle("Rating this food")
                .setDescription("Please give us your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment about this meal")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogStyle)
                .create(FoodDetails.this)
                .show();

    }

    @Override
    public void onNegativeButtonClicked() {
    }

    @Override
    public void onPositiveButtonClicked(int value, @NotNull String comments) {
        //Get rating and upload to Firebase Server..

        Rating rating = new Rating(Common.mCurrentUser.getPhone(),
                categoryId,
                String.valueOf(value),
                comments);

        ratingTable.child(Common.mCurrentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(Common.mCurrentUser.getPhone()).exists()) {
                    //Removing old value
                    ratingTable.child(Common.mCurrentUser.getPhone()).removeValue();
                    //update new value
                    ratingTable.child(Common.mCurrentUser.getPhone()).setValue(rating);
                } else {
                    ratingTable.child(Common.mCurrentUser.getPhone()).setValue(rating);

                }
                Toast.makeText(FoodDetails.this, "Thank you for submit your Orders :)", Toast.LENGTH_SHORT).show();
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
            loadListFood(categoryId);
        } else if (languageSelected.equals("ar")) {
            loadListFoodforArabicLanguage(categoryId);
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
            loadListFood(categoryId);
        } else if (languageSelected.equals("ar")) {
            loadListFoodforArabicLanguage(categoryId);
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


    private void loadListFoodforArabicLanguage(String categoryId) {
        foodListForArabic.child(categoryId).addValueEventListener(new ValueEventListener() {
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
                halfName.setText(currentFood.getHalfOrderName());
                halfCost.setText(currentFood.getHalfOrderPrice());
                quarterName.setText(currentFood.getQuarterOrderName());
                quarterCost.setText(currentFood.getQuarterOrderPrice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}

