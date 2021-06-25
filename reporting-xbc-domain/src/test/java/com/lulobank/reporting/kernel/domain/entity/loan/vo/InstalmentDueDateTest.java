package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class InstalmentDueDateTest {

    private InstalmentDueDate target;

    private static final String period ="2020-09-21";

    @Before
    public void setup(){
        target = InstalmentDueDate.builder().value(LocalDate.parse(period)).build();
    }

    @Test
    public void getTimelyInstallmentPaymentDateTest(){
        Assert.assertEquals("Sept. 21, 2020",target.formatInstalmentDueDate());
    }
}
