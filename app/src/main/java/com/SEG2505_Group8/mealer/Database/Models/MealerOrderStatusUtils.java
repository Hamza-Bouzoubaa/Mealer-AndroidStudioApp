package com.SEG2505_Group8.mealer.Database.Models;

import android.content.Context;

import com.SEG2505_Group8.mealer.R;

import java.util.ArrayList;
import java.util.List;

public class MealerOrderStatusUtils {
    public static int toStep(MealerOrderStatus status) {
        switch (status) {
            case WAITING:
                return 0;
            case ACCEPTED:
                return 1;
            case REJECTED:
            case COMPLETED:
                return 2;
        }

        return 0;
    }

    public static int getMaxStatusStep() {
        return 2;
    }

    public static String getPrettyStatus(MealerOrderStatus status, Context context) {
        switch (status) {
            case WAITING:
                return context.getString(R.string.status_waiting);
            case ACCEPTED:
                return context.getString(R.string.status_accepted);
            case COMPLETED:
                return context.getString(R.string.status_completed);
            case REJECTED:
                return context.getString(R.string.status_rejected);
            default:
                return "Unknown Status";
        }
    }

    private static List<String> hiddenOrderStatus;

    public static List<String> getHiddenOrderStatus() {
        if (hiddenOrderStatus == null) {
            hiddenOrderStatus = new ArrayList<>();
            hiddenOrderStatus.add(MealerOrderStatus.COMPLETED.toString());
            hiddenOrderStatus.add(MealerOrderStatus.REJECTED.toString());
        }

        return hiddenOrderStatus;
    }
}
