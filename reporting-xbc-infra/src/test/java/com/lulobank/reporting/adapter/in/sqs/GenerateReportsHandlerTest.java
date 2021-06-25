package com.lulobank.reporting.adapter.in.sqs;

import com.lulobank.reporting.adapter.in.sqs.event.CreateStatementMessage;
import com.lulobank.reporting.adapter.in.sqs.handler.GenerateReportsHandler;
import com.lulobank.reporting.kernel.command.statement.GenerateCreditStatement;
import com.lulobank.reporting.kernel.exception.BuildPdfException;
import com.lulobank.reporting.usecase.statement.GenerateCreditStatementUseCase;
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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

public class GenerateReportsHandlerTest {

    @Mock
    private GenerateCreditStatementUseCase useCase;

    @Captor
    private ArgumentCaptor<GenerateCreditStatement> commandCaptor;

    private CreateStatementMessage createStatementMessage;

    private GenerateReportsHandler target;

    @Before
    public void setup() {
		Map<String, String> data = new HashMap<>();
		data.put("interestRate", "12.1");
		data.put("totalInstalments", "0");
		data.put("instalmentTotalDue", "0");
		data.put("instalmentPrincipalDue", "0");
		data.put("instalmentInterestDue", "0");
		data.put("instalmentPenaltiesDue", "0");
		data.put("inArrearsBalance", "0");
		data.put("insuranceFee", "0");
		data.put("legalExpenses", "0");
		data.put("currentInstalment", "0");
		data.put("totalBalance", "0");
		data.put("principalPaid", "0");
		data.put("loanAmount", "0");
		data.put("penaltyRate", "0");
		data.put("daysInArrears", "0");
        data.put("name", "name");
        data.put("middleName", "middleName");
        data.put("lastName", "lastName");
        data.put("secondSurname", "secondSurname");
        createStatementMessage = new CreateStatementMessage(Sample.ID_CLIENT,
                Sample.PRODUCT_TYPE,
                Sample.REPORT_TYPE,
                data);
        MockitoAnnotations.openMocks(this);
        target = new GenerateReportsHandler(useCase);
    }
    
    @Test
    public void useCaseReturnError() {
        when(useCase.execute(commandCaptor.capture())).thenReturn(Mono.error(new BuildPdfException("")));
        Mono<Void> response = target.execute(createStatementMessage);

        StepVerifier.create(response).verifyError();
        Assert.assertEquals(Sample.ID_CLIENT, commandCaptor.getValue().getClientId());
        Assert.assertEquals(Sample.PRODUCT_TYPE, commandCaptor.getValue().getProductType());
        Assert.assertEquals(Sample.REPORT_TYPE, commandCaptor.getValue().getReportType());
    }

    @Test
    public void useCaseReturnSuccess() {
        when(useCase.execute(commandCaptor.capture())).thenReturn(Mono.just(true));
        Mono<Void> response = target.execute(createStatementMessage);

        StepVerifier.create(response).verifyComplete();
        Assert.assertEquals(Sample.ID_CLIENT, commandCaptor.getValue().getClientId());
        Assert.assertEquals(Sample.PRODUCT_TYPE, commandCaptor.getValue().getProductType());
        Assert.assertEquals(Sample.REPORT_TYPE, commandCaptor.getValue().getReportType());
    }
}
