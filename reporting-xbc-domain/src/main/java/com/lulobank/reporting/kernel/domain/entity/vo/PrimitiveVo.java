package com.lulobank.reporting.kernel.domain.entity.vo;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public abstract class PrimitiveVo<T> {
    protected T value;
}
