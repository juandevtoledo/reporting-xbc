package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class TotalPaidTest {

	@Test
	public void shouldCreateFromString() {
		String value = "0.0";
		TotalPaid totalPaid = TotalPaid.buildFromString(value);
		Assert.assertThat(totalPaid.getValue().compareTo(new BigDecimal(value)), is(0)); 
	}
}
