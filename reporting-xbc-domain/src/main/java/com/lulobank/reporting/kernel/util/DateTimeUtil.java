package com.lulobank.reporting.kernel.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtil {

    private static final String SHORT_DATE_FORMAT = "d MMM yyyy";
    private static final String TIME_FORMAT = "h:mm a";
    private static final String UPPERCASE_AM = "AM";
    private static final Locale LOCALE_CO = new Locale("es", "CO");
    private static final String UPPERCASE_PM = "PM";
    private static final String LOWERCASE_AM = "a.m.";
    private static final String LOWERCASE_PM = "p.m.";


    public static String formatToShortDate(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(SHORT_DATE_FORMAT, LOCALE_CO);
        return localDateTime.format(dateTimeFormatter).replace(".","");
    }

    public static String formatToHour(LocalDateTime localDateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        return localDateTime.format(dateTimeFormatter)
                .replace(UPPERCASE_AM, LOWERCASE_AM)
                .replace(UPPERCASE_PM, LOWERCASE_PM)
                .replace(". ",".");
    }

}
