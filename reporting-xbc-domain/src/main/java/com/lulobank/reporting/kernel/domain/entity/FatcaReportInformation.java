package com.lulobank.reporting.kernel.domain.entity;

import com.lulobank.reporting.kernel.command.statement.CreateReport;
import com.lulobank.reporting.kernel.domain.entity.fatca.FatcaInformation;
import com.lulobank.reporting.kernel.domain.entity.fatca.vo.Country;
import com.lulobank.reporting.kernel.domain.entity.fatca.vo.TaxLiability;
import com.lulobank.reporting.kernel.domain.entity.fatca.vo.TaxNumber;
import com.lulobank.reporting.kernel.domain.entity.person.ClientInformation;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Address;
import com.lulobank.reporting.kernel.domain.entity.person.vo.AddressComplement;
import com.lulobank.reporting.kernel.domain.entity.person.vo.BirthPlace;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Birthdate;
import com.lulobank.reporting.kernel.domain.entity.person.vo.CardId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.City;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.ClientIdCBS;
import com.lulobank.reporting.kernel.domain.entity.person.vo.DocumentType;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Email;
import com.lulobank.reporting.kernel.domain.entity.person.vo.FirstName;
import com.lulobank.reporting.kernel.domain.entity.person.vo.MiddleName;
import com.lulobank.reporting.kernel.domain.entity.person.vo.PhoneNumber;
import com.lulobank.reporting.kernel.domain.entity.person.vo.SecondSurname;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Surname;
import com.lulobank.reporting.kernel.domain.entity.report.ReportInformation;
import com.lulobank.reporting.kernel.domain.entity.report.vo.ReportDate;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class FatcaReportInformation {

    public static final String USA = "USA";

    private final ClientInformation clientInformation;
    private final List<FatcaInformation> fatcaInformation;
    private final ReportInformation reportInformation;
    private boolean isFatcaResponsible;
    private boolean isCrsResponsible;

    private FatcaReportInformation(ClientInformation clientInformation,
                                   List<FatcaInformation> fatcaInformation,
                                   ReportInformation reportInformation) {
        this.clientInformation = clientInformation;
        this.fatcaInformation = fatcaInformation;
        this.reportInformation = reportInformation;
    }

    public static FatcaReportInformation buildFatcaReportInformationFromCreateReport(CreateReport command) {
        Map<String, Object> data = command.getData();
        return new FatcaReportInformation(buildClientInformation(data,command),
                buildFatcaInformationList(data), buildReportInformation(data));
    }

    private static ClientInformation buildClientInformation(Map<String, Object> data,CreateReport command ) {
        return ClientInformation.builder()
                .clientId(ClientId.builder().value(command.getIdClient()).build())
                .firstName(FirstName.builder().value(getField(data.get("firstName"))).build())
                .middleName(MiddleName.builder().value(Option.of(getField(data.get("middleName")))).build())
                .surname(Surname.builder().value(getField(data.get("surname"))).build())
                .secondSurname(SecondSurname.builder().value(Option.of(getField(data.get("secondSurname")))).build())
                .documentType(DocumentType.builder().value(getField(data.get("documentType"))).build())
                .clientIdCBS(ClientIdCBS.builder().value(getField(data.get("idCard"))).build())
                .cardId(CardId.builder().value(getField(data.get("idCard"))).build())
                .birthdate(Birthdate.builder().value(LocalDate.parse(getField(data.get("birthDate")),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"))).build())
                .birthPlace(BirthPlace.builder().value(getField(data.get("birthPlace"))).build())
                .address(Address.builder().value(getField(data.get("address"))).build())
                .addressComplement(AddressComplement.builder().value(getField(data.get("addressComplement"))).build())
                .city(City.builder().value(getField(data.get("city"))).build())
                .phoneNumber(PhoneNumber.builder().value(getField(data.get("phoneNumber"))).build())
                .email(Email.builder().value(getField(data.get("email"))).build())
                .build();
    }

    private static List<FatcaInformation> buildFatcaInformationList(Map<String, Object> data) {
        return ((List<Map<String, Object>>) data.get("countries")).stream()
                .map(FatcaReportInformation::buildFatcaInformation)
                .collect(Collectors.toList());
    }

    private static FatcaInformation buildFatcaInformation(Map<String, Object> data) {
        return FatcaInformation.builder()
                .taxLiability(TaxLiability.builder().value(Boolean.valueOf(getField(data.get("taxLiability")))).build())
                .country(Country.builder().value(getField(data.get("country"))).build())
                .taxNumber(TaxNumber.builder().value(getField(data.get("taxNumber"))).build())
                .build();
    }

    private static ReportInformation buildReportInformation(Map<String, Object> data) {
        return ReportInformation.builder()
                .reportDate(ReportDate.builder().value(LocalDateTime.parse(getField(data.get("reportDate")),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm:ss"))).build())
                .build();
    }

    private static String getField(Object o) {
        return Option.of(o).fold(() -> null, Object::toString);
    }

}
