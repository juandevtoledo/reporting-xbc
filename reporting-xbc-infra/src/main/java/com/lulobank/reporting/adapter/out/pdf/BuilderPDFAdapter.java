package com.lulobank.reporting.adapter.out.pdf;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.DigitalEvidencePageInformation;
import com.lulobank.reporting.kernel.exception.BuildPdfException;
import com.lulobank.reporting.usecase.port.out.BuilderFileService;
import io.vavr.control.Try;
import lombok.CustomLog;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@CustomLog
public class BuilderPDFAdapter implements BuilderFileService {

    private static final String SIGNATURE_TEMPLATE = "sign";

    private final ITemplateEngine templateEngine;

    private static final String ERROR_MESSAGE ="Error trying to build PDF {0}";
    private static final String DIGITAL_EVIDENCE_ERROR_MESSAGE ="Error trying to add digital evidence page {0}";

    public BuilderPDFAdapter(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public Mono<ByteArrayOutputStream> buildFile(final String templateName, final Map<String, Object> map){

        return Mono.using(ByteArrayOutputStream::new,
                byteArrayOutputStream ->
                        fillInContext(map)
                                .map(context -> templateEngine.process(templateName, context))
                                .flatMap(processedHtml -> buildByteArrayOutputStreamByReport(byteArrayOutputStream, processedHtml)),
                byteArrayOutputStream -> Try.run(byteArrayOutputStream::close)
                        .onFailure(e -> log.error("Error closing byteArrayOutputStream {}", e.getMessage(), e)))
                .filter(Objects::nonNull)
                .onErrorResume(e-> Mono.error(new BuildPdfException(MessageFormat.format(ERROR_MESSAGE,e.getMessage()))))
                .switchIfEmpty( Mono.error(new BuildPdfException(ERROR_MESSAGE)))
                .doOnError(e -> log.error(e.getMessage()));

    }

    @Override
    public Mono<ByteArrayOutputStream> addDigitalEvidencePage(ByteArrayOutputStream report, DigitalEvidencePageInformation digitalEvidencePageInformation) {

     return Mono.using(ByteArrayOutputStream::new,
                byteArrayOutputStream -> Mono.using(() -> new PdfDocument(new PdfWriter(byteArrayOutputStream)),
                        pdfDocument -> Mono.using(() -> new PdfMerger(pdfDocument),
                                pdfMerge -> Mono.using(() -> new PdfDocument(new PdfReader(new ByteArrayInputStream(report.toByteArray()))),
                                        pdfReport ->generateSignDocument(digitalEvidencePageInformation)
                                                .flatMap(digitalEvidencePage -> Mono.using(() -> new PdfDocument(new PdfReader(new ByteArrayInputStream(digitalEvidencePage.toByteArray()))),
                                                        pdfDigitalEvidence -> Mono.fromCallable(() -> {
                                                            pdfMerge.merge(pdfReport, 1, pdfReport.getNumberOfPages());
                                                            pdfMerge.merge(pdfDigitalEvidence, 1, pdfDigitalEvidence.getNumberOfPages());
                                                            return byteArrayOutputStream;
                                                        }),
                                                        pdfDigitalEvidence -> pdfDigitalEvidence.close())),
                                        pdfReport -> pdfReport.close()),
                                pdfMerger -> pdfMerger.close()),
                        pdfDocument -> pdfDocument.close()),
                byteArrayOutputStream -> Try.run(byteArrayOutputStream::close)
                        .onFailure(e -> log.error("Error closing byteArrayOutputStream {}", e.getMessage(), e)))
             .doOnError(e -> log.error(e.getMessage(), e))
             .onErrorResume(e-> Mono.error(new BuildPdfException(MessageFormat.format(DIGITAL_EVIDENCE_ERROR_MESSAGE,e.getMessage()))))
             .switchIfEmpty( Mono.error(new BuildPdfException(DIGITAL_EVIDENCE_ERROR_MESSAGE)));
    }

    private Mono<ByteArrayOutputStream> generateSignDocument(DigitalEvidencePageInformation digitalEvidencePageInformation) {
        return Mono.just(getClientInfoMap(digitalEvidencePageInformation))
                .flatMap(map -> buildFile(SIGNATURE_TEMPLATE, map));
    }

    private Map<String, Object> getClientInfoMap(DigitalEvidencePageInformation digitalEvidencePageInformation) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("clientName", digitalEvidencePageInformation.getNames());
        map.put("clientLastName", digitalEvidencePageInformation.getLastNames());
        map.put("clientIdentification", digitalEvidencePageInformation.getCardId().getValue());
        map.put("currentDate", digitalEvidencePageInformation.getReportDate().reportDateTimeFormat());
        return map;
    }

    private Mono<ByteArrayOutputStream> buildByteArrayOutputStreamByReport(ByteArrayOutputStream byteArrayOutputStream, String processedHtml) {

        return Mono.fromCallable(()->{
            HtmlConverter.convertToPdf(processedHtml, byteArrayOutputStream);
            return byteArrayOutputStream;
        }).publishOn(Schedulers.elastic());
    }

    private Mono<Context> fillInContext(final Map<String, Object> map){
        return Mono.fromCallable(()->{
            Context context =new Context();
            context.setVariables(map);
            return context;
        });
    }

}
