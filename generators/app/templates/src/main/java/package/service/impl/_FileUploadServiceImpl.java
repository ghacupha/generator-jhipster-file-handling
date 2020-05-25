package <%= packageName %>.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import <%= packageName %>.domain.FileUpload;
import <%= packageName %>.service.mapper.FileUploadMapper;
import <%= packageName %>.service.FileUploadService;
import <%= packageName %>.service.dto.FileUploadDTO;
import <%= packageName %>.repository.FileUploadRepository;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FileUpload}.
 */
@Service
@Transactional
public class FileUploadServiceImpl implements FileUploadService {

    private final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final FileUploadRepository fileUploadRepository;

    private final FileUploadMapper fileUploadMapper;

    public FileUploadServiceImpl(FileUploadRepository fileUploadRepository, FileUploadMapper fileUploadMapper) {
        this.fileUploadRepository = fileUploadRepository;
        this.fileUploadMapper = fileUploadMapper;
    }

    /**
     * Save a fileUpload.
     *
     * @param fileUploadDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FileUploadDTO save(FileUploadDTO fileUploadDTO) {
        log.debug("Request to save FileUpload : {}", fileUploadDTO);
        FileUpload fileUpload = fileUploadMapper.toEntity(fileUploadDTO);
        fileUpload = fileUploadRepository.save(fileUpload);
        return fileUploadMapper.toDto(fileUpload);
    }

    /**
     * Get all the fileUploads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FileUploadDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FileUploads");
        return fileUploadRepository.findAll(pageable)
            .map(fileUploadMapper::toDto);
    }


    /**
     * Get one fileUpload by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FileUploadDTO> findOne(Long id) {
        log.debug("Request to get FileUpload : {}", id);
        return fileUploadRepository.findById(id)
            .map(fileUploadMapper::toDto);
    }

    /**
     * Delete the fileUpload by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileUpload : {}", id);

        fileUploadRepository.deleteById(id);
    }
}
