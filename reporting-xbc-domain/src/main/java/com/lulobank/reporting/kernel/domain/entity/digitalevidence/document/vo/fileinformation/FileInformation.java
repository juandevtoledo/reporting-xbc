package com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation;

import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation.vo.AcceptanceTimestamp;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation.vo.Location;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileInformation {

    private AcceptanceTimestamp acceptanceTimestamp;
    private Location location;


}
