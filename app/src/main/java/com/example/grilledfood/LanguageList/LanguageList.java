package com.example.grilledfood.LanguageList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LanguageList {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> Language = new ArrayList<String>();
        Language.add("English");
        Language.add("Arabic");

        expandableListDetail.put("Language", Language);
        return expandableListDetail;
    }
}
