package com.SEG2505_Group8.mealer.UI.Activities.Utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Standardize date format
 */
public class DateUtils {

    // ISO String date format
    @SuppressLint("SimpleDateFormat")
    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

    /**
     * Convert isoString into a Date object following DateUtils default format
     * @param isoString
     * @return
     * @throws ParseException
     */
    public static Date toDate(String isoString) throws ParseException {
        return format.parse(isoString);
    }

    /**
     * Convert date into a string using the DataUtils default format
     * @param date
     * @return
     */
    public static String toString(Date date) {
        return format.format(date);
    }

    /**
     * Convert isoString to user readable string using DateUtils default format.
     * @param isoString
     * @return
     * @throws ParseException
     */
    public static String toPrettyString(String isoString) throws ParseException {
        return toDate(isoString).toString();
    }

    /**
     * Get current time as human readable string following DateUtils default format.
     * @return
     */
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
