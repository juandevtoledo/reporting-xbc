package com.lulobank.reporting.usecase.statement;

import com.lulobank.reporting.kernel.command.statement.ListPeriodsByStatementsAvailable;
import com.lulobank.reporting.kernel.domain.entity.filestatement.StatementFileInformation;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.Period;
import com.lulobank.reporting.usecase.UseCase;
import com.lulobank.reporting.usecase.port.out.repository.StatementRepository;
import com.lulobank.reporting.usecase.util.Util;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class ListPeriodsByStatementsAvailableUseCase implements UseCase<ListPeriodsByStatementsAvailable, List<Period>> {

    private final StatementRepository statementRepository;

    public ListPeriodsByStatementsAvailableUseCase(StatementRepository statementRepository) {
        this.statementRepository = statementRepository;
    }


    @Override
    public Mono<List<Period>> execute(ListPeriodsByStatementsAvailable command) {
        return Util.buildRequestForStatementRepository(command.getClientId(),command.getProductType(),command.getProductId())
                .flatMap(tuple -> statementRepository.findById(tuple._1, tuple._2, tuple._3))
                .map(statement -> mapToPeriodList(statement.getListFiles()))
                .map(this::getPeriodDatesOfLastYer);
    }

    private List<Period> mapToPeriodList(List<StatementFileInformation> list) {
        return io.vavr.collection.List.ofAll(list)
                .map(statementFileInformation -> new Period(statementFileInformation.getInstallment(),
                        statementFileInformation.getPeriodDate()))
                .distinctBy(comparing(Period::getInstallment)
                        .thenComparing(period -> period.getPeriodDate().getValue()))
                .sorted(comparing(Period::getInstallment).reversed())
                .asJava();
    }

    private List<Period> getPeriodDatesOfLastYer(List<Period> list) {
        return list.stream()
                .filter(period -> period.getPeriodDate().toLocalDate()
                        .isAfter(getLimitLower(list.get(0))))
                .collect(Collectors.toList());
    }

    private LocalDate getLimitLower(Period currentPeriod){
        return currentPeriod.getPeriodDate().toLocalDate().minusYears(1);
    }
}
