package com.lulobank.reporting.adapter.out.pdf;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.lulobank.reporting.adapter.out.pdf.util.UtilReports;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientIdCBS;
import com.lulobank.reporting.kernel.exception.BuildPdfException;
import com.lulobank.reporting.usecase.port.out.BuilderFileService;
import com.lulobank.reporting.utils.Sample;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static com.lulobank.reporting.utils.Sample.ACCEPTANCE_DATE;
import static com.lulobank.reporting.utils.Sample.CARD_ID;
import static com.lulobank.reporting.utils.Sample.LAST_NAME;
import static com.lulobank.reporting.utils.Sample.NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SignerPDFAdapterTest {

    private static final String SAMPLE_PDF = "file-sample/sample.pdf";

    private SignerPDFAdapter target;

    private File filePdf;

    private int pagesFilePdf;

    @Captor
    private ArgumentCaptor<Map<String, Object>> hashMapArgumentCaptor;

    @Mock
    private BuilderFileService builderFileService;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        ClassLoader classLoader = getClass().getClassLoader();
        filePdf = new File(classLoader.getResource(SAMPLE_PDF).getFile());
        FileInputStream inputStream = new FileInputStream(filePdf);
        PdfReader pdfReader = new PdfReader(inputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfReader);
        pagesFilePdf = pdfDocument.getNumberOfPages();


        target =new SignerPDFAdapter(builderFileService);
    }

    @Test(expected = BuildPdfException.class)
    public void byteArrayOutputStreamInvalid() {
        Mono<ByteArrayOutputStream> response = target.singFile(new ByteArrayOutputStream(), ClientIdCBS.builder().value(Sample.ID_CLIENT_CBS).build());
        response.block();

    }

    @Test
    public void byteArrayValid() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        HtmlConverter.convertToPdf("<html><body>prueba</body></html>", byteArrayOutputStream);
        Mono<ByteArrayOutputStream> response = target.singFile(byteArrayOutputStream, ClientIdCBS.builder().value(Sample.ID_CLIENT_CBS).build());

        Assert.assertNotNull(response.block());
        Assert.assertTrue(response.block().size()>0);
    }

    @Test
    public void addSignaturePageErrorBuildPage() {

        when(builderFileService.buildFile(any(), any())).thenReturn(Mono.error(new BuildPdfException("")));

        Mono<File> response = target.addSignaturePage(filePdf, Sample.getClientInformation());
        StepVerifier
                .create(response)
                .expectError(BuildPdfException.class)
                .verify();

    }

    @Test
    public void addSignaturePageOk() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf("<html><body>prueba</body></html>", byteArrayOutputStream);
        when(builderFileService.buildFile(any(), hashMapArgumentCaptor.capture())).thenReturn(Mono.just(byteArrayOutputStream));

        Mono<File> response = target.addSignaturePage(filePdf, Sample.getClientInformation());
        StepVerifier
                .create(response)
                .expectNextMatches(file -> Try.of(() -> compareNumberPages(file)).get())
                .verifyComplete();
        assertEquals(StringUtils.capitalize(NAME.toLowerCase()), hashMapArgumentCaptor.getValue().get("clientName"));
        assertEquals(StringUtils.capitalize(LAST_NAME.toLowerCase()), hashMapArgumentCaptor.getValue().get("clientLastName"));
        assertEquals(CARD_ID, hashMapArgumentCaptor.getValue().get("clientIdentification"));
        assertEquals(StringUtils.capitalize(UtilReports.formatReportDate(ACCEPTANCE_DATE)),
                hashMapArgumentCaptor.getValue().get("currentDate"));

    }

    private boolean compareNumberPages(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        PdfReader pdfReader = new PdfReader(inputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfReader);
        return pagesFilePdf < pdfDocument.getNumberOfPages();
    }

}
