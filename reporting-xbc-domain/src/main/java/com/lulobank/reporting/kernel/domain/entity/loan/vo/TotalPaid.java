package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
public class TotalPaid extends PrimitiveVo<BigDecimal> {
	
	public static TotalPaid buildFromString(String value) {
		return TotalPaid.builder()
				.value(new BigDecimal(value))
				.build();
	}
}
