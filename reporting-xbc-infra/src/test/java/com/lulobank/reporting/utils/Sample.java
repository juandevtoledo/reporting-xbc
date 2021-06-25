package com.lulobank.reporting.utils;

import com.lulobank.reporting.kernel.domain.entity.FatcaCrsInformation;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.Document;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.DocumentName;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation.FileInformation;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation.vo.AcceptanceTimestamp;
import com.lulobank.reporting.kernel.domain.entity.digitalevidence.document.vo.fileinformation.vo.Location;
import com.lulobank.reporting.kernel.domain.entity.fatca.vo.Country;
import com.lulobank.reporting.kernel.domain.entity.filestatement.StatementFileInformation;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileName;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.StatementType;
import com.lulobank.reporting.kernel.domain.entity.person.ClientInformation;
import com.lulobank.reporting.kernel.domain.entity.person.vo.CardId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Email;
import com.lulobank.reporting.kernel.domain.entity.person.vo.FirstName;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Surname;
import com.lulobank.reporting.kernel.domain.entity.statement.Statement;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.PeriodDate;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;
import flexibility.client.enums.LoanAccountPaymentStatus;
import flexibility.client.models.response.GetLoanStatementResponse;
import io.vavr.collection.HashMap;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lulobank.reporting.usecase.report.fatca.CreateFatcaReportUseCase.PDF_EXTENSION;

public class Sample {


    public static final String ID_CLIENT="cb1b9958-e56f-4b5e-9288-78827cd4d8d3";
    public static final String PRODUCT_TYPE="LOAN_ACCOUNT";
    public static final String REPORT_TYPE="LOANSTATEMENTS";
    public static final String NAME="DANIEL";
    public static final String LAST_NAME="PEREZ";
    public static final String ID_CLIENT_CBS="1999155272";
    public static final String ID_CREDIT_CBS="76834381467";
    public static final String STATEMENT_DATE="2020-09";
    public static final String AUTOMATIC_DEBIT="false";
    public static final String EMAIL="tae_vuykv@mailinator.com";
    public static final BigDecimal INTEREST_RATE=BigDecimal.valueOf(16.5);
    public static final String STATE="Open";
    public static final String STATEMENT_CREATED_AT="2020-09-01T01-01-01";

    public static final LocalDate INSTALMENT_DUE_DATE =LocalDate.parse("2020-09-21");
    public static final LocalDateTime DISBURSEMENT_DATE =LocalDateTime.parse("2020-06-28T10:36:13");
    public static final LocalDate CUT_OFF_DATE = LocalDate.parse("2020-09-10");
    public static final Double INSTALMENT_INTEREST_DUE =0.0;
    public static final Double INSTALMENT_PRINCIPAL_DUE =0.0;
    public static final Double INSTALMENT_TOTAL_DUE =0.0;
    public static final Integer CURRENT_INSTALMENT =2;
    public static final Integer TOTAL_INSTALMENTS =24;
    public static final Double LOAN_AMOUNT =1300000.0;
    public static final String CARD_ID = "1999155272";
    public static final Double TOTAL_BALANCE = 40614.96;
    public static final Double ACCRUED_PENALTY =0.0;
    public static final Double IN_ARREARS_BALANCE = 0.0;
    public static final Double FEE_AMOUNT = 0.0;
    public static final Double LEGAL_EXPENSES = 0.0;
    public static final Double PRINCIPAL_PAID = 0.0;
    public static final Double PENALTY_RATE = 0.0;
    public static final Double PENALTY_BALANCE= 0.0;
    public static final String REPORT_TYPE_FATCA= "FATCA";
    public static final String PRODUCT_TYPE_FATCA= "FatcaReport";

    public static final String AMORTIZATION = "Cuota Fija";

    public static final LocalDateTime ACCEPTANCE_DATE = LocalDateTime.parse("2020-09-28T10:36:13");

    public static Statement getStatement() {
        List<StatementFileInformation> list =new ArrayList<>();
        list.add(StatementFileInformation
                .builder()
                .fileFullPath(FileFullPath.builder().value("/").build())
                .statementType(StatementType.buildLoanStatement())
                .periodDate(PeriodDate.builder().value(STATEMENT_DATE).build())
                .fileName(FileName.buildFileNamePDFStatement(PRODUCT_TYPE,ID_CREDIT_CBS, STATEMENT_CREATED_AT))
                .build());
        return Statement
                .builder()
                .productId(ProductId.builder().value(ID_CREDIT_CBS).build())
                .clientId(ClientId.builder().value(ID_CLIENT).build())
                .productType(ProductType.buildProductLoan())
                .listFiles(list)
                .build();
    }

    public static GetLoanStatementResponse getLoanStatementResponse() {

        GetLoanStatementResponse getLoanStatementResponse =new GetLoanStatementResponse();
        GetLoanStatementResponse.LoanData loanData =new GetLoanStatementResponse.LoanData();
        loanData.setCutOffDate(CUT_OFF_DATE);
        loanData.setInstalmentDueDate(INSTALMENT_DUE_DATE);
        loanData.setInstalmentInterestDue(INSTALMENT_INTEREST_DUE);
        loanData.setInstalmentTotalDue(INSTALMENT_TOTAL_DUE);
        loanData.setInstalment(CURRENT_INSTALMENT);
        loanData.setFeesAmount(FEE_AMOUNT);
        loanData.setLegalExpenses(LEGAL_EXPENSES);
        loanData.setInstalmentPrincipalDue(INSTALMENT_PRINCIPAL_DUE);
        getLoanStatementResponse.setInstalments(TOTAL_INSTALMENTS);
        getLoanStatementResponse.setLoanAmount(LOAN_AMOUNT);
        getLoanStatementResponse.setTotalBalance(TOTAL_BALANCE);
        getLoanStatementResponse.setAccruedPenalty(ACCRUED_PENALTY);
        getLoanStatementResponse.setInArrearsBalance(IN_ARREARS_BALANCE);
        getLoanStatementResponse.setLoanData(loanData);
        getLoanStatementResponse.setAmortization(AMORTIZATION);
        getLoanStatementResponse.setPenaltyRate(PENALTY_RATE);
        getLoanStatementResponse.setPrincipalPaid(PRINCIPAL_PAID);
        getLoanStatementResponse.setDisbursementDate(DISBURSEMENT_DATE);
        getLoanStatementResponse.setPenaltyBalance(PENALTY_BALANCE);
        getLoanStatementResponse.setState(LoanAccountPaymentStatus.UP_TO_DATE);
        getLoanStatementResponse.setDaysInArrears(0L);

        return getLoanStatementResponse;
    }

    public static Map<String, Object> buildMapForCreditStatement() {

        return HashMap.empty()
                .put("periodDate", STATEMENT_DATE)
                .put("idLoan", ID_CREDIT_CBS)
                .put("instalments", CURRENT_INSTALMENT)
                .put("instalment", TOTAL_INSTALMENTS)
                .put("fullPeriodDate", STATEMENT_DATE)
                .put("fullName", NAME)
                .put("creditState", STATE)
                .put("email", EMAIL)
                .put("timelyInstallmentPaymentDate", INSTALMENT_INTEREST_DUE)
                .put("instalmentInterestDue",INSTALMENT_INTEREST_DUE)
                .put("instalmentPrincipalDue", INSTALMENT_PRINCIPAL_DUE)
                .put("insuranceValue", "0")
                .put("legalExpenses", "0")
                .put("instalmentTotalDue", INSTALMENT_TOTAL_DUE)
                .put("totalBalance", TOTAL_BALANCE)
                .put("loanAmount", LOAN_AMOUNT)
                .put("disbursementDate", DISBURSEMENT_DATE)
                .put("interestRate", INTEREST_RATE)
                .put("penaltyRate", "0")
                .put("automaticDebit", AUTOMATIC_DEBIT)
                .put("amortization", "Cuota fija").mapKeys(String::valueOf).toJavaMap();
    }

    public static ClientId getClientId() {
        return ClientId.builder().value(ID_CLIENT).build();
    }

    public static List<Document> getListDocuments() {
        return List.of(
                Document.builder()
                        .documentName(DocumentName.builder().value(FileName.buildFileNameFatca(PDF_EXTENSION, Country.builder().value("USA").build()).getValue()).build())
                        .documentInformation(
                                FileInformation.builder()
                                        .acceptanceTimestamp(AcceptanceTimestamp.builder().value(ACCEPTANCE_DATE).build())
                                        .location(Location.builder().value(ID_CLIENT).build())
                                        .build()
                        )
                        .build()
        );
    }

    public static Map<String, AttributeValue> getDocumentsValue(List<Document> documents) {
        return Map.of(":documents", getDocumentsListValue(documents));
    }

    public static AttributeValue getDocumentsListValue(List<Document> documents) {
        return AttributeValue.builder()
                .l(
                        documents.stream()
                                .map(Sample::getDocAttributeValue)
                                .collect(Collectors.toList()))
                .build();

    }

    public static AttributeValue getDocAttributeValue(Document document) {
        return AttributeValue.builder()
                .m(Map.of(
                        "name", AttributeValue.builder().s(document.getDocumentName().getValue()).build(),
                        "documentInformation", AttributeValue.builder()
                                .m(
                                        Map.of("location",
                                                AttributeValue.builder().s(document.getDocumentInformation().getLocation().getValue()).build(),
                                                "acceptanceTimestamp",
                                                AttributeValue.builder().s(document.getDocumentInformation().getAcceptanceTimestamp().getValue().toString()).build())
                                ).build()

                )).build();
    }

    public static ClientInformation getClientInformation() {
        return ClientInformation.builder()
                .clientId(ClientId.builder().value(ID_CLIENT).build())
                .firstName(FirstName.builder().value(NAME).build())
                .surname(Surname.builder().value(LAST_NAME).build())
                .cardId(CardId.builder().value(CARD_ID).build())
                .email(Email.builder().value(EMAIL).build())
                .acceptanceTimestamp(com.lulobank.reporting.kernel.domain.entity.person.vo.AcceptanceTimestamp.builder().value(ACCEPTANCE_DATE).build())
                .build();
    }

    public static FatcaCrsInformation getFatcaCrsInformation(){
        return FatcaCrsInformation.builder()
                .clientId(ID_CLIENT)
                .countries(getCountries())
                .build();
    }

    public static List<Country> getCountries(){
        List<Country> countries = new ArrayList<>();
        countries.add(Country.builder()
                .value("USA")
                .build());
        return countries;
    }
}
