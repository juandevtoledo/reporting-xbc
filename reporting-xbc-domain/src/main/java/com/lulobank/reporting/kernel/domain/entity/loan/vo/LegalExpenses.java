package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
public class LegalExpenses extends PrimitiveVo<BigDecimal> {
	public static LegalExpenses buildFromString(String value) {
		return LegalExpenses.builder()
				.value(new BigDecimal(value))
				.build();
	}
}
