package com.SEG2505_Group8.mealer.UI.Activities.Utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class DateUtils {

    @SuppressLint("SimpleDateFormat")
    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

    public static Date toDate(String isoString) throws ParseException {
        return format.parse(isoString);
    }

    public static String toString(Date date) {
        return format.format(date);
    }

    public static String toPrettyString(String isoString) throws ParseException {
        return toDate(isoString).toString();
    }

    public static String nowPrettyString() {
        return new Date().toString();
    }

    /**
     * Returns true if date has already passed.
     * Returns false if date provided is invalid.
     * @param isoString
     * @return
     */
    public static boolean isPassed(String isoString) {
        try {
            Date d = toDate(isoString);
            return d.before(new Date());
        } catch (ParseException e) {
            return false;
        }
    }
}
