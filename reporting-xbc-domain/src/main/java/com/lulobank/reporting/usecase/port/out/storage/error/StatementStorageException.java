package com.lulobank.reporting.usecase.port.out.storage.error;

import com.lulobank.reporting.kernel.exception.UseCaseException;

public class StatementStorageException extends UseCaseException {

    private StatementStorageException(StatementStorageErrorStatus pseErrorStatus, String providerCode) {
        super(pseErrorStatus.name(), providerCode, StatementStorageErrorStatus.DEFAULT_DETAIL);
    }

    public static StatementStorageException defaultStorageServiceError() {
        return new StatementStorageException(StatementStorageErrorStatus.RPX_102, "500");
    }

    public static StatementStorageException notFountError() {
        return new StatementStorageException(StatementStorageErrorStatus.RPX_103, "404");
    }
}
