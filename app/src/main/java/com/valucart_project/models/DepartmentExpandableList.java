package com.valucart_project.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DepartmentExpandableList {
    public static HashMap<String, List<String>> getData(Categories response) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> vegetable = new ArrayList<String>();
        vegetable.add("Tomatoes");

        for(int counter=0;counter<response.getData().size();counter++) {
            expandableListDetail.put(response.getData().get(counter).getName(), vegetable);
        }
        return expandableListDetail;
    }
}