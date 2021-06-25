package com.lulobank.reporting.kernel.domain.entity.digitalevidence.document;

import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation.FileInformation;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.DocumentName;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Document {

  private DocumentName documentName;
  private FileInformation documentInformation;

}
