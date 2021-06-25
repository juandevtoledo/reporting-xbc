package com.lulobank.reporting.utils;

import com.lulobank.reporting.kernel.domain.entity.filestatement.StatementFileInformation;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileFullPath;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.FileName;
import com.lulobank.reporting.kernel.domain.entity.filestatement.vo.StatementType;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.statement.Statement;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.PeriodDate;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductId;
import com.lulobank.reporting.kernel.domain.entity.statement.vo.ProductType;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SampleStatementRepository {

    public static String PATH ="statement/{0}/{1}/{2}/";
    public static String FILE_NAME ="credit_account-76834381467.pdf";
    public static String PERIOD_2019_09 ="2019-09";
    public static String PERIOD_2019_10 ="2019-10";
    public static String PERIOD_2019_11 ="2019-11";
    public static String PERIOD_2019_12 ="2019-12";
    public static String PERIOD_2020_01 ="2020-01";
    public static String PERIOD_2020_02 ="2020-02";
    public static String PERIOD_2020_03 ="2020-03";
    public static String PERIOD_2020_04 ="2020-04";
    public static String PERIOD_2020_05 ="2020-05";
    public static String PERIOD_2020_06 ="2020-06";
    public static String PERIOD_2020_07 ="2020-07";
    public static String PERIOD_2020_08 ="2020-08";
    public static String PERIOD_2020_09 ="2020-09";
    public static String PERIOD_2020_10 ="2020-10";
    public static String PERIOD_2020_11 ="2020-11";

    public static Statement getStatement(){
        return Statement
                .builder()
                .clientId(ClientId.builder().value(Sample.ID_CLIENT).build())
                .productType(ProductType.buildProductLoan())
                .productId(ProductId.builder().value(Sample.ID_CREDIT_CBS).build())
                .listFiles(getStatementFileInformationList())
                .build();

    }

    public static Statement getStatementWithOneFile(){
        return Statement
                .builder()
                .clientId(ClientId.builder().value(Sample.ID_CLIENT).build())
                .productType(ProductType.buildProductLoan())
                .productId(ProductId.builder().value(Sample.ID_CREDIT_CBS).build())
                .listFiles(List.of(StatementFileInformation
                        .builder()
                        .fileFullPath(FileFullPath.builder().value(buildPath(PERIOD_2020_10, 1)).build())
                        .periodDate( PeriodDate.builder().value(PERIOD_2020_10).build())
                        .statementType(StatementType.buildLoanStatement())
                        .fileName(FileName.builder().value(FILE_NAME).build())
                        .installment(1)
                        .createdAt(getCreatedAtFromPeriod(PERIOD_2020_10))
                        .build()))
                .build();

    }

    public static List<StatementFileInformation> getStatementFileInformationList(){
        List<String> periods = buildList();
        return IntStream.range(0, periods.size())
                .mapToObj(i -> StatementFileInformation.builder()
                    .fileName(FileName.builder().value(FILE_NAME).build())
                    .statementType(StatementType.buildLoanStatement())
                    .periodDate(PeriodDate.builder().value(periods.get(i)).build())
                    .fileFullPath(FileFullPath.builder().value(buildPath(periods.get(i), i+1)).build())
                    .installment(i+1)
                    .createdAt(getCreatedAtFromPeriod(periods.get(i)))
                    .build())
                .collect(Collectors.toList());

    }

    public static String buildPath(String period, Integer installment){
        return MessageFormat.format(PATH, Sample.ID_CLIENT, Sample.ID_CREDIT_CBS, installment.toString().concat("_")
                .concat(period)).concat(FILE_NAME);
    }

    private static LocalDateTime getCreatedAtFromPeriod(String period) {
        return LocalDate.parse(period+"-01",
                DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
    }

    private static List<String> buildList(){
        return List.of(PERIOD_2019_09,
                PERIOD_2019_10,
                PERIOD_2019_11,
                PERIOD_2019_12,
                PERIOD_2020_01,
                PERIOD_2020_02,
                PERIOD_2020_03,
                PERIOD_2020_04,
                PERIOD_2020_05,
                PERIOD_2020_06,
                PERIOD_2020_07,
                PERIOD_2020_08,
                PERIOD_2020_09,
                PERIOD_2020_10,
                PERIOD_2020_11);
    }
}
