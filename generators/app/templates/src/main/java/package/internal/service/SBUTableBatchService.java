package io.github.deposits.app.service;

import io.github.deposits.repository.SbuTableRepository;
import io.github.deposits.repository.search.SbuTableSearchRepository;
import io.github.deposits.service.dto.SbuTableDTO;
import io.github.deposits.service.mapper.SbuTableMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class SBUTableBatchService implements BatchService<SbuTableDTO> {

    private final SbuTableMapper sbuTableMapper;
    private final SbuTableRepository sbuTableRepository;
    private final SbuTableSearchRepository sbuTableSearchRepository;

    public SBUTableBatchService(final SbuTableMapper sbuTableMapper, final SbuTableRepository sbuTableRepository, final SbuTableSearchRepository sbuTableSearchRepository) {
        this.sbuTableMapper = sbuTableMapper;
        this.sbuTableRepository = sbuTableRepository;
        this.sbuTableSearchRepository = sbuTableSearchRepository;
    }

    @Override
    public List<SbuTableDTO> save(final List<SbuTableDTO> entities) {
        return sbuTableMapper.toDto(sbuTableRepository.saveAll(sbuTableMapper.toEntity(entities)));
    }

    @Override
    public void index(final List<SbuTableDTO> entities) {

        this.sbuTableSearchRepository.saveAll(sbuTableMapper.toEntity(entities));
    }
}
