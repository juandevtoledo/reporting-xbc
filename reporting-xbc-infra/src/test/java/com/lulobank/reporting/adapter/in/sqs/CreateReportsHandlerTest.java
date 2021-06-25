package com.lulobank.reporting.adapter.in.sqs;

import com.lulobank.reporting.adapter.in.sqs.event.CreateReportMessage;
import com.lulobank.reporting.adapter.in.sqs.handler.CreateReportsHandler;
import com.lulobank.reporting.kernel.command.statement.CreateReport;
import com.lulobank.reporting.kernel.exception.BuildPdfException;
import com.lulobank.reporting.usecase.report.fatca.CreateFatcaReportUseCase;
import com.lulobank.reporting.utils.Sample;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.Mockito.when;

public class CreateReportsHandlerTest {

    @Mock
    private CreateFatcaReportUseCase useCase;

    @Captor
    private ArgumentCaptor<CreateReport> commandCaptor;

    private CreateReportMessage createReportMessage;

    private CreateReportsHandler target;

    @Before
    public void setup(){
        createReportMessage =new CreateReportMessage();
        createReportMessage.setData(Map.of());
        createReportMessage.setIdClient(Sample.ID_CLIENT);
        createReportMessage.setProductType(Sample.PRODUCT_TYPE_FATCA);
        createReportMessage.setReportType(Sample.REPORT_TYPE_FATCA);
        MockitoAnnotations.openMocks(this);
        target=new CreateReportsHandler( Map.of(Sample.REPORT_TYPE_FATCA,useCase));
    }

    @Test
    public void useCaseReturnError(){
        when(useCase.execute(commandCaptor.capture())).thenReturn(Mono.error(new BuildPdfException("")));
        Mono<Void> response = target.execute(createReportMessage);

        StepVerifier.create(response).verifyError();
        Assert.assertEquals(Sample.ID_CLIENT,commandCaptor.getValue().getIdClient());
        Assert.assertEquals(Sample.PRODUCT_TYPE_FATCA,commandCaptor.getValue().getProductType());
        Assert.assertEquals(Sample.REPORT_TYPE_FATCA,commandCaptor.getValue().getReportType());
    }

    @Test
    public void useCaseReturnSuccess(){
        when(useCase.execute(commandCaptor.capture())).thenReturn(Mono.just(true));
        Mono<Void> response = target.execute(createReportMessage);

        StepVerifier.create(response).verifyComplete();
        Assert.assertEquals(Sample.ID_CLIENT,commandCaptor.getValue().getIdClient());
        Assert.assertEquals(Sample.PRODUCT_TYPE_FATCA,commandCaptor.getValue().getProductType());
        Assert.assertEquals(Sample.REPORT_TYPE_FATCA,commandCaptor.getValue().getReportType());
    }
}
