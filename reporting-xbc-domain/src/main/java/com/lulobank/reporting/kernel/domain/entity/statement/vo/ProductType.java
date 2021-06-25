package com.lulobank.reporting.kernel.domain.entity.statement.vo;

import com.lulobank.reporting.kernel.domain.entity.types.ProductTypes;
import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ProductType extends PrimitiveVo<String> {

    public static ProductType buildProductLoan(){
        return ProductType.builder().value(ProductTypes.LOAN_ACCOUNT.name()).build();
    }

}
