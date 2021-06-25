package com.lulobank.reporting.kernel.domain.entity.filestatement.vo;

import com.lulobank.reporting.kernel.domain.entity.vo.PrimitiveVo;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class FileFullPath extends PrimitiveVo<String> {

    private final FileName fileName;

    public static FileFullPath buildFullPathByFileName(String path, FileName fileName) {
        return FileFullPath.builder().value(path.concat(fileName.getValue())).fileName(fileName).build();
    }
}
