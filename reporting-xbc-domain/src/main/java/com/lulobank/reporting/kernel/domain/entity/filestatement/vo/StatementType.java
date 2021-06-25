package com.lulobank.reporting.kernel.domain.entity.filestatement.vo;

import com.lulobank.reporting.kernel.domain.entity.types.StatementTypes;
import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class StatementType extends PrimitiveVo<String> {

    public static StatementType buildLoanStatement(){
        return StatementType.builder().value(StatementTypes.LOAN_STATEMENT.name()).build();
    }
}
