package io.github.deposits.app.messaging.depositAccount;

import io.github.deposits.app.Mapping;
import io.github.deposits.service.dto.DepositAccountDTO;
import org.mapstruct.Mapper;

import java.time.LocalDate;

import static io.github.deposits.app.AppConstants.DATETIME_FORMATTER;

@Mapper(componentModel = "spring", uses = {})
public interface DepositAccountMTODTOMapping extends Mapping<DepositAccountMTO, DepositAccountDTO> {

    @org.mapstruct.Mapping(target = "accountOpeningDate", source = "accountOpeningDatey")
    @org.mapstruct.Mapping(target = "accountMaturityDate", source = "accountMaturityDate")
    @org.mapstruct.Mapping(target = "monthOfStudy", source = "monthOfStudy")
    default LocalDate dateStringMap(String stringDate) {
        return stringDate == null ? null : LocalDate.parse(stringDate, DATETIME_FORMATTER);
    }
}
