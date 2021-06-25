package com.lulobank.reporting.usecase.port.out.repository.error;

import com.lulobank.reporting.kernel.exception.UseCaseException;

public class StatementRepositoryException  extends UseCaseException {

    private StatementRepositoryException(StatementRepositoryErrorStatus pseErrorStatus, String providerCode) {
        super(pseErrorStatus.name(), providerCode, StatementRepositoryErrorStatus.DEFAULT_DETAIL);
    }

    public static StatementRepositoryException getDefaultError() {
        return new StatementRepositoryException(StatementRepositoryErrorStatus.RPX_100, "500");
    }

    public static StatementRepositoryException notFoundError() {
        return new StatementRepositoryException(StatementRepositoryErrorStatus.RPX_101, "404");
    }
}
