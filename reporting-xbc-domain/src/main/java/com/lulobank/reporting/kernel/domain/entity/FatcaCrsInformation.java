package com.lulobank.reporting.kernel.domain.entity;

import com.lulobank.reporting.kernel.domain.entity.fatca.vo.Country;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FatcaCrsInformation {
    private final String clientId;
    private final List<Country> countries;
}
