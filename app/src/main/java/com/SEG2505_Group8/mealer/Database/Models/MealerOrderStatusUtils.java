package com.SEG2505_Group8.mealer.Database.Models;

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

    public static String toString(MealerOrderStatus status) {
        switch (status) {
            case WAITING:
                return "WAITING";
            case ACCEPTED:
                return "ACCEPTED";
            case REJECTED:
                return "REJECTED";
            case COMPLETED:
                return "COMPLETED";
            default:
                throw new RuntimeException("Unknown status: " + status);
        }
    }

    private static List<String> hiddenOrderStatus;

    public static List<String> getHiddenOrderStatus() {
        if (hiddenOrderStatus == null) {
            hiddenOrderStatus = new ArrayList<>();
            hiddenOrderStatus.add(toString(MealerOrderStatus.COMPLETED));
            hiddenOrderStatus.add(toString(MealerOrderStatus.REJECTED));
        }

        return hiddenOrderStatus;
    }
}
