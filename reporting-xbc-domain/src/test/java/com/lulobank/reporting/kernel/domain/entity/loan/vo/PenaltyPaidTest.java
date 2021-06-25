package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class PenaltyPaidTest {

	@Test
	public void shouldCreateFromString() {
		String value = "0.0";
		PenaltyPaid penaltyPaid = PenaltyPaid.buildFromString(value);
		Assert.assertThat(penaltyPaid.getValue().compareTo(new BigDecimal(value)), is(0)); 
	}
}
