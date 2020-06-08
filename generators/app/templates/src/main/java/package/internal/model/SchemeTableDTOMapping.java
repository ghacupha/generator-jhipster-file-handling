package io.github.deposits.app.model;

import io.github.deposits.app.Mapping;
import io.github.deposits.service.dto.SchemeTableDTO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", uses = {})
public interface SchemeTableDTOMapping extends Mapping<SchemeTableEVM, SchemeTableDTO> {
}
