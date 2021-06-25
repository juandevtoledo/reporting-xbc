package com.lulobank.reporting.utils;

import com.lulobank.reporting.kernel.command.statement.GenerateCreditStatement;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.Document;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.DocumentName;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation.FileInformation;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation.vo.AcceptanceTimestamp;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation.vo.Location;
import com.lulobank.reporting.kernel.domain.entity.filestatement.StatementFileInformation;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileName;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.StatementType;
import com.lulobank.reporting.kernel.domain.entity.loan.CurrentPeriod;
import com.lulobank.reporting.kernel.domain.entity.loan.LastPeriod;
import com.lulobank.reporting.kernel.domain.entity.loan.LoanInformation;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.Amortization;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.CurrentInstalment;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.CutOffDate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.DaysInArrears;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.DisbursementDate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InArrearsBalance;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentDueDate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentInterestDue;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentPenaltiesDue;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentPrincipalDue;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InstalmentTotalDue;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InsuranceFee;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InterestPaid;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LegalExpenses;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanAmount;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanState;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PenaltyPaid;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PenaltyRate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PrincipalPaid;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.TotalBalance;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.TotalInstalments;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.TotalPaid;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.statement.Statement;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.PeriodDate;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;
import io.vavr.control.Option;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Sample {

    public static final String ID_CLIENT = "cb1b9958-e56f-4b5e-9288-78827cd4d8d3";
    public static final String PRODUCT_TYPE = "LOAN_ACCOUNT";
    public static final String REPORT_TYPE = "CREDITSTATEMENTS";
    public static final String NAME = "DANIEL";
    public static final String MIDDLE_NAME = "CARLOS";
    public static final String LAST_NAME = "PEREZ";
    public static final String SECOND_SURNAME = "LOGOS";
    public static final String FULL_NAME = "DANIEL CARLOS PEREZ LOGOS";
    public static final String ID_CLIENT_CBS = "1999155272";
    public static final String CARD_ID = "1999155272";
    public static final String ID_CREDIT_CBS = "76834381467";
    public static final String STATEMENT_DATE = "2020-09";
    public static final String AUTOMATIC_DEBIT = "false";
    public static final String EMAIL = "tae_vuykv@mailinator.com";
    public static final BigDecimal INTEREST_RATE = BigDecimal.valueOf(16.5);
    public static final String STATE = "Open";
    public static final String STATEMENT_TIMESTAMP = "2020-09-01T01-01-01";

    public static final LocalDate INSTALMENT_DUE_DATE = LocalDate.parse("2020-09-21");
    public static final LocalDateTime DISBURSEMENT_DATE = LocalDateTime.parse("2020-06-28T10:36:13");
    public static final LocalDate CUT_OFF_DATE = LocalDate.parse("2020-09-10");
    public static final BigDecimal INSTALMENT_INTEREST_DUE = BigDecimal.valueOf(0.00);
    public static final BigDecimal INSTALMENT_PRINCIPAL_DUE = BigDecimal.valueOf(0.00);
    public static final BigDecimal INSTALMENT_TOTAL_DUE = BigDecimal.valueOf(0.00);
    public static final BigDecimal INSTALMENT_PENALTIES_DUE = BigDecimal.valueOf(0.00);
    public static final Integer CURRENT_INSTALMENT = 2;
    public static final Integer TOTAL_INSTALMENTS = 24;
    public static final BigDecimal LOAN_AMOUNT = BigDecimal.valueOf(1300000.0);
    public static final BigDecimal TOTAL_BALANCE = BigDecimal.valueOf(40614.96);
    public static final BigDecimal IN_ARREARS_BALANCE = BigDecimal.valueOf(0.0);
    public static final BigDecimal FEE_AMOUNT = BigDecimal.valueOf(0.0);
    public static final BigDecimal LEGAL_EXPENSES = BigDecimal.valueOf(0.0);
    public static final BigDecimal PRINCIPAL_PAID = BigDecimal.valueOf(0.0);
    public static final BigDecimal PENALTY_PAID = BigDecimal.valueOf(0.0);

    public static final BigDecimal TOTAL_PAID = BigDecimal.valueOf(0.0);

    public static final BigDecimal INTEREST_PAID = BigDecimal.valueOf(0.0);

    public static final BigDecimal PENALTY_RATE = BigDecimal.valueOf(0.0);
    public static final String AMORTIZATION = "Cuota Fija";
    public static final String UP_TO_DATE = "UP_TO_DATE";

    public static final LocalDateTime ACCEPTANCE_DOCUMENTS_TIME = LocalDateTime.parse("2020-09-28T10:36:13");


    public static GenerateCreditStatement getGenerateCreditStatement() {

        return GenerateCreditStatement.builder()
                .clientId(ID_CLIENT)
                .productType(PRODUCT_TYPE)
                .reportType(REPORT_TYPE)
                .name(NAME)
                .middleName(Option.of(MIDDLE_NAME))
                .lastName(LAST_NAME)
                .secondSurname(Option.of(SECOND_SURNAME))
                .fullName(FULL_NAME)
                .idClientCBS(ID_CLIENT_CBS)
                .idCreditCBS(ID_CREDIT_CBS)
                .statementDate(STATEMENT_DATE)
                .automaticDebit(AUTOMATIC_DEBIT)
                .email(EMAIL)
                .interestRate(INTEREST_RATE)
                .cutOffDate("26/04/2021")
                .instalmentDueDate("21/09/2020")
                .lastPeriodTotalPaid(Option.none())
                .disbursementDate("28/06/2020T10:36:13")
                .penaltyRate(PENALTY_RATE)
                .currentInstalment(CURRENT_INSTALMENT)
                .instalmentTotalDue(INSTALMENT_TOTAL_DUE)
                .instalmentInterestDue(INSTALMENT_INTEREST_DUE)
                .instalmentPrincipalDue(INSTALMENT_PRINCIPAL_DUE)
                .loanAmount(LOAN_AMOUNT)
                .totalBalance(TOTAL_BALANCE)
                .totalInstalments(TOTAL_INSTALMENTS)
                .build();
    }

    public static Statement getStatement() {
        List<StatementFileInformation> list =new ArrayList<>();
        list.add(StatementFileInformation
                .builder()
                .fileFullPath(FileFullPath.builder().value("/").build())
                .statementType(StatementType.buildLoanStatement())
                .periodDate(PeriodDate.builder().value(STATEMENT_DATE).build())
                .fileName(FileName.buildFileNamePDFStatement(PRODUCT_TYPE, ID_CREDIT_CBS, STATEMENT_TIMESTAMP))
                .build());
        return Statement
                .builder()
                .productId(ProductId.builder().value(ID_CREDIT_CBS).build())
                .clientId(ClientId.builder().value(ID_CLIENT).build())
                .productType(ProductType.buildProductLoan())
                .listFiles(list)
                .build();
    }

    public static LoanInformation getLoanInformation() {
        return LoanInformation.builder()
                .currentPeriod(CurrentPeriod.builder()
                        .instalmentDueDate(InstalmentDueDate.builder().value(INSTALMENT_DUE_DATE).build())
                        .instalmentInterestDue(InstalmentInterestDue.builder().value(INSTALMENT_INTEREST_DUE).build())
                        .instalmentPrincipalDue(InstalmentPrincipalDue.builder().value(INSTALMENT_PRINCIPAL_DUE).build())
                        .instalmentTotalDue(InstalmentTotalDue.builder().value(INSTALMENT_TOTAL_DUE).build())
                        .instalmentPenaltiesDue(InstalmentPenaltiesDue.builder().value(INSTALMENT_PENALTIES_DUE).build())
                        .currentInstalment(CurrentInstalment.builder().value(CURRENT_INSTALMENT).build())
                        .cutOffDate(CutOffDate.builder().value(CUT_OFF_DATE).build())
                        .inArrearsBalance(InArrearsBalance.builder().value(IN_ARREARS_BALANCE).build())
                        .legalExpenses(LegalExpenses.builder().value(LEGAL_EXPENSES).build())
                        .insuranceFee(InsuranceFee.builder().value(FEE_AMOUNT).build())
                        .build())
                .lastPeriod(Option.of(
                        LastPeriod.builder()
                                .legalExpenses(LegalExpenses.builder().value(LEGAL_EXPENSES).build())
                                .penaltyPaid(PenaltyPaid.builder().value(PENALTY_PAID).build())
                                .totalPaid(TotalPaid.builder().value(TOTAL_PAID).build())
                                .principalPaid(PrincipalPaid.builder().value(PRINCIPAL_PAID).build())
                                .interestPaid(InterestPaid.builder().value(INTEREST_PAID).build())
                                .insuranceFee(InsuranceFee.builder().value(FEE_AMOUNT).build())
                                .build()))
                .totalBalance(TotalBalance.builder().value(TOTAL_BALANCE).build())
                .disbursementDate(DisbursementDate.builder().value(DISBURSEMENT_DATE).build())
                .totalInstalments(TotalInstalments.builder().value(TOTAL_INSTALMENTS).build())
                .loanAmount(LoanAmount.builder().value(LOAN_AMOUNT).build())
                .principalPaid(PrincipalPaid.builder().value(PRINCIPAL_PAID).build())
                .penaltyRate(PenaltyRate.builder().value(PENALTY_RATE).build())
                .amortization(Amortization.builder().value(AMORTIZATION).build())
                .loanState(LoanState.builder().value(UP_TO_DATE).build())
                .daysInArrears(DaysInArrears.builder().value(0).build())
                .build();
    }

    public static Document getDocument() {
        return Document.builder()
                .documentName(DocumentName.builder().value("document").build())
                .documentInformation(
                        FileInformation.builder()
                                .location(Location.builder().value("location").build())
                                .acceptanceTimestamp(AcceptanceTimestamp.builder().value(ACCEPTANCE_DOCUMENTS_TIME).build())
                                .build())
                .build();
    }
}
