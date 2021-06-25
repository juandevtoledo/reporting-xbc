package com.lulobank.reporting.kernel.domain.entity.person;

import com.lulobank.reporting.kernel.domain.entity.person.vo.AcceptanceTimestamp;
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
import com.lulobank.reporting.kernel.domain.entity.person.vo.PhonePrefix;
import com.lulobank.reporting.kernel.domain.entity.person.vo.SecondSurname;
import com.lulobank.reporting.kernel.domain.entity.person.vo.Surname;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClientInformation {

    private static final String SPACE =" ";
    private static final String EMPTY ="";

    private final ClientId clientId;
    private final ClientIdCBS clientIdCBS;
    private final FirstName firstName;
    private final MiddleName middleName;
    private final Surname surname;
    private final SecondSurname secondSurname;
    private final String fullName;
    private final Email email;
    private final CardId cardId;
    private final Address address;
    private final AddressComplement addressComplement;
    private final PhonePrefix phonePrefix;
    private final PhoneNumber phoneNumber;
    private final AcceptanceTimestamp acceptanceTimestamp;
    private final DocumentType documentType;
    private final Birthdate birthdate;
    private final BirthPlace birthPlace;
    private final City city;
}
