package com.lulobank.reporting.adapter.in.util;

import com.lulobank.reporting.kernel.exception.BuildHashFileException;
import com.lulobank.reporting.kernel.exception.BuildPdfException;
import com.lulobank.reporting.kernel.exception.LocalFileException;
import com.lulobank.reporting.kernel.exception.RepositoryException;
import com.lulobank.reporting.kernel.exception.StorageFileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.lulobank.reporting.kernel.domain.error.ReportingErrorCode.DATA_BASE_ERROR;
import static com.lulobank.reporting.kernel.domain.error.ReportingErrorCode.FILE_SERVICE_ERROR;
import static com.lulobank.reporting.kernel.domain.error.ReportingErrorCode.PROCESS_FILE_ERROR;

@RestControllerAdvice
public class InboundAdapterControllerAdvice {

    private static final String ERROR = "500";

    @ExceptionHandler(StorageFileException.class)
    @ResponseBody
    public ResponseEntity<GenericResponse> storageFileException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .code(FILE_SERVICE_ERROR.getCode())
                        .failure(ERROR)
                        .detail(FILE_SERVICE_ERROR.getDetail())
                        .build());
    }

    @ExceptionHandler(LocalFileException.class)
    @ResponseBody
    public ResponseEntity<GenericResponse> localFileException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .code(FILE_SERVICE_ERROR.getCode())
                        .failure(ERROR)
                        .detail(FILE_SERVICE_ERROR.getDetail())
                        .build());
    }


    @ExceptionHandler(BuildPdfException.class)
    @ResponseBody
    public ResponseEntity<GenericResponse> buildPdfException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .code(PROCESS_FILE_ERROR.getCode())
                        .failure(ERROR)
                        .detail(PROCESS_FILE_ERROR.getDetail())
                        .build());
    }

    @ExceptionHandler(BuildHashFileException.class)
    @ResponseBody
    public ResponseEntity<GenericResponse> buildHashFileException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .code(PROCESS_FILE_ERROR.getCode())
                        .failure(ERROR)
                        .detail(PROCESS_FILE_ERROR.getDetail())
                        .build());
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseBody
    public ResponseEntity<GenericResponse> repositoryException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorResponse.builder()
                        .code(DATA_BASE_ERROR.getCode())
                        .failure(ERROR)
                        .detail(DATA_BASE_ERROR.getDetail())
                        .build());
    }

}