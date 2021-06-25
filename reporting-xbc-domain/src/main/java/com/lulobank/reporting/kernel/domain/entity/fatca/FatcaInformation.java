package com.lulobank.reporting.kernel.domain.entity.fatca;

import com.lulobank.reporting.kernel.domain.entity.fatca.vo.Country;
import com.lulobank.reporting.kernel.domain.entity.fatca.vo.TaxLiability;
import com.lulobank.reporting.kernel.domain.entity.fatca.vo.TaxNumber;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FatcaInformation {
    private final TaxLiability taxLiability;
    private final Country country;
    private final TaxNumber taxNumber;
}
