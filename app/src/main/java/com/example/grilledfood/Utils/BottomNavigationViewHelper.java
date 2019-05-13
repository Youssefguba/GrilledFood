package com.example.grilledfood.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.grilledfood.CategoryFragment;
import com.example.grilledfood.FoodDetails;
import com.example.grilledfood.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {

    public static void setupBottomNavigationView(BottomNavigationViewEx navigationViewEx) {
        navigationViewEx.enableAnimation(true);
        navigationViewEx.enableShiftingMode(true);
        navigationViewEx.enableItemShiftingMode(true);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx viewEx) {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.main_orders: // Activity Num ==> 0
                        Intent homeIntent = new Intent(context, CategoryFragment.class);
                        context.startActivity(homeIntent);

                        break;

                    case R.id.menu_meal: // Activity Num ==> 1
                        Intent Intent = new Intent(context, FoodDetails.class);
                        context.startActivity(Intent);
                        break;

                    case R.id.menu_sandwich: // Activity Num ==> 2

                        break;
                }

                return false;
            }
        });
    }
}
