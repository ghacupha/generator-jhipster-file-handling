package <%= packageName %>.internal.resource;

import <%= packageName %>.internal.messaging.fileNotification.FileNotification;
import <%= packageName %>.internal.messaging.platform.MessageService;
import <%= packageName %>.internal.messaging.platform.TokenizableMessage;
import <%= packageName %>.internal.resource.decorator.IFileUploadResource;
import <%= packageName %>.domain.<%= classNamesPrefix %>FileType;
import <%= packageName %>.service.<%= classNamesPrefix %>FileTypeService;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>FileUploadCriteria;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>FileUploadDTO;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;

/**
 * This resource contains custome file-uploads code. For instance when we receive a POST request
 * <p/>
 * containing the file, we both persist the file and send a file-notification into the queue
 * <p/>
 * thereby triggering secondary file processing protocols and batch processor instances.
 * <p/>
 * Future development might involve a delete process that triggers all tokens associated with a particular
 * <p/>
 * file-upload.
 * <p/>
 * Watch out for the logs for they are created with lombok. So make sure you get that dependency
 */
@Slf4j
@RestController
@RequestMapping("/api/app")
public class AppFileUploadResource implements IFileUploadResource {

    private final IFileUploadResource fileUploadResource;
    private final MessageService<TokenizableMessage<String>, <%= classNamesPrefix %>MessageTokenDTO> fileNotificationMessageService;
    private final <%= classNamesPrefix %>FileTypeService fileTypeService;

    public AppFileUploadResource(final IFileUploadResource fileUploadResourceDecorator, final MessageService<TokenizableMessage<String>, <%= classNamesPrefix %>MessageTokenDTO> fileNotificationMessageService,
                                 final <%= classNamesPrefix %>FileTypeService fileTypeService) {
        this.fileUploadResource = fileUploadResourceDecorator;
        this.fileNotificationMessageService = fileNotificationMessageService;
        this.fileTypeService = fileTypeService;
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

        ResponseEntity<<%= classNamesPrefix %>FileUploadDTO> responseEntity = fileUploadResource.createFileUpload(fileUploadDTO);

        <%= classNamesPrefix %>FileType fileType = fileTypeService.findOne(fileUploadDTO.get<%= classNamesPrefix %>FileTypeId()).get();

        <%= classNamesPrefix %>MessageTokenDTO token = fileNotificationMessageService.sendMessage(FileNotification.builder()
                                                                                           .filename(fileUploadDTO.getFileName())
                                                                                           .description(fileUploadDTO.getDescription())
                                                                                           .fileModelType(fileType.get<%= classNamesPrefix %>fileType())
                                                                                           .fileId(String.valueOf(responseEntity.getBody().getId()))
                                                                                           .build());

        log.info("File {} has been uploaded and sent to queue with token {} @ time {}", token.getDescription(), token.getTokenValue(), token.getTimeSent());

        return responseEntity;
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
    public ResponseEntity<List<<%= classNamesPrefix %>FileUploadDTO>> getAllFileUploads (<%= classNamesPrefix %>FileUploadCriteria criteria, Pageable pageable) {

        return fileUploadResource.getAllFileUploads(criteria, pageable);
    }

    /**
     * {@code GET  /file-uploads/count} : count all the fileUploads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/file-uploads/count")
    public ResponseEntity<Long> countFileUploads (<%= classNamesPrefix %>FileUploadCriteria criteria) {

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
