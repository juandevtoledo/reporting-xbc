package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
public class PenaltyRate extends PrimitiveVo<BigDecimal> {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    public BigDecimal convertToPercentValue(){
        return value.multiply(ONE_HUNDRED);
    }
}
