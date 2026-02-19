package me.waterarchery.litsellchest.utils;

import com.chickennw.utils.utils.ConfigUtils;
import me.waterarchery.litsellchest.configuration.config.ConfigFile;
import me.waterarchery.litsellchest.configuration.config.LangFile;

import java.text.DecimalFormat;

public class LangUtils {

    public static String formatNumber(double number) {
        ConfigFile config = ConfigUtils.get(ConfigFile.class);
        String formater = config.getNumberFormatter();

        DecimalFormat df = new DecimalFormat(formater);
        return df.format(number);
    }

    public static String formatTime(long number) {
        long days = number / 86400;
        long hours = number % 86400 / 3600;
        long minutes = number % 3600 / 60;
        long seconds = number % 60;

        LangFile lang = ConfigUtils.get(LangFile.class);
        String daysText = days + (days == 1 ? lang.getTimeParser().getDay() : lang.getTimeParser().getDays());
        String hoursText = hours + (hours == 1 ? lang.getTimeParser().getHour() : lang.getTimeParser().getHours());
        String minutesText = minutes + (minutes == 1 ? lang.getTimeParser().getMinute() : lang.getTimeParser().getMinutes());
        String secondsText = seconds + (seconds == 1 ? lang.getTimeParser().getSecond() : lang.getTimeParser().getSeconds());

        String value = "";
        if (days > 0) value += daysText + " ";
        if (hours > 0 || minutes > 0 || seconds > 0) value += hoursText + " ";
        if (minutes > 0 || seconds > 0) value += minutesText + " ";
        if (seconds > 0) value += secondsText;
        return value;
    }
}
