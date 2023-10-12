package com.example.cyclecare;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeStamp {

    public static String convertTimestampToDateTime(long timestamp) {
        try {
            // Create a Date object using the timestamp
            Date date = new Date(timestamp);

            // Create a SimpleDateFormat object with the desired format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            // Format the date and time
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error converting timestamp";
        }
    }
}



