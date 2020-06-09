package <%= packageName %>.internal.resource.decorator;

import <%= packageName %>.service.dto.<%= classNamesPrefix %>FileUploadCriteria;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>FileUploadDTO;
import <%= packageName %>.web.rest.<%= classNamesPrefix %>FileUploadResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * This decorator implements the {@code IFileUploadResource} using the original {@code FileUploadResource}
 * <p/>
 * which has the effect that the original code will have not indication whatsoever that it has been
 * <p/>
 * tempered with to implement some interface
 * <p/>
 * This class can now be extented by a client to extend the original code without repeatative calls
 * <p/>
 * to the original code itself. Check child classes for illustration
 * @param fileUploadResource
 */
@Component("fileUploadResourceDecorator")
public class FileUploadResourceDecorator implements IFileUploadResource {

    private final <%= classNamesPrefix %>FileUploadResource fileUploadResource;

    public FileUploadResourceDecorator(final <%= classNamesPrefix %>FileUploadResource fileUploadResource) {
        this.fileUploadResource = fileUploadResource;
    }

    /**
     * {@code POST  /file-uploads} : Create a new fileUpload.
     *
     * @param fileUploadDTO the fileUploadDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fileUploadDTO, or with status {@code 400 (Bad Request)} if the fileUpload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/file-uploads")
    public ResponseEntity<<%= classNamesPrefix %>FileUploadDTO> createFileUpload(@Valid @RequestBody <%= classNamesPrefix %>FileUploadDTO fileUploadDTO) throws URISyntaxException {

        return fileUploadResource.createFileUpload(fileUploadDTO);
    }

    /**
     * {@code PUT  /file-uploads} : Updates an existing fileUpload.
     *
     * @param fileUploadDTO the fileUploadDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fileUploadDTO, or with status {@code 400 (Bad Request)} if the fileUploadDTO is not valid, or with
     * status {@code 500 (Internal Server Error)} if the fileUploadDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/file-uploads")
    public ResponseEntity<<%= classNamesPrefix %>FileUploadDTO> updateFileUpload(@Valid @RequestBody <%= classNamesPrefix %>FileUploadDTO fileUploadDTO) throws URISyntaxException {

        return fileUploadResource.updateFileUpload(fileUploadDTO);
    }

    /**
     * {@code GET  /file-uploads} : get all the fileUploads.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fileUploads in body.
     */
    @GetMapping("/file-uploads")
    public ResponseEntity<List<<%= classNamesPrefix %>FileUploadDTO>> getAllFileUploads(<%= classNamesPrefix %>FileUploadCriteria criteria, Pageable pageable) {

        return fileUploadResource.getAllFileUploads(criteria, pageable);
    }

    /**
     * {@code GET  /file-uploads/count} : count all the fileUploads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/file-uploads/count")
    public ResponseEntity<Long> countFileUploads(<%= classNamesPrefix %>FileUploadCriteria criteria) {

        return fileUploadResource.countFileUploads(criteria);
    }

    /**
     * {@code GET  /file-uploads/:id} : get the "id" fileUpload.
     *
     * @param id the id of the fileUploadDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fileUploadDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/file-uploads/{id}")
    public ResponseEntity<<%= classNamesPrefix %>FileUploadDTO> getFileUpload(@PathVariable Long id) {

        return fileUploadResource.getFileUpload(id);
    }

    /**
     * {@code DELETE  /file-uploads/:id} : delete the "id" fileUpload.
     *
     * @param id the id of the fileUploadDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/file-uploads/{id}")
    public ResponseEntity<Void> deleteFileUpload(@PathVariable Long id) {

        return fileUploadResource.deleteFileUpload(id);
    }
}
