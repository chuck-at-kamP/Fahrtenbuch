package com.example.fahrtenbuch;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static boolean verifyTime(String str) {
        //str = str.trim();
        if(str == null || str.equals("")) return false;
        if(!str.matches("^[0-9.: ]+$")) return false;
        return true;
    }
    public static boolean verifyString(String str) {
        //str = str.trim();
        if(str == null || str.equals("")) return false;
        return true;
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    static public String createFilename(String dateTime) {
        Log.d("Track Timestamp","Timestamp " + dateTime);
        String tmp = dateTime;
        String[] dt = tmp.split(" ");
        String date = dt[0];
        String time = dt[1];
        Log.d("Track Timestamp", "Timestamp date=" + date + ", time=" + time);
        String newTime = time.replace(':','-');
        String[] dates = date.split("\\.");

        if(dates.length < 3) {
            Log.d("Track Timestamp","Size is less than 3! length=" + dates.length);
        }
        StringBuilder track = new StringBuilder();
        track.append(dates[2]).append("-").append(dates[1]).append("-").append(dates[0]).append("_").append(newTime).append(".gpx");
        String result = track.toString();
        Log.d("Track Timestamp","Filename: " + result);
        return result;
    }

    static public String[] getDate() {
        DateFormat dateFormat1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
        Date act = new Date();
        String currentGpxString = dateFormat2.format(act);
        String currentDateString = dateFormat1.format(act);
        Log.d("Util getDate()", "Date date=" + currentDateString + ", gpx=" + currentGpxString);
        String[] ret = { currentDateString, currentGpxString };
        return ret;
    }
    static public String timestamp(long diff) {
        long hours = diff / 3600;
        long minutes = (diff % 3600) / 60;
        long seconds = diff % 60;
        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return timeString;
    }

}
