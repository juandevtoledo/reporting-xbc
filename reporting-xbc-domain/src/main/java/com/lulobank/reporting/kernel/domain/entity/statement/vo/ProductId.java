package com.lulobank.reporting.kernel.domain.entity.statement.vo;

import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanId;
import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ProductId extends PrimitiveVo<String>{

    public static ProductId buildProductIdFromLoanId(LoanId loanId){
        return ProductId.builder().value(loanId.getValue()).build();
    }
}
