package com.lulobank.reporting.adapter.out.pdf;

import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.lulobank.reporting.kernel.domain.entity.person.ClientInformation;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientIdCBS;
import com.lulobank.reporting.kernel.exception.BuildPdfException;
import com.lulobank.reporting.usecase.port.out.BuilderFileService;
import com.lulobank.reporting.usecase.port.out.SignerFileService;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lulobank.reporting.adapter.out.pdf.util.UtilReports.formatReportDate;
import static org.apache.commons.lang3.StringUtils.SPACE;

@CustomLog
@RequiredArgsConstructor
public class SignerPDFAdapter implements SignerFileService {

    private final String ERROR_MESSAGE = "Error trying to signer PDF {0}";
    private static final String SIGNATURE_TEMPLATE = "sign";

    private final BuilderFileService builderFileService;

    public Mono<ByteArrayOutputStream> singFile(ByteArrayOutputStream pdfOutputStream, ClientIdCBS clientIdCBS) {

        return Mono.using(ByteArrayOutputStream::new,
                byteArrayOutputStream -> setPasswordToPdf(pdfOutputStream, clientIdCBS.getValue(), byteArrayOutputStream),
                byteArrayOutputStream -> Try.run(byteArrayOutputStream::close)
                        .onFailure(e -> log.error("Error closing byteArrayOutputStream {}")))
                .filter(Objects::nonNull)
                .onErrorResume(e-> Mono.error(new BuildPdfException(MessageFormat.format(ERROR_MESSAGE,e))))
                .switchIfEmpty( Mono.error(new BuildPdfException(ERROR_MESSAGE)))
                .doOnError(e -> log.error(e.getMessage(), e));
    }

    @Override
    public Mono<File> addSignaturePage(File value, ClientInformation clientInformation) {
        return Mono.fromCallable(() -> {
            FileInputStream inputStream = new FileInputStream(value);
            PdfReader pdfReader = new PdfReader(inputStream);
            return new PdfDocument(pdfReader);
        }).flatMap(original -> getModifiedFile(clientInformation, original, value))
                .doOnError(e -> log.error(e.getMessage()))
                .onErrorMap(e -> new BuildPdfException(MessageFormat.format(ERROR_MESSAGE, e)))
                .switchIfEmpty(Mono.error(new BuildPdfException(ERROR_MESSAGE)));
    }

    private Mono<ByteArrayOutputStream> setPasswordToPdf(ByteArrayOutputStream pdfOutputStream, String password, ByteArrayOutputStream newPdfOutputStream){

        return Mono.fromCallable(()-> new PdfReader(new ByteArrayInputStream(pdfOutputStream.toByteArray())))
                .flatMap(pdfReader -> getWriterPropertiesPwd(password)
                        .map(writerProperties ->  new PdfDocument(pdfReader, new PdfWriter(newPdfOutputStream,writerProperties))))
                .flatMap(document -> Mono.fromCallable(()-> {
                    document.getWriter().flush();
                    document.close();
                    return newPdfOutputStream; }).publishOn(Schedulers.elastic())
                );
    }


    private Mono<WriterProperties> getWriterPropertiesPwd(String password) {

        return Mono.just(password)
                .map(pass -> pass.getBytes(StandardCharsets.UTF_8))
                .flatMap(this::setWriterProperties)
                .switchIfEmpty(Mono.just(new WriterProperties()));
    }

    private Mono<WriterProperties> setWriterProperties(byte[] bytes) {

        return Mono.just(new WriterProperties())
                .map(writerProperties -> writerProperties.setStandardEncryption(bytes, null,
                        EncryptionConstants.ALLOW_PRINTING,
                        EncryptionConstants.ENCRYPTION_AES_128));
    }

    private Mono<File> getModifiedFile(ClientInformation clientInformation, PdfDocument original, File file) {
        return generateSignDocument(clientInformation)
                .flatMap(byteArrayOutputStream -> Mono.fromCallable(() -> {
                    PdfDocument sign = new PdfDocument(new PdfReader(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
                    File newFile = new File(file.getAbsolutePath());
                    PdfDocument dest = new PdfDocument(new PdfWriter(new FileOutputStream(newFile)));
                    original.copyPagesTo(1, original.getNumberOfPages(), dest);
                    sign.copyPagesTo(1, sign.getNumberOfPages(), dest);
                    dest.getWriter().flush();
                    dest.close();
                    return newFile;
                }));
    }
    private Mono<ByteArrayOutputStream> generateSignDocument(ClientInformation clientInformation) {
        return Mono.just(getClientInfoMap(clientInformation))
                .flatMap(map -> builderFileService.buildFile(SIGNATURE_TEMPLATE, map));
    }

    private Map<String, Object> getClientInfoMap(ClientInformation clientInformation) {

            HashMap<String, Object> map = new HashMap<>();
            map.put("clientName", formatName(clientInformation.getFirstName().getValue()));
            map.put("clientLastName", formatName(clientInformation.getSurname().getValue()));
            map.put("clientIdentification", clientInformation.getCardId().getValue());
            map.put("currentDate",
                    createDateInFormat(Option.of(clientInformation.getAcceptanceTimestamp().getValue())
                            .getOrElse(LocalDateTime.now())));
            return map;
    }


    private String formatName(String name) {
        return Stream.of(name.split(" "))
                .map(value-> StringUtils.capitalize(value.toLowerCase()))
                .map(value->value.concat(SPACE))
                .collect(Collectors.joining())
                .trim();
    }

    private String createDateInFormat(LocalDateTime time) {
        return StringUtils.capitalize(formatReportDate(time));
    }


}
