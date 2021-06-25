package com.lulobank.reporting.kernel.domain.entity;

import com.lulobank.reporting.utils.Sample;
import org.junit.Assert;
import org.junit.Test;

public class CreditStatementAdditionalInformationTest {

    private CreditStatementInformation creditStatementInformation;

    @Test
    public void buildCreditStatementInformation(){
        creditStatementInformation = CreditStatementInformation
                .buildByLoanInformationAndCommand(Sample.getGenerateCreditStatement());

        Assert.assertEquals(Sample.ID_CLIENT,  creditStatementInformation.getClientInformation().getClientId().getValue());
        Assert.assertEquals(Sample.NAME,  creditStatementInformation.getClientInformation().getFirstName().getValue());
        Assert.assertEquals(Sample.MIDDLE_NAME,  creditStatementInformation.getClientInformation().getMiddleName().getValue().get());
        Assert.assertEquals(Sample.LAST_NAME,  creditStatementInformation.getClientInformation().getSurname().getValue());
        Assert.assertEquals(Sample.SECOND_SURNAME,  creditStatementInformation.getClientInformation().getSecondSurname().getValue().get());
        Assert.assertEquals(Sample.ID_CLIENT_CBS,  creditStatementInformation.getClientInformation().getClientIdCBS().getValue());
        Assert.assertEquals(Sample.EMAIL,  creditStatementInformation.getClientInformation().getEmail().getValue());

        Assert.assertEquals(Boolean.getBoolean(Sample.AUTOMATIC_DEBIT),  creditStatementInformation.getAdditionalLoanInformation().getAutomaticDebit().getValue());
        Assert.assertEquals(Sample.INTEREST_RATE,  creditStatementInformation.getAdditionalLoanInformation().getInterestRate().getValue());
        Assert.assertEquals(Sample.ID_CREDIT_CBS,  creditStatementInformation.getAdditionalLoanInformation().getLoanId().getValue());

        Assert.assertEquals(Sample.CURRENT_INSTALMENT,  creditStatementInformation.getLoanInformation().getCurrentPeriod().getCurrentInstalment().getValue());
        Assert.assertEquals(Sample.DISBURSEMENT_DATE,  creditStatementInformation.getLoanInformation().getDisbursementDate().getValue());
        Assert.assertEquals(Sample.INSTALMENT_TOTAL_DUE,  creditStatementInformation.getLoanInformation().getCurrentPeriod().getInstalmentTotalDue().getValue());
        Assert.assertEquals(Sample.INSTALMENT_DUE_DATE,  creditStatementInformation.getLoanInformation().getCurrentPeriod().getInstalmentDueDate().getValue());
        Assert.assertEquals(Sample.INSTALMENT_INTEREST_DUE,  creditStatementInformation.getLoanInformation().getCurrentPeriod().getInstalmentInterestDue().getValue());
        Assert.assertEquals(Sample.INSTALMENT_PRINCIPAL_DUE,  creditStatementInformation.getLoanInformation().getCurrentPeriod().getInstalmentPrincipalDue().getValue());
        Assert.assertEquals(Sample.LOAN_AMOUNT,  creditStatementInformation.getLoanInformation().getLoanAmount().getValue());
        Assert.assertEquals(Sample.TOTAL_BALANCE,  creditStatementInformation.getLoanInformation().getTotalBalance().getValue());
        Assert.assertEquals(Sample.TOTAL_INSTALMENTS,  creditStatementInformation.getLoanInformation().getTotalInstalments().getValue());

        Assert.assertEquals(Sample.STATEMENT_DATE,  creditStatementInformation.getStatementAdditionalInformation().getPeriodDate().getValue());
        Assert.assertEquals(Sample.PRODUCT_TYPE,  creditStatementInformation.getStatementAdditionalInformation().getProductType().getValue());

    }
}
