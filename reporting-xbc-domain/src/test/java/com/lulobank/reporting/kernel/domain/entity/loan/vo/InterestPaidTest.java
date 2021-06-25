package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class InterestPaidTest {

	@Test
	public void shouldCreateFromString() {
		String value = "0.0";
		InterestPaid interestPaid = InterestPaid.buildFromString(value);
		Assert.assertThat(interestPaid.getValue().compareTo(new BigDecimal(value)), is(0)); 
	}
}
