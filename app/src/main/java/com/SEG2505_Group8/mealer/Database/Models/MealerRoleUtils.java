package com.SEG2505_Group8.mealer.Database.Models;

import android.content.Context;

import com.SEG2505_Group8.mealer.R;

public class MealerRoleUtils {
    public static String getPrettyRole(MealerRole role, Context context) {
        switch (role) {
            case ADMIN:
                return context.getString(R.string.role_admin);
            case USER:
                return context.getString(R.string.role_user);
            case CHEF:
                return context.getString(R.string.role_chef);
            default:
                return "Unknown Role";
        }
    }
}
