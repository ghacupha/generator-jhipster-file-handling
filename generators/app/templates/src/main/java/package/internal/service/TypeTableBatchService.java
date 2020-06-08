package io.github.deposits.app.service;

import io.github.deposits.repository.TypeTableRepository;
import io.github.deposits.repository.search.TypeTableSearchRepository;
import io.github.deposits.service.dto.TypeTableDTO;
import io.github.deposits.service.mapper.TypeTableMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TypeTableBatchService implements BatchService<TypeTableDTO> {

    private final TypeTableMapper typeTableMapper;
    private final TypeTableRepository typeTableRepository;
    private final TypeTableSearchRepository typeTableSearchRepository;

    public TypeTableBatchService(final TypeTableMapper typeTableMapper, final TypeTableRepository typeTableRepository, final TypeTableSearchRepository typeTableSearchRepository) {
        this.typeTableMapper = typeTableMapper;
        this.typeTableRepository = typeTableRepository;
        this.typeTableSearchRepository = typeTableSearchRepository;
    }

    @Override
    public List<TypeTableDTO> save(final List<TypeTableDTO> entities) {
        return typeTableMapper.toDto(typeTableRepository.saveAll(typeTableMapper.toEntity(entities)));
    }

    @Override
    public void index(final List<TypeTableDTO> entities) {

        this.typeTableSearchRepository.saveAll(typeTableMapper.toEntity(entities));
    }
}
