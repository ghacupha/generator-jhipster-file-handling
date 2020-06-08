package io.github.deposits.app.service;

import io.github.deposits.repository.BranchTableRepository;
import io.github.deposits.repository.search.BranchTableSearchRepository;
import io.github.deposits.service.dto.BranchTableDTO;
import io.github.deposits.service.mapper.BranchTableMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("branchTableBatchService")
public class BranchTableBatchService implements BatchService<BranchTableDTO> {

    private final BranchTableMapper branchTableMapper;
    private final BranchTableRepository branchTableRepository;
    private final BranchTableSearchRepository branchTableSearchRepository;

    public BranchTableBatchService(final BranchTableMapper branchTableMapper, final BranchTableRepository branchTableRepository, final BranchTableSearchRepository branchTableSearchRepository) {
        this.branchTableMapper = branchTableMapper;
        this.branchTableRepository = branchTableRepository;
        this.branchTableSearchRepository = branchTableSearchRepository;
    }

    @Override
    public List<BranchTableDTO> save(final List<BranchTableDTO> entities) {

        return branchTableMapper.toDto(branchTableRepository.saveAll(branchTableMapper.toEntity(entities)));
    }

    @Override
    public void index(final List<BranchTableDTO> entities) {

        branchTableSearchRepository.saveAll(branchTableMapper.toEntity(entities));
    }
}
