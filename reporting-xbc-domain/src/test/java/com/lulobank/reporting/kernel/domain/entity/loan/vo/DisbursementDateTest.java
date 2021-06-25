package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

public class DisbursementDateTest {

    private DisbursementDate target;

    private static final String date ="2020-06-28T10:36:13";

    @Before
    public void setup(){
        target = DisbursementDate.builder().value(LocalDateTime.parse(date)).build();
    }

    @Test
    public void getDisbursementDateFormatTest(){
        Assert.assertEquals("28 de junio de 2020",target.getDisbursementDateFormat());
    }
}
