package io.github.deposits.app.model;

import io.github.deposits.app.Mapping;
import io.github.deposits.service.dto.BranchTableDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface BranchTableEVMMapping extends Mapping<BranchTableEVM, BranchTableDTO> {
}
