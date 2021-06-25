package com.lulobank.reporting.kernel.domain.entity.loan.vo;


import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
public class InsuranceFee extends PrimitiveVo<BigDecimal> {
	public static InsuranceFee buildFromString(String value) {
		return InsuranceFee.builder()
				.value(new BigDecimal(value))
				.build();
	}
}
