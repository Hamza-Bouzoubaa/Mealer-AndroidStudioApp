package com.SEG2505_Group8.mealer.Database.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingUtils {
    public static float averageRating(Map<String, List<String>> ratings) {

        if (ratings == null)
        {
            return 0;
        }

        float sum = 0;
        int c = 0;

        for (int i = 0; i < 5; i++) {
            int t = ratings.getOrDefault(String.valueOf(i), new ArrayList<>()).size();
            sum += (i + 1) * t;
            c += t;
        }

        return sum / c;
    }

    public static void rate(Map<String, List<String>> ratings, int rating, String userId) {
        if (!ratings.containsKey(String.valueOf(rating))) {
            ratings.put(String.valueOf(rating), new ArrayList<>());
        }

        try {
            ratings.get("0").remove(userId);
        } catch (NullPointerException e) {
            ratings.put("0", new ArrayList<>());
        }

        try {
            ratings.get("1").remove(userId);
        } catch (NullPointerException e) {
            ratings.put("1", new ArrayList<>());
        }
        try {
            ratings.get("2").remove(userId);
        } catch (NullPointerException e) {
            ratings.put("2", new ArrayList<>());
        }
        try {
            ratings.get("3").remove(userId);
        } catch (NullPointerException e) {
            ratings.put("3", new ArrayList<>());
        }
        try {
            ratings.get("4").remove(userId);
        } catch (NullPointerException e) {
            ratings.put("4", new ArrayList<>());
        }

        ratings.get(String.valueOf(rating)).add(userId);
    }
}
