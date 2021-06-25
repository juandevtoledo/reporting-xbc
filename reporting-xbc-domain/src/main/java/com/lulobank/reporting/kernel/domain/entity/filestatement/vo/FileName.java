package com.lulobank.reporting.kernel.domain.entity.filestatement.vo;

import com.lulobank.reporting.kernel.domain.entity.fatca.vo.Country;
import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class FileName extends PrimitiveVo<String> {

    public static final String SEPARATOR = "-";

    public static FileName buildFileNamePDFStatement(String productType, String idProductNumber, String dateTime) {
        return FileName.builder()
                .value(productType.toLowerCase().concat(SEPARATOR).concat(idProductNumber).concat(
                        dateTime.isEmpty() ? dateTime : SEPARATOR.concat(dateTime)).concat(".pdf"))
                .build();
    }

    public static FileName buildFileNameFatca(String extension, Country country) {
        return FileName.builder()
                .value("autocertificacion_".concat(country.getValue()).concat(".").concat(extension))
                .build();
    }
}
