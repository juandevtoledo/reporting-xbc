package com.lulobank.reporting.kernel.domain.entity.loan.vo;

import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class LoanState extends PrimitiveVo<String> {
    private static final String IN_ARREARS ="IN_ARREARS";

    public boolean isLoanInArrears(){
        return IN_ARREARS.equals(value);
    }

    public String getTextInSpanish(){
        return isLoanInArrears()?"en mora":"al dia";
    }
}
