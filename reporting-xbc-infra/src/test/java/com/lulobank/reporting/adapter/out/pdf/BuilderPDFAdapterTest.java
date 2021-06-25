package com.lulobank.reporting.adapter.out.pdf;

import com.lulobank.reporting.kernel.exception.BuildPdfException;
import com.lulobank.reporting.utils.Sample;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.ITemplateEngine;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BuilderPDFAdapterTest {

    @Mock
    private ITemplateEngine templateEngine;

    private BuilderPDFAdapter target;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        target = new BuilderPDFAdapter(templateEngine);
    }


    @Test(expected = BuildPdfException.class)
    public void incompleteMap(){
        Mono<ByteArrayOutputStream> response = target.buildFile(Sample.PRODUCT_TYPE, Map.of());
        response.block();
    }

    @Test(expected = BuildPdfException.class)
    public void correctInformationButFileIsNotValidHTML(){
        when(templateEngine.process(anyString(),any())).thenReturn("<html");
        Mono<ByteArrayOutputStream> response = target.buildFile(Sample.REPORT_TYPE,Sample.buildMapForCreditStatement());
        response.block();
    }

    @Test
    public void correctInformation(){
        when(templateEngine.process(anyString(),any())).thenReturn("<html><body>prueba</body></html>");
        Mono<ByteArrayOutputStream> response = target.buildFile(Sample.REPORT_TYPE,Sample.buildMapForCreditStatement());
        response.block();
        Assert.assertNotNull(response.block());
        Assert.assertTrue(response.block().size()>0);
    }
}
