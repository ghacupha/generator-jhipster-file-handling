package io.github.deposits.app.model;

import io.github.deposits.app.Mapping;
import io.github.deposits.service.dto.SbuTableDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface SBUTableEVMMapping extends Mapping<SBUTableEVM, SbuTableDTO> {
}
