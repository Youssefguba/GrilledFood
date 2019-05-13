package com.example.grilledfood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Database.Database;
import com.example.grilledfood.Helper.LocaleHelper;
import com.example.grilledfood.Model.Category;
import com.example.grilledfood.Model.Token;
import com.example.grilledfood.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //    private static final String TAG = "Home";
//    private static final int ACTIVITY_NUM = 0;
    FirebaseDatabase database;
    DatabaseReference category;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    Database localDb;

    TextView textFullName, textCartItemCount;
    View actionView;

    BottomNavigationViewEx bottomNavigationViewEx;


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
                .build());

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.Menu);
        setSupportActionBar(toolbar);

        Paper.init(this);

        //Get Token when user Login
        updateToken(FirebaseInstanceId.getInstance().getToken());
        bottomNavigationViewEx = findViewById(R.id.bottom_navigation_ex);
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(true);
        bottomNavigationViewEx.enableItemShiftingMode(true);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.main_orders:
                        selectedFragment = CategoryFragment.newInstance();
                        break;

                    case R.id.menu_meal:
                        selectedFragment = MealsFragment.newInstance();
                        break;

                    case R.id.menu_sandwich:
                        selectedFragment = SandwichFragment.newInstance();
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        setDefaultFragment();


        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        localDb = new Database(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set Name of User
        View headerView = navigationView.getHeaderView(0);
        textFullName = headerView.findViewById(R.id.textFullName);
        textFullName.setText(Common.mCurrentUser.getName());


    }

    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, CategoryFragment.newInstance());
        transaction.commit();
    }


    private void updateToken(String token) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tokenRef = database.getReference("Tokens");
        Token data = new Token(token, false); //False because this token sent from client App..
        tokenRef.child(Common.mCurrentUser.getPhone()).setValue(data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);
        actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart: {
                Intent cartIntent = new Intent(this, Cart.class);
                startActivity(cartIntent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {
        if (textCartItemCount != null) {
            if (new Database(this).getCountCarts(Common.mCurrentUser.getPhone()) == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(new Database(this).getCountCarts(Common.mCurrentUser.getPhone())));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBadge();
        if (adapter != null) {
            adapter.startListening();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            Intent menuIntent = new Intent(this, Home.class);
            startActivity(menuIntent);

        } else if (id == R.id.nav_cart) {
            Intent cartIntent = new Intent(this, Cart.class);
            startActivity(cartIntent);
        } else if (id == R.id.home_address) {
            showHomeAddressDialog();


        } else if (id == R.id.nav_orders) {
            Intent orderIntent = new Intent(this, OrderStatus.class);
            startActivity(orderIntent);

        }
//        else if (id == R.id.nav_log_out) {
//            //Delete Remember User & password
//            Paper.book().destroy();
//            //Logout
//            Intent logOutIntent = new Intent(this, SignIn.class);
//            logOutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(logOutIntent);
//
//        }
//        else if (id == R.id.language_icon) {
//            showListOfLanguage();
//        }

        else if (id == R.id.discount_news) {
            showDiscountNews();

        }


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDiscountNews() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discount News");

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.setting_layout, null);
        CheckBox checkBox = view.findViewById(R.id.checkBox_SubscribeNews);

        //Add Code to remember the state of CheckBox
        Paper.init(this);
        String isSubsribe = Paper.book().read("sub_news");

        if (isSubsribe == null || TextUtils.isEmpty(isSubsribe) || isSubsribe.equals("false")) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(true);

        }

        builder.setView(view);

        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();

            if (checkBox.isChecked()) {
                FirebaseMessaging.getInstance().subscribeToTopic(Common.topicName);
                //Write Value
                Paper.book().write("sub_news", "true");

            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Common.topicName);
                //Write Value
                Paper.book().write("sub_news", "false");
            }

        });

        builder.show(); // Don't forget it ..
    }

    private void showHomeAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Address");
        builder.setMessage("Please Enter your address");

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.home_address_layout, null);
        final MaterialEditText editAddress = view.findViewById(R.id.editHomeAddress);

        builder.setView(view);

        builder.setPositiveButton("Update", (dialog, which) -> {
            dialog.dismiss();
            //Set Home Address
            Common.mCurrentUser.setHomeAddress(editAddress.getText().toString());
            FirebaseDatabase.getInstance().getReference("User")
                    .child(Common.mCurrentUser.getPhone())
                    .setValue(Common.mCurrentUser)
                    .addOnCompleteListener(task -> Toast.makeText(Home.this, " Address Updated!", Toast.LENGTH_SHORT).show());
        });

        builder.show(); // Don't forget it ..
    }

    private void showListOfLanguage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Your Language");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.language_layout, null);

        final RadioButton arabicChoose = view.findViewById(R.id.arabic_choose);
        final RadioButton englishChoose = view.findViewById(R.id.english_choose);


        arabicChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    LocaleHelper.setLocale(Home.this, "ar");
                finish();
                recreate();
            }
        });

        englishChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    LocaleHelper.setLocale(Home.this, "en");
                finish();
                recreate();
            }
        });


        builder.setView(view);

        builder.show();


    }


}
