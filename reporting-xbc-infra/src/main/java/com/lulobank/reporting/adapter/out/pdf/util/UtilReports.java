package com.lulobank.reporting.adapter.out.pdf.util;

import com.google.common.collect.ImmutableMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.Map;

public class UtilReports {

    private UtilReports() {
    }

    private static final Locale LOCALE_CO = new Locale("es", "CO");
    private static final Map<Long, String> symbolsMap = ImmutableMap.of(0L, "a.m.", 1L, "p.m.");

    public static String formatReportDate(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("MMMM dd 'de' yyyy '·' hh:mm ")
                .appendText(ChronoField.AMPM_OF_DAY, symbolsMap)
                .toFormatter(LOCALE_CO);
        return localDateTime.format(formatter);
    }
}
