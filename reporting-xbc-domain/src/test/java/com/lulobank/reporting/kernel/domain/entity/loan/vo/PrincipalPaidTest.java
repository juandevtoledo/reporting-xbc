package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class PrincipalPaidTest {

	@Test
	public void shouldCreateFromString() {
		String value = "0.0";
		PrincipalPaid principalPaid = PrincipalPaid.buildFromString(value);
		Assert.assertThat(principalPaid.getValue().compareTo(new BigDecimal(value)), is(0)); 
	}
}
