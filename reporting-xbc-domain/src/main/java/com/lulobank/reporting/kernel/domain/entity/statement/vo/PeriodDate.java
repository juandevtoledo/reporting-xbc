package com.lulobank.reporting.kernel.domain.entity.statement.vo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
@Builder
public class PeriodDate {

    private final String value;
    private static final Locale LOCALE_CO = new Locale("es", "CO");

    public String getPeriodDateWithName() {
        LocalDate date =getPreviousMonthByDate();
        return getMonthName(date).concat(" ").concat(String.valueOf(date.getYear()));
    }

    public LocalDate toLocalDate(){
        return parseToLocalDate();
    }

    private String getMonthName(LocalDate date) {
        String monthInLetters = date.getMonth().getDisplayName(TextStyle.FULL, LOCALE_CO);
        return monthInLetters.replaceFirst("^(\\w)", monthInLetters.substring(0,1).toUpperCase());
    }

    private LocalDate getPreviousMonthByDate() {
        return parseToLocalDate().minusMonths(1);
    }

    private LocalDate parseToLocalDate() {
        String strInstalmentDueDate = value + "-01";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(strInstalmentDueDate, formatter);
    }
}
