package com.SEG2505_Group8.mealer.UI.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ActivityUtils {

    public static void launchActivity(Activity src, Context context, Class<?> activity) {
        // Create the intent
        Intent i = new Intent(context, activity);
        context.startActivity(i);

        // Kill our current intent
        src.finish();
    }
}
