package com.moralesf.masquerade.java.Date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateParser {
    public static String dateToChatTime(String date){
        StringBuilder chat_time = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        try {

            Date new_date = format.parse(date);
            Calendar day = Calendar.getInstance();
            day.setTime(new_date);

            Calendar now = Calendar.getInstance();

            if(now.get(Calendar.YEAR) == day.get(Calendar.YEAR) &&
                    now.get(Calendar.DAY_OF_YEAR) != day.get(Calendar.DAY_OF_YEAR)){
                chat_time.append(formatDayMonth(day));
            }else if(now.get(Calendar.YEAR) != day.get(Calendar.YEAR)){
                chat_time.append(formatDayMonthYear(day));
            }

            chat_time.append(formatHour(day));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return chat_time.toString();
    }

    private static String formatHour(Calendar calendar){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)));
        sb.append(":");
        sb.append(String.format("%02d", calendar.get(Calendar.MINUTE)));
        return sb.toString();
    }

    private static String formatDayMonth(Calendar calendar){
        return new SimpleDateFormat("dd MMM ").format(calendar.getTime());
    }
    private static String formatDayMonthYear(Calendar calendar){
        return new SimpleDateFormat("dd MMM yyyy ").format(calendar.getTime());
    }
}
