package com.identitymanager.activities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> thoseTrust = new ArrayList<String>();
        thoseTrust.add("EX");
        thoseTrust.add("EX");
        thoseTrust.add("EX");
        thoseTrust.add("EX");
        thoseTrust.add("EX");

        List<String> youTrust = new ArrayList<String>();
        youTrust.add("EX");
        youTrust.add("EX");
        youTrust.add("EX");
        youTrust.add("EX");
        youTrust.add("EX");

        expandableListDetail.put("The ones who trust", thoseTrust);
        expandableListDetail.put("Those who trust you", youTrust);
        return expandableListDetail;
    }
}
