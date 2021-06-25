package com.lulobank.reporting.kernel.domain.entity.loan.vo;


import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

@SuperBuilder
public class CutOffDate extends PrimitiveVo<LocalDate> {

    private static final Locale LOCALE_CO = new Locale("es", "CO");
    
    public String formatCutOffDate() {

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("MMMM dd '-' yyyy")
                .toFormatter(LOCALE_CO);
        String dateFormatted = value.format(formatter);
        return dateFormatted.replaceFirst("^(\\w)", dateFormatted.substring(0, 1).toUpperCase());
    }
    
    public static CutOffDate createFromString(String value) {
    	return CutOffDate.builder()
    			.value(LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy")))
    			.build();
    }

}
