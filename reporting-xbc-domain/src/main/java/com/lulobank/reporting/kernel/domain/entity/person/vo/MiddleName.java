package com.lulobank.reporting.kernel.domain.entity.person.vo;

import io.vavr.control.Option;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MiddleName {
    private final Option<String> value;
}
