package <%= packageName %>.internal.resource.decorator;

import <%= packageName %>.service.dto.<%= classNamesPrefix %>FileUploadCriteria;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>FileUploadDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * This interface provides the client alternative ways to implement already existing responses
 * <p/>
 * for file-uploads
 */
public interface IFileUploadResource {
    /**
     * {@code POST  /file-uploads} : Create a new fileUpload.
     *
     * @param fileUploadDTO the fileUploadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileUploadDTO, or with status {@code 400 (Bad Request)} if the fileUpload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    ResponseEntity<<%= classNamesPrefix %>FileUploadDTO> createFileUpload(@Valid @RequestBody <%= classNamesPrefix %>FileUploadDTO fileUploadDTO) throws URISyntaxException;

    /**
     * {@code PUT  /file-uploads} : Updates an existing fileUpload.
     *
     * @param fileUploadDTO the fileUploadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileUploadDTO, or with status {@code 400 (Bad Request)} if the fileUploadDTO is not valid, or with
     * status {@code 500 (Internal Server Error)} if the fileUploadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    ResponseEntity<<%= classNamesPrefix %>FileUploadDTO> updateFileUpload(@Valid @RequestBody <%= classNamesPrefix %>FileUploadDTO fileUploadDTO) throws URISyntaxException;

    /**
     * {@code GET  /file-uploads} : get all the fileUploads.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileUploads in body.
     */
    ResponseEntity<List<<%= classNamesPrefix %>FileUploadDTO>> getAllFileUploads(<%= classNamesPrefix %>FileUploadCriteria criteria, Pageable pageable);

    /**
     * {@code GET  /file-uploads/count} : count all the fileUploads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    ResponseEntity<Long> countFileUploads(<%= classNamesPrefix %>FileUploadCriteria criteria);

    /**
     * {@code GET  /file-uploads/:id} : get the "id" fileUpload.
     *
     * @param id the id of the fileUploadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileUploadDTO, or with status {@code 404 (Not Found)}.
     */
    ResponseEntity<<%= classNamesPrefix %>FileUploadDTO> getFileUpload(@PathVariable Long id);

    /**
     * {@code DELETE  /file-uploads/:id} : delete the "id" fileUpload.
     *
     * @param id the id of the fileUploadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    ResponseEntity<Void> deleteFileUpload(@PathVariable Long id);
}
