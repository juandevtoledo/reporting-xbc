package com.lulobank.reporting.kernel.domain.entity.person.vo;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AcceptanceTimestamp {
    private final LocalDateTime value;
}
