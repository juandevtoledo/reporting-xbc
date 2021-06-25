package com.lulobank.reporting.kernel.domain.entity.fatca.vo;

import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TaxLiability extends PrimitiveVo<Boolean> {

    public String getTaxLiability(){
        return value ? "Si" : "No";
    }
}
