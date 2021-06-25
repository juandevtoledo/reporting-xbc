package com.lulobank.reporting.kernel.domain.entity.statement;

import com.lulobank.reporting.kernel.domain.entity.filestatement.StatementFileInformation;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Builder
@Getter
public class Statement {

    private final ClientId clientId;
    private final ProductType productType;
    private final ProductId productId;
    private final List<StatementFileInformation> listFiles;

}
