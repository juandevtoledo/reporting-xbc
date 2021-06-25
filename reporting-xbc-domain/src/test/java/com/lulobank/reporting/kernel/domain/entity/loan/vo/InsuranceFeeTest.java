package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import java.math.BigDecimal;

import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import org.junit.Test;

import com.lulobank.reporting.kernel.domain.entity.loan.vo.InsuranceFee;

public class InsuranceFeeTest {

	@Test
	public void shouldCreateFromString() {
		String value = "0.0";
		InsuranceFee insuranceFee = InsuranceFee.buildFromString(value);
		Assert.assertThat(insuranceFee.getValue().compareTo(new BigDecimal(value)), is(0)); 
	}
}
