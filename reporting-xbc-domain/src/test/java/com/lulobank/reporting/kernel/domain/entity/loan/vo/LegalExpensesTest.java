package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class LegalExpensesTest {

	@Test
	public void shouldCreateFromString() {
		String value = "0.0";
		LegalExpenses legalExpenses = LegalExpenses.buildFromString(value);
		Assert.assertThat(legalExpenses.getValue().compareTo(new BigDecimal(value)), is(0)); 
	}
}
