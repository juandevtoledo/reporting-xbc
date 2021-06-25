package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
public class PrincipalPaid extends PrimitiveVo<BigDecimal> {
	
	public static PrincipalPaid buildFromString(String value) {
		return PrincipalPaid.builder()
				.value(new BigDecimal(value))
				.build();
	}
}
