package com.example.grilledfood.RegionList;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.grilledfood.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump extends AppCompatActivity {

    public static HashMap<String, List<String>> getData(Context context) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        List<String> Cairo = new ArrayList<String>();
        Cairo.add(context.getResources().getString(R.string.ElMaadi));
        Cairo.add(context.getResources().getString(R.string.ElSayedaZeinab));
        Cairo.add(context.getResources().getString(R.string.MasrElQadema));

        Cairo.add(context.getResources().getString(R.string.ElManial));
        Cairo.add(context.getResources().getString(R.string.ElFostat));
        Cairo.add(context.getResources().getString(R.string.GardenCity));


        List<String> Giza = new ArrayList<String>();
        Giza.add(context.getResources().getString(R.string.Faisal));
        Giza.add(context.getResources().getString(R.string.ElHaram));
        Giza.add(context.getResources().getString(R.string.ElDokki));
        Giza.add(context.getResources().getString(R.string.ElAgoza));
        Giza.add(context.getResources().getString(R.string.ElMohandesen));


        expandableListDetail.put(context.getResources().getString(R.string.Cairo), Cairo);
        expandableListDetail.put(context.getResources().getString(R.string.Giza), Giza);
        return expandableListDetail;
    }
}
