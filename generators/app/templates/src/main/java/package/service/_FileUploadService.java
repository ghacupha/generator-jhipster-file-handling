package <%= packageName %>.service;

import <%= packageName %>.domain.FileUpload;
import <%= packageName %>.service.dto.FileUploadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link FileUpload}.
 */
public interface FileUploadService {

    /**
     * Save a fileUpload.
     *
     * @param fileUploadDTO the entity to save.
     * @return the persisted entity.
     */
    FileUploadDTO save(FileUploadDTO fileUploadDTO);

    /**
     * Get all the fileUploads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FileUploadDTO> findAll(Pageable pageable);


    /**
     * Get the "id" fileUpload.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileUploadDTO> findOne(Long id);

    /**
     * Delete the "id" fileUpload.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
