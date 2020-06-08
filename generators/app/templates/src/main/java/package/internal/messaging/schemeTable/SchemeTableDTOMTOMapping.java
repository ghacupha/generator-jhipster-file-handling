package io.github.deposits.app.messaging.schemeTable;

import io.github.deposits.app.Mapping;
import io.github.deposits.service.dto.SchemeTableDTO;
import org.springframework.stereotype.Component;

@Component("schemeTableDTOMTOMapping")
public class SchemeTableDTOMTOMapping implements Mapping<SchemeTableDTO, SchemeTableMTO> {

    @Override
    public SchemeTableDTO toValue1(final SchemeTableMTO vs) {
        SchemeTableDTO schemeTable = new SchemeTableDTO();
        schemeTable.setSchemeCode(vs.getSchemeCode());
        schemeTable.setDescription(vs.getSchemeDescription());

        return schemeTable;
    }

    @Override
    public SchemeTableMTO toValue2(final SchemeTableDTO vs) {
        return SchemeTableMTO.builder().schemeCode(vs.getSchemeCode()).schemeDescription(vs.getDescription()).build();
    }
}
