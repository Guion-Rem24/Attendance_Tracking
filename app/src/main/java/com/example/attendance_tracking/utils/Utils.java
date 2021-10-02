package com.example.attendance_tracking.utils;

import android.util.Log;

public class Utils {
    static public String
    toWorkDayString(int startHour,int startMinute,int endHour,int endMinute){
        return String.format("%02d%02d/%02d%02d",startHour,startMinute,endHour,endMinute);
    }
    static public String
    toWorkDayString(String[] daytime){
        if(daytime.length < 7) return "";
        StringBuilder str = new StringBuilder(daytime[0]);
        for(int i=1;i<daytime.length;++i){
            str.append(String.format("#%s", daytime[i]));
        }
        Log.d("Utils", str.toString());
        return str.toString();
    }

    static public String[]
    splitToWorkDays(final String timeString){
        return timeString.split("#",-1);
    }
}
