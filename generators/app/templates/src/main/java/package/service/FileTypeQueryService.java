package io.github.files;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link FileType} entities in the database.
 * The main input is a {@link FileTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FileType} or a {@link Page} of {@link FileType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FileTypeQueryService extends QueryService<FileType> {

    private final Logger log = LoggerFactory.getLogger(FileTypeQueryService.class);

    private final FileTypeRepository fileTypeRepository;

    public FileTypeQueryService(FileTypeRepository fileTypeRepository) {
        this.fileTypeRepository = fileTypeRepository;
    }

    /**
     * Return a {@link List} of {@link FileType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FileType> findByCriteria(FileTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FileType> specification = createSpecification(criteria);
        return fileTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link FileType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FileType> findByCriteria(FileTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FileType> specification = createSpecification(criteria);
        return fileTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FileTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FileType> specification = createSpecification(criteria);
        return fileTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FileTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FileType> createSpecification(FileTypeCriteria criteria) {
        Specification<FileType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FileType_.id));
            }
            if (criteria.getFileTypeName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileTypeName(), FileType_.fileTypeName));
            }
            if (criteria.getFileMediumType() != null) {
                specification = specification.and(buildSpecification(criteria.getFileMediumType(), FileType_.fileMediumType));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), FileType_.description));
            }
            if (criteria.getFileType() != null) {
                specification = specification.and(buildSpecification(criteria.getFileType(), FileType_.fileType));
            }
        }
        return specification;
    }
}
