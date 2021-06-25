package com.lulobank.reporting.kernel.command.statement;

import com.lulobank.reporting.kernel.command.Command;
import io.vavr.control.Option;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class GenerateCreditStatement implements Command {

    private final String clientId;
    private final String productType;
    private final String reportType;
    private final String name;
    private final Option<String> middleName;
    private final String lastName;
    private final Option<String> secondSurname;
    private final String fullName;
    private final String idClientCBS;
    private final String idCreditCBS;
    private final String statementDate;
    private final String automaticDebit;
    private final String email;
    private final BigDecimal interestRate;
    private final Integer totalInstalments;
    private final String cutOffDate;
    private final String instalmentDueDate;
    private final BigDecimal instalmentTotalDue; 
    private final BigDecimal instalmentPrincipalDue;
    private final BigDecimal instalmentInterestDue;
    private final BigDecimal instalmentPenaltiesDue;
    private final BigDecimal inArrearsBalance;
    private final BigDecimal insuranceFee;
    private final BigDecimal legalExpenses;
    private final Integer currentInstalment;
    private final Option<String> lastPeriodTotalPaid;
    private final Option<String> lastPeriodPrincipalPaid;
    private final Option<String> lastPeriodInterestPaid;
    private final Option<String> lastPeriodPenaltyPaid;
    private final Option<String> lastPeriodInsuranceFee;
    private final Option<String> lastPeriodLegalExpenses;
    private final BigDecimal totalBalance;
    private final BigDecimal principalPaid;
    private final BigDecimal loanAmount;
    private final String disbursementDate;
    private final BigDecimal penaltyRate;
    private final String amortization;
    private final Integer daysInArrears;
    private final String loanState;

}


