package com.example.trackerapp.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        return dateFormatter.format(date);
    }
}
