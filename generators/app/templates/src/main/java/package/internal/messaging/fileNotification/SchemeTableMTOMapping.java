package io.github.deposits.app.messaging.fileNotification;

import io.github.deposits.app.Mapping;
import io.github.deposits.app.messaging.schemeTable.SchemeTableMTO;
import io.github.deposits.app.model.SchemeTableEVM;
import org.springframework.stereotype.Component;

/**
 * This mapping pnly files in the usual scheme-table fields and skips the other
 * details which are to be filled in by the message services involved
 */
@Component("schemeTableMTOMapping")
public class SchemeTableMTOMapping implements Mapping<SchemeTableEVM, SchemeTableMTO> {

    @Override
    public SchemeTableEVM toValue1(final SchemeTableMTO vs) {
        return SchemeTableEVM.builder().description(vs.getSchemeDescription()).schemeCode(vs.getSchemeCode()).build();
    }

    @Override
    public SchemeTableMTO toValue2(final SchemeTableEVM vs) {
        return SchemeTableMTO.builder().schemeDescription(vs.getDescription()).schemeCode(vs.getSchemeCode()).build();
    }
}
