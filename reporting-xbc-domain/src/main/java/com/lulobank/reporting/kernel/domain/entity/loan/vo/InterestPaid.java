package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
public class InterestPaid  extends PrimitiveVo<BigDecimal> {
	public static InterestPaid buildFromString(String value) {
		return InterestPaid.builder()
				.value(new BigDecimal(value))
				.build();
	}
}
