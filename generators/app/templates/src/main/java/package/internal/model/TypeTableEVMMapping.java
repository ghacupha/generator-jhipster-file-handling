package io.github.deposits.app.model;

import io.github.deposits.app.Mapping;
import io.github.deposits.service.dto.TypeTableDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface TypeTableEVMMapping extends Mapping<TypeTableEVM, TypeTableDTO> {
}
