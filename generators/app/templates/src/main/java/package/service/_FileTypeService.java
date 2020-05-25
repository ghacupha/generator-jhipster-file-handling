package <%= packageName %>.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import <%= packageName %>.domain.FileType;
import <%= packageName %>.repository.FileTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FileType}.
 */
@Service
@Transactional
public class FileTypeService {

    private final Logger log = LoggerFactory.getLogger(FileTypeService.class);

    private final FileTypeRepository fileTypeRepository;

    public FileTypeService(FileTypeRepository fileTypeRepository) {
        this.fileTypeRepository = fileTypeRepository;
    }

    /**
     * Save a fileType.
     *
     * @param fileType the entity to save.
     * @return the persisted entity.
     */
    public FileType save(FileType fileType) {
        log.debug("Request to save FileType : {}", fileType);
        return fileTypeRepository.save(fileType);
    }

    /**
     * Get all the fileTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FileType> findAll(Pageable pageable) {
        log.debug("Request to get all FileTypes");
        return fileTypeRepository.findAll(pageable);
    }


    /**
     * Get one fileType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FileType> findOne(Long id) {
        log.debug("Request to get FileType : {}", id);
        return fileTypeRepository.findById(id);
    }

    /**
     * Delete the fileType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FileType : {}", id);

        fileTypeRepository.deleteById(id);
    }
}
