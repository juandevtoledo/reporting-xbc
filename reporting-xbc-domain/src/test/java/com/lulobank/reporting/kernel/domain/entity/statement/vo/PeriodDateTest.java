package com.lulobank.reporting.kernel.domain.entity.statement.vo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class PeriodDateTest {

    private PeriodDate target;

    private static final String PERIOD ="2020-09";

    @Before
    public void setup(){
        target = PeriodDate.builder()
                .value(PERIOD)
                .build();
    }

    @Test
    public void getPeriodDateTest(){
        Assert.assertEquals("Agosto 2020",target.getPeriodDateWithName());
    }
}
