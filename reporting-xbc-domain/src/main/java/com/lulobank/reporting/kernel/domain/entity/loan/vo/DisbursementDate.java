package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

@SuperBuilder
public class DisbursementDate  extends PrimitiveVo<LocalDateTime> {

    private static final Locale LOCALE_CO = new Locale("es", "CO");

    public String getDisbursementDateFormat(){
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("dd 'de' MMMM 'de' yyyy")
                .toFormatter(LOCALE_CO);
        return value.format(formatter);
    }
    
    public static DisbursementDate createFromString(String value) {
    	return DisbursementDate.builder()
    			.value(LocalDateTime.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm:ss")))
    			.build();
    }
}
