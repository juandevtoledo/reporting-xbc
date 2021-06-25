package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
public class PenaltyPaid extends PrimitiveVo<BigDecimal> {
	public static PenaltyPaid buildFromString(String value) {
		return PenaltyPaid.builder()
				.value(new BigDecimal(value))
				.build();
	}
}
