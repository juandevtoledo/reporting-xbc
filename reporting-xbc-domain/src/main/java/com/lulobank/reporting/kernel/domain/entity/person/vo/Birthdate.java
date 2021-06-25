package com.lulobank.reporting.kernel.domain.entity.person.vo;

import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

@SuperBuilder
public class Birthdate extends PrimitiveVo<LocalDate> {

    private static final Locale LOCALE_CO = new Locale("es", "CO");

    public String birthdateFormat() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("dd/MM/yyyy")
                .toFormatter(LOCALE_CO);
        return value.format(formatter);
    }
}
