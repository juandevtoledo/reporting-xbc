package com.lulobank.reporting.adapter.in.sqs.mapper;

import com.lulobank.reporting.adapter.in.sqs.event.CreateStatementMessage;
import com.lulobank.reporting.kernel.command.statement.GenerateCreditStatement;
import io.vavr.control.Option;

import java.math.BigDecimal;

import static org.unbescape.html.HtmlEscape.escapeHtml4;

public class CreateStatementMessageToCommand {

    private static final String SPACE =" ";
    private static final String EMPTY ="";

    private static final String NAME ="name";
    private static final String MIDDLE_NAME ="middleName";
    private static final String LAST_NAME ="lastName";
    private static final String SECOND_SURNAME ="secondSurname";
    private static final String ID_CLIENT_CBS ="idClientCBS";
    private static final String ID_CREDIT_CBS ="idCreditCBS";
    private static final String STATEMENT_DATE ="statementDate";
    private static final String AUTOMATIC_DEBIT ="automaticDebit";
    private static final String EMAIL ="email";
    private static final String INTEREST_RATE ="interestRate";
    
    private static final String TOTAL_INSTALMENTS ="totalInstalments";
    private static final String CUT_OFF_DATE ="cutOffDate";
    private static final String INSTALMENT_DUE_DATE ="instalmentDueDate";
    private static final String INSTALMENT_TOTAL_DUE ="instalmentTotalDue";
    private static final String INSTALMENT_PRINCIPAL_DUE ="instalmentPrincipalDue";
    private static final String INSTALMENT_INTEREST_DUE ="instalmentInterestDue";
    private static final String INSTALMENT_PENALTIES_DUE ="instalmentPenaltiesDue";
    private static final String IN_ARREARS_BALANCE ="inArrearsBalance";
    private static final String INSURANCE_FEE ="insuranceFee";
    private static final String LEGAL_EXPENSES ="legalExpenses";
    private static final String CURRENT_INSTALMENT ="currentInstalment";
    private static final String LAST_PERIOD_TOTAL_PAID ="lastPeriodTotalPaid";
    private static final String LAST_PERIOD_PRINCIPAL_PAID ="lastPeriodPrincipalPaid";
    private static final String LAST_PERIOD_INTEREST_PAID ="lastPeriodInterestPaid";
    private static final String LAST_PERIOD_PENALTY_PAID ="lastPeriodPenaltyPaid";
    private static final String LAST_PERIOD_INSURANCE_FEE ="lastPeriodInsuranceFee";
    private static final String LAST_PERIOD_LEGAL_EXPENSES ="lastPeriodLegalExpenses";
    private static final String TOTAL_BALANCE ="totalBalance";
    private static final String PRINCIPAL_PAID ="principalPaid";
    private static final String LOAN_AMOUNT ="loanAmount";
    private static final String DISBURSEMENT_DATE ="disbursementDate";
    private static final String PENALTY_RATE ="penaltyRate";
    private static final String AMORTIZATION ="amortization";
    private static final String DAYS_IN_ARREARS ="daysInArrears";
    private static final String LOAN_STATE ="loanState";

    private CreateStatementMessageToCommand(){

    }

    public static GenerateCreditStatement toGenerateCreditStatement(CreateStatementMessage message) {

        return GenerateCreditStatement.builder()
                .clientId(message.getIdClient())
                .productType(message.getProductType())
                .reportType(message.getReportType())
                .name(message.getData().get(NAME))
                .middleName(Option.of(message.getData().get(MIDDLE_NAME)))
                .lastName(message.getData().get(LAST_NAME))
                .secondSurname(Option.of(message.getData().get(SECOND_SURNAME)))
                .fullName(escapeHtml4(message.getData().get(NAME)
                        .concat(Option.of(message.getData().get(MIDDLE_NAME)).fold(()->EMPTY, SPACE::concat))
                        .concat(SPACE).concat(message.getData().get(LAST_NAME))
                        .concat(Option.of(message.getData().get(SECOND_SURNAME)).fold(()->EMPTY, SPACE::concat))))
                .idClientCBS(message.getData().get(ID_CLIENT_CBS))
                .idCreditCBS(message.getData().get(ID_CREDIT_CBS))
                .statementDate(message.getData().get(STATEMENT_DATE))
                .automaticDebit(message.getData().get(AUTOMATIC_DEBIT))
                .email(message.getData().get(EMAIL))
                .interestRate(new BigDecimal(message.getData().get(INTEREST_RATE)))
                .totalInstalments(Integer.valueOf(message.getData().get(TOTAL_INSTALMENTS)))
                .cutOffDate(message.getData().get(CUT_OFF_DATE))
                .instalmentDueDate(message.getData().get(INSTALMENT_DUE_DATE))
                .instalmentTotalDue(new BigDecimal(message.getData().get(INSTALMENT_TOTAL_DUE)))
                .instalmentPrincipalDue(new BigDecimal(message.getData().get(INSTALMENT_PRINCIPAL_DUE)))
                .instalmentInterestDue(new BigDecimal(message.getData().get(INSTALMENT_INTEREST_DUE)))
                .instalmentPenaltiesDue(new BigDecimal(message.getData().get(INSTALMENT_PENALTIES_DUE)))
                .inArrearsBalance(new BigDecimal(message.getData().get(IN_ARREARS_BALANCE)))
                .insuranceFee(new BigDecimal(message.getData().get(INSURANCE_FEE)))
                .legalExpenses(new BigDecimal(message.getData().get(LEGAL_EXPENSES)))
                .currentInstalment(Integer.valueOf(message.getData().get(CURRENT_INSTALMENT)))
                .lastPeriodTotalPaid(Option.of(message.getData().get(LAST_PERIOD_TOTAL_PAID)))
                .lastPeriodPrincipalPaid(Option.of(message.getData().get(LAST_PERIOD_PRINCIPAL_PAID)))
                .lastPeriodInterestPaid(Option.of(message.getData().get(LAST_PERIOD_INTEREST_PAID)))
                .lastPeriodPenaltyPaid(Option.of(message.getData().get(LAST_PERIOD_PENALTY_PAID)))
                .lastPeriodInsuranceFee(Option.of(message.getData().get(LAST_PERIOD_INSURANCE_FEE)))
                .lastPeriodLegalExpenses(Option.of(message.getData().get(LAST_PERIOD_LEGAL_EXPENSES)))
                .totalBalance(new BigDecimal(message.getData().get(TOTAL_BALANCE)))
                .principalPaid(new BigDecimal(message.getData().get(PRINCIPAL_PAID)))
                .loanAmount(new BigDecimal(message.getData().get(LOAN_AMOUNT)))
                .disbursementDate(message.getData().get(DISBURSEMENT_DATE))
                .penaltyRate(new BigDecimal(message.getData().get(PENALTY_RATE)))
                .amortization(message.getData().get(AMORTIZATION))
                .daysInArrears(Integer.valueOf(message.getData().get(DAYS_IN_ARREARS)))
                .loanState(message.getData().get(LOAN_STATE))
                .build();
        
    }

}
