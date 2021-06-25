package com.lulobank.reporting.kernel.domain.entity.statement;

import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileName;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ContentBase64;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatementContent {
    private final ContentBase64 contentBase64;
    private final FileName fileName;

}
