package com.lulobank.reporting.kernel.domain.entity.report.vo;

import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;
import java.util.Map;

@SuperBuilder
public class ReportDate extends PrimitiveVo<LocalDateTime> {

    private static final Locale LOCALE_CO = new Locale("es", "CO");
    private static final Map<Long, String> symbolsMap = Map.of(0L, "a.m.", 1L, "p.m.");

    public String reportDateFormat() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("MMMM dd 'de' yyyy")
                .toFormatter(LOCALE_CO);
        String dateFormated = value.format(formatter);
        return dateFormated.replaceFirst("^(\\w)", dateFormated.substring(0,1).toUpperCase());
    }

    public String reportDateTimeFormat() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("MMMM dd 'de' yyyy '·' hh:mm ")
                .appendText(ChronoField.AMPM_OF_DAY, symbolsMap)
                .toFormatter(LOCALE_CO);
        String dateFormated = value.format(formatter);
        return dateFormated.replaceFirst("^(\\w)", dateFormated.substring(0,1).toUpperCase());
    }
}
