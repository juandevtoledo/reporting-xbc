package com.lulobank.reporting.kernel.domain.entity.digitalevidence;

import com.lulobank.reporting.kernel.domain.entity.person.vo.CardId;
import com.lulobank.reporting.kernel.domain.entity.person.vo.FirstName;
import com.lulobank.reporting.kernel.domain.entity.person.vo.MiddleName;
import com.lulobank.reporting.kernel.domain.entity.person.vo.SecondSurname;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Surname;
import com.lulobank.reporting.kernel.domain.entity.report.vo.ReportDate;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DigitalEvidencePageInformation {

    private static final String SPACE =" ";
    private static final String EMPTY ="";

    private final FirstName firstName;
    private final MiddleName middleName;
    private final Surname surname;
    private final SecondSurname secondSurname;
    private final CardId cardId;
    private final ReportDate reportDate;

    public String getNames(){
        return firstName.getValue()
                .concat(middleName.getValue().fold(()->EMPTY, SPACE::concat));
    }

    public String getLastNames(){
        return surname.getValue()
                .concat(secondSurname.getValue().fold(()->EMPTY, SPACE::concat));
    }
}
