package com.example.grilledfood;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.grilledfood.Common.Common;
import com.example.grilledfood.Helper.LocaleHelper;
import com.example.grilledfood.RegionList.ExpandableListDataPump;
import com.example.grilledfood.ViewHolder.CustomExpandableListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChoosingArea extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_area);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.Regions);
        setSupportActionBar(toolbar);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData(this);
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Toast.makeText(
//                        getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " -> "
//                                + expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition),
//                        Toast.LENGTH_SHORT).show();


                Common.mCurrentUser.setGlobalAddress(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition));
                FirebaseDatabase.getInstance().getReference("User")
                        .child(Common.mCurrentUser.getPhone())
                        .setValue(Common.mCurrentUser);
                finish();
                return true;


            }
        });
    }

}

