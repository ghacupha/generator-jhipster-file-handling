package io.github.deposits.app.model;

import io.github.deposits.app.Mapping;
import io.github.deposits.domain.enumeration.CurrencyLocality;
import io.github.deposits.service.dto.CurrencyTableDTO;
import org.mapstruct.Mapper;

import static io.github.deposits.domain.enumeration.CurrencyLocality.FOREIGN;
import static io.github.deposits.domain.enumeration.CurrencyLocality.LOCAL;

@Mapper(componentModel = "spring", uses = {})
public interface CurrencyTableEVMMapping extends Mapping<CurrencyTableEVM, CurrencyTableDTO> {

    @org.mapstruct.Mapping(target = "locality", source = "locality")
    default CurrencyLocality locality(String localityString) {

        if (localityString == null) {
            return null;
        }
        if (localityString.equalsIgnoreCase("LOCAL")) {
            return LOCAL;
        }
        if (localityString.equalsIgnoreCase("FOREIGN")) {
            return FOREIGN;
        }
        return null;
    }

    @org.mapstruct.Mapping(target = "locality", source = "locality")
    default String locality(CurrencyLocality locality) {

        if (locality == null) {
            return null;
        }
        if (locality == LOCAL) {
            return "LOCAL";
        }
        if (locality == FOREIGN) {
            return "FOREIGN";
        }
        return null;
    }
}
