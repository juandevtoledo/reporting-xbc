package com.lulobank.reporting.kernel.domain.entity;

import com.lulobank.reporting.kernel.command.statement.GenerateCreditStatement;
import com.lulobank.reporting.kernel.domain.entity.loan.AdditionalLoanInformation;
import com.lulobank.reporting.kernel.domain.entity.loan.CurrentPeriod;
import com.lulobank.reporting.kernel.domain.entity.loan.LastPeriod;
import com.lulobank.reporting.kernel.domain.entity.loan.LoanInformation;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.Amortization;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.AutomaticDebit;
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
import com.lulobank.reporting.kernel.domain.entity.loan.vo.InterestRate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LegalExpenses;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanAmount;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanId;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.LoanState;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PenaltyPaid;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PenaltyRate;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.PrincipalPaid;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.TotalBalance;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.TotalInstalments;
import com.lulobank.reporting.kernel.domain.entity.loan.vo.TotalPaid;
import com.lulobank.reporting.kernel.domain.entity.person.ClientInformation;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientIdCBS;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Email;
import com.lulobank.reporting.kernel.domain.entity.person.vo.FirstName;
import com.lulobank.reporting.kernel.domain.entity.person.vo.MiddleName;
import com.lulobank.reporting.kernel.domain.entity.person.vo.SecondSurname;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Surname;
import com.lulobank.reporting.kernel.domain.entity.statement.StatementAdditionalInformation;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.PeriodDate;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;
import io.vavr.control.Option;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreditStatementInformation {

    private final LoanInformation loanInformation;
    private final ClientInformation clientInformation;
    private final StatementAdditionalInformation statementAdditionalInformation;
    private final AdditionalLoanInformation additionalLoanInformation;

    public static CreditStatementInformation buildByLoanInformationAndCommand(GenerateCreditStatement generateCreditStatement) {
        return CreditStatementInformation.builder()
                .loanInformation(buildLoanInformation(generateCreditStatement))
                .clientInformation(ClientInformation.builder()
                        .clientId(ClientId.builder().value(generateCreditStatement.getClientId()).build())
                        .email(Email.builder().value(generateCreditStatement.getEmail()).build())
                        .firstName(FirstName.builder().value(generateCreditStatement.getName()).build())
                        .middleName(MiddleName.builder().value(generateCreditStatement.getMiddleName()).build())
                        .surname(Surname.builder().value(generateCreditStatement.getLastName()).build())
                        .secondSurname(SecondSurname.builder().value(generateCreditStatement.getSecondSurname()).build())
                        .fullName(generateCreditStatement.getFullName())
                        .clientIdCBS(ClientIdCBS.builder().value(generateCreditStatement.getIdClientCBS()).build())
                        .build())
                .additionalLoanInformation(AdditionalLoanInformation.builder()
                        .automaticDebit(AutomaticDebit.builder().value(Boolean.parseBoolean(generateCreditStatement.getAutomaticDebit())).build())
                        .interestRate(InterestRate.builder().value(generateCreditStatement.getInterestRate()).build())
                        .loanId(LoanId.builder().value(generateCreditStatement.getIdCreditCBS()).build())
                        .build())
                .statementAdditionalInformation(StatementAdditionalInformation.builder()
                        .periodDate(PeriodDate.builder().value(generateCreditStatement.getStatementDate()).build())
                        .productType(ProductType.builder().value(generateCreditStatement.getProductType()).build())
                        .build())
                .build();
    }
    
    private static LoanInformation buildLoanInformation(GenerateCreditStatement generateCreditStatement) {
    	return LoanInformation.builder()
    			.totalInstalments(TotalInstalments.builder().value(generateCreditStatement.getTotalInstalments()).build())
    			.currentPeriod(buildCurrentPeriod(generateCreditStatement))
    			.lastPeriod(buildLasPeriod(generateCreditStatement))
    			.totalBalance(TotalBalance.builder().value(generateCreditStatement.getTotalBalance()).build())
    			.principalPaid(PrincipalPaid.builder().value(generateCreditStatement.getPrincipalPaid()).build())
    			.loanAmount(LoanAmount.builder().value(generateCreditStatement.getLoanAmount()).build())
    			.disbursementDate(DisbursementDate.createFromString(generateCreditStatement.getDisbursementDate()))
    			.penaltyRate(PenaltyRate.builder().value(generateCreditStatement.getPenaltyRate()).build())
    			.amortization(Amortization.builder().value(generateCreditStatement.getAmortization()).build())
    			.daysInArrears(DaysInArrears.builder().value(generateCreditStatement.getDaysInArrears()).build())
    			.loanState(LoanState.builder().value(generateCreditStatement.getLoanState()).build())
    			.build();
    }
    
    private static Option<LastPeriod> buildLasPeriod(GenerateCreditStatement generateCreditStatement) {
    	return generateCreditStatement.getLastPeriodTotalPaid().map(lastPeriodTotalPaid -> 
    		LastPeriod.builder()
    		.totalPaid(TotalPaid.buildFromString(lastPeriodTotalPaid))
    		.principalPaid(PrincipalPaid.buildFromString(generateCreditStatement.getLastPeriodPrincipalPaid().getOrElse("0")))
    		.interestPaid(InterestPaid.buildFromString(generateCreditStatement.getLastPeriodInterestPaid().getOrElse("0")))
    		.penaltyPaid(PenaltyPaid.buildFromString(generateCreditStatement.getLastPeriodPenaltyPaid().getOrElse("0")))
    		.insuranceFee(InsuranceFee.buildFromString(generateCreditStatement.getLastPeriodInsuranceFee().getOrElse("0")))
    		.legalExpenses(LegalExpenses.buildFromString(generateCreditStatement.getLastPeriodLegalExpenses().getOrElse("0")))
    		.build()
    	);
	}

	private static CurrentPeriod buildCurrentPeriod(GenerateCreditStatement generateCreditStatement) {
    	return CurrentPeriod.builder()
    			.cutOffDate(CutOffDate.createFromString(generateCreditStatement.getCutOffDate()))
    			.instalmentDueDate(InstalmentDueDate.createFromString(generateCreditStatement.getInstalmentDueDate()))
    			.instalmentTotalDue(InstalmentTotalDue.builder().value(generateCreditStatement.getInstalmentTotalDue()).build())
    			.instalmentPrincipalDue(InstalmentPrincipalDue.builder().value(generateCreditStatement.getInstalmentPrincipalDue()).build())
    			.instalmentInterestDue(InstalmentInterestDue.builder().value(generateCreditStatement.getInstalmentInterestDue()).build())
    			.instalmentPenaltiesDue(InstalmentPenaltiesDue.builder().value(generateCreditStatement.getInstalmentPenaltiesDue()).build())
    			.inArrearsBalance(InArrearsBalance.builder().value(generateCreditStatement.getInArrearsBalance()).build())
    			.insuranceFee(InsuranceFee.builder().value(generateCreditStatement.getInsuranceFee()).build())
    			.legalExpenses(LegalExpenses.builder().value(generateCreditStatement.getLegalExpenses()).build())
    			.currentInstalment(CurrentInstalment.builder().value(generateCreditStatement.getCurrentInstalment()).build())
    			.build();
    }

}
