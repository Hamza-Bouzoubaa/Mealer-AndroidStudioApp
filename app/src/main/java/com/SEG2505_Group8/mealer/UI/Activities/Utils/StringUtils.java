package com.SEG2505_Group8.mealer.UI.Activities.Utils;

public class StringUtils {
    public static String capitalize(String input) {

        if (input == null || input.equals("")) {
            return "";
        }

        return input.substring(0,1).toUpperCase() + input.substring(1);
    }
}
