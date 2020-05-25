package <%= packageName %>.web.rest;

import <%= packageName %>.<%= appName %>App;
import <%= packageName %>.domain.FileType;
import <%= packageName %>.repository.FileTypeRepository;
import <%= packageName %>.service.FileTypeService;
import <%= packageName %>.service.dto.FileTypeCriteria;
import <%= packageName %>.service.FileTypeQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import <%= packageName %>.domain.enumeration.FileMediumTypes;
import <%= packageName %>.domain.enumeration.FileModelType;
/**
 * Integration tests for the {@link FileTypeResource} REST controller.
 */
@SpringBootTest(classes = <%= appName %>App.class)
@AutoConfigureMockMvc
@WithMockUser
public class FileTypeResourceIT {

    private static final String DEFAULT_FILE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE_NAME = "BBBBBBBBBB";

    private static final FileMediumTypes DEFAULT_FILE_MEDIUM_TYPE = FileMediumTypes.EXCEL;
    private static final FileMediumTypes UPDATED_FILE_MEDIUM_TYPE = FileMediumTypes.EXCEL_XLS;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FILE_TEMPLATE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE_TEMPLATE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_TEMPLATE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_TEMPLATE_CONTENT_TYPE = "image/png";

    private static final FileModelType DEFAULT_FILE_TYPE = FileModelType.SERVICE_OUTLETS;
    private static final FileModelType UPDATED_FILE_TYPE = FileModelType.TRANSACTION_ACCOUNTS;

    @Autowired
    private FileTypeRepository fileTypeRepository;

    @Autowired
    private FileTypeService fileTypeService;

    @Autowired
    private FileTypeQueryService fileTypeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileTypeMockMvc;

    private FileType fileType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileType createEntity(EntityManager em) {
        FileType fileType = new FileType()
            .fileTypeName(DEFAULT_FILE_TYPE_NAME)
            .fileMediumType(DEFAULT_FILE_MEDIUM_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .fileTemplate(DEFAULT_FILE_TEMPLATE)
            .fileTemplateContentType(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)
            .fileType(DEFAULT_FILE_TYPE);
        return fileType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FileType createUpdatedEntity(EntityManager em) {
        FileType fileType = new FileType()
            .fileTypeName(UPDATED_FILE_TYPE_NAME)
            .fileMediumType(UPDATED_FILE_MEDIUM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .fileTemplate(UPDATED_FILE_TEMPLATE)
            .fileTemplateContentType(UPDATED_FILE_TEMPLATE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE);
        return fileType;
    }

    @BeforeEach
    public void initTest() {
        fileType = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileType() throws Exception {
        int databaseSizeBeforeCreate = fileTypeRepository.findAll().size();
        // Create the FileType
        restFileTypeMockMvc.perform(post("/api/file-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileType)))
            .andExpect(status().isCreated());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeCreate + 1);
        FileType testFileType = fileTypeList.get(fileTypeList.size() - 1);
        assertThat(testFileType.getFileTypeName()).isEqualTo(DEFAULT_FILE_TYPE_NAME);
        assertThat(testFileType.getFileMediumType()).isEqualTo(DEFAULT_FILE_MEDIUM_TYPE);
        assertThat(testFileType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFileType.getFileTemplate()).isEqualTo(DEFAULT_FILE_TEMPLATE);
        assertThat(testFileType.getFileTemplateContentType()).isEqualTo(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE);
        assertThat(testFileType.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
    }

    @Test
    @Transactional
    public void createFileTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fileTypeRepository.findAll().size();

        // Create the FileType with an existing ID
        fileType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileTypeMockMvc.perform(post("/api/file-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileType)))
            .andExpect(status().isBadRequest());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFileTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileTypeRepository.findAll().size();
        // set the field null
        fileType.setFileTypeName(null);

        // Create the FileType, which fails.


        restFileTypeMockMvc.perform(post("/api/file-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileType)))
            .andExpect(status().isBadRequest());

        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFileMediumTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = fileTypeRepository.findAll().size();
        // set the field null
        fileType.setFileMediumType(null);

        // Create the FileType, which fails.


        restFileTypeMockMvc.perform(post("/api/file-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileType)))
            .andExpect(status().isBadRequest());

        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFileTypes() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList
        restFileTypeMockMvc.perform(get("/api/file-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileTypeName").value(hasItem(DEFAULT_FILE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].fileMediumType").value(hasItem(DEFAULT_FILE_MEDIUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileTemplateContentType").value(hasItem(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE))))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getFileType() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get the fileType
        restFileTypeMockMvc.perform(get("/api/file-types/{id}", fileType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fileType.getId().intValue()))
            .andExpect(jsonPath("$.fileTypeName").value(DEFAULT_FILE_TYPE_NAME))
            .andExpect(jsonPath("$.fileMediumType").value(DEFAULT_FILE_MEDIUM_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.fileTemplateContentType").value(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileTemplate").value(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE)))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE.toString()));
    }


    @Test
    @Transactional
    public void getFileTypesByIdFiltering() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        Long id = fileType.getId();

        defaultFileTypeShouldBeFound("id.equals=" + id);
        defaultFileTypeShouldNotBeFound("id.notEquals=" + id);

        defaultFileTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFileTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultFileTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFileTypeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFileTypesByFileTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileTypeName equals to DEFAULT_FILE_TYPE_NAME
        defaultFileTypeShouldBeFound("fileTypeName.equals=" + DEFAULT_FILE_TYPE_NAME);

        // Get all the fileTypeList where fileTypeName equals to UPDATED_FILE_TYPE_NAME
        defaultFileTypeShouldNotBeFound("fileTypeName.equals=" + UPDATED_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllFileTypesByFileTypeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileTypeName not equals to DEFAULT_FILE_TYPE_NAME
        defaultFileTypeShouldNotBeFound("fileTypeName.notEquals=" + DEFAULT_FILE_TYPE_NAME);

        // Get all the fileTypeList where fileTypeName not equals to UPDATED_FILE_TYPE_NAME
        defaultFileTypeShouldBeFound("fileTypeName.notEquals=" + UPDATED_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllFileTypesByFileTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileTypeName in DEFAULT_FILE_TYPE_NAME or UPDATED_FILE_TYPE_NAME
        defaultFileTypeShouldBeFound("fileTypeName.in=" + DEFAULT_FILE_TYPE_NAME + "," + UPDATED_FILE_TYPE_NAME);

        // Get all the fileTypeList where fileTypeName equals to UPDATED_FILE_TYPE_NAME
        defaultFileTypeShouldNotBeFound("fileTypeName.in=" + UPDATED_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllFileTypesByFileTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileTypeName is not null
        defaultFileTypeShouldBeFound("fileTypeName.specified=true");

        // Get all the fileTypeList where fileTypeName is null
        defaultFileTypeShouldNotBeFound("fileTypeName.specified=false");
    }
                @Test
    @Transactional
    public void getAllFileTypesByFileTypeNameContainsSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileTypeName contains DEFAULT_FILE_TYPE_NAME
        defaultFileTypeShouldBeFound("fileTypeName.contains=" + DEFAULT_FILE_TYPE_NAME);

        // Get all the fileTypeList where fileTypeName contains UPDATED_FILE_TYPE_NAME
        defaultFileTypeShouldNotBeFound("fileTypeName.contains=" + UPDATED_FILE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllFileTypesByFileTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileTypeName does not contain DEFAULT_FILE_TYPE_NAME
        defaultFileTypeShouldNotBeFound("fileTypeName.doesNotContain=" + DEFAULT_FILE_TYPE_NAME);

        // Get all the fileTypeList where fileTypeName does not contain UPDATED_FILE_TYPE_NAME
        defaultFileTypeShouldBeFound("fileTypeName.doesNotContain=" + UPDATED_FILE_TYPE_NAME);
    }


    @Test
    @Transactional
    public void getAllFileTypesByFileMediumTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileMediumType equals to DEFAULT_FILE_MEDIUM_TYPE
        defaultFileTypeShouldBeFound("fileMediumType.equals=" + DEFAULT_FILE_MEDIUM_TYPE);

        // Get all the fileTypeList where fileMediumType equals to UPDATED_FILE_MEDIUM_TYPE
        defaultFileTypeShouldNotBeFound("fileMediumType.equals=" + UPDATED_FILE_MEDIUM_TYPE);
    }

    @Test
    @Transactional
    public void getAllFileTypesByFileMediumTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileMediumType not equals to DEFAULT_FILE_MEDIUM_TYPE
        defaultFileTypeShouldNotBeFound("fileMediumType.notEquals=" + DEFAULT_FILE_MEDIUM_TYPE);

        // Get all the fileTypeList where fileMediumType not equals to UPDATED_FILE_MEDIUM_TYPE
        defaultFileTypeShouldBeFound("fileMediumType.notEquals=" + UPDATED_FILE_MEDIUM_TYPE);
    }

    @Test
    @Transactional
    public void getAllFileTypesByFileMediumTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileMediumType in DEFAULT_FILE_MEDIUM_TYPE or UPDATED_FILE_MEDIUM_TYPE
        defaultFileTypeShouldBeFound("fileMediumType.in=" + DEFAULT_FILE_MEDIUM_TYPE + "," + UPDATED_FILE_MEDIUM_TYPE);

        // Get all the fileTypeList where fileMediumType equals to UPDATED_FILE_MEDIUM_TYPE
        defaultFileTypeShouldNotBeFound("fileMediumType.in=" + UPDATED_FILE_MEDIUM_TYPE);
    }

    @Test
    @Transactional
    public void getAllFileTypesByFileMediumTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileMediumType is not null
        defaultFileTypeShouldBeFound("fileMediumType.specified=true");

        // Get all the fileTypeList where fileMediumType is null
        defaultFileTypeShouldNotBeFound("fileMediumType.specified=false");
    }

    @Test
    @Transactional
    public void getAllFileTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where description equals to DEFAULT_DESCRIPTION
        defaultFileTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the fileTypeList where description equals to UPDATED_DESCRIPTION
        defaultFileTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFileTypesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where description not equals to DEFAULT_DESCRIPTION
        defaultFileTypeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the fileTypeList where description not equals to UPDATED_DESCRIPTION
        defaultFileTypeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFileTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultFileTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the fileTypeList where description equals to UPDATED_DESCRIPTION
        defaultFileTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFileTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where description is not null
        defaultFileTypeShouldBeFound("description.specified=true");

        // Get all the fileTypeList where description is null
        defaultFileTypeShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllFileTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where description contains DEFAULT_DESCRIPTION
        defaultFileTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the fileTypeList where description contains UPDATED_DESCRIPTION
        defaultFileTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllFileTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultFileTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the fileTypeList where description does not contain UPDATED_DESCRIPTION
        defaultFileTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllFileTypesByFileTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileType equals to DEFAULT_FILE_TYPE
        defaultFileTypeShouldBeFound("fileType.equals=" + DEFAULT_FILE_TYPE);

        // Get all the fileTypeList where fileType equals to UPDATED_FILE_TYPE
        defaultFileTypeShouldNotBeFound("fileType.equals=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    public void getAllFileTypesByFileTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileType not equals to DEFAULT_FILE_TYPE
        defaultFileTypeShouldNotBeFound("fileType.notEquals=" + DEFAULT_FILE_TYPE);

        // Get all the fileTypeList where fileType not equals to UPDATED_FILE_TYPE
        defaultFileTypeShouldBeFound("fileType.notEquals=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    public void getAllFileTypesByFileTypeIsInShouldWork() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileType in DEFAULT_FILE_TYPE or UPDATED_FILE_TYPE
        defaultFileTypeShouldBeFound("fileType.in=" + DEFAULT_FILE_TYPE + "," + UPDATED_FILE_TYPE);

        // Get all the fileTypeList where fileType equals to UPDATED_FILE_TYPE
        defaultFileTypeShouldNotBeFound("fileType.in=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    public void getAllFileTypesByFileTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        fileTypeRepository.saveAndFlush(fileType);

        // Get all the fileTypeList where fileType is not null
        defaultFileTypeShouldBeFound("fileType.specified=true");

        // Get all the fileTypeList where fileType is null
        defaultFileTypeShouldNotBeFound("fileType.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFileTypeShouldBeFound(String filter) throws Exception {
        restFileTypeMockMvc.perform(get("/api/file-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileType.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileTypeName").value(hasItem(DEFAULT_FILE_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].fileMediumType").value(hasItem(DEFAULT_FILE_MEDIUM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].fileTemplateContentType").value(hasItem(DEFAULT_FILE_TEMPLATE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileTemplate").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE_TEMPLATE))))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE.toString())));

        // Check, that the count call also returns 1
        restFileTypeMockMvc.perform(get("/api/file-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFileTypeShouldNotBeFound(String filter) throws Exception {
        restFileTypeMockMvc.perform(get("/api/file-types?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFileTypeMockMvc.perform(get("/api/file-types/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingFileType() throws Exception {
        // Get the fileType
        restFileTypeMockMvc.perform(get("/api/file-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileType() throws Exception {
        // Initialize the database
        fileTypeService.save(fileType);

        int databaseSizeBeforeUpdate = fileTypeRepository.findAll().size();

        // Update the fileType
        FileType updatedFileType = fileTypeRepository.findById(fileType.getId()).get();
        // Disconnect from session so that the updates on updatedFileType are not directly saved in db
        em.detach(updatedFileType);
        updatedFileType
            .fileTypeName(UPDATED_FILE_TYPE_NAME)
            .fileMediumType(UPDATED_FILE_MEDIUM_TYPE)
            .description(UPDATED_DESCRIPTION)
            .fileTemplate(UPDATED_FILE_TEMPLATE)
            .fileTemplateContentType(UPDATED_FILE_TEMPLATE_CONTENT_TYPE)
            .fileType(UPDATED_FILE_TYPE);

        restFileTypeMockMvc.perform(put("/api/file-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedFileType)))
            .andExpect(status().isOk());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeUpdate);
        FileType testFileType = fileTypeList.get(fileTypeList.size() - 1);
        assertThat(testFileType.getFileTypeName()).isEqualTo(UPDATED_FILE_TYPE_NAME);
        assertThat(testFileType.getFileMediumType()).isEqualTo(UPDATED_FILE_MEDIUM_TYPE);
        assertThat(testFileType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFileType.getFileTemplate()).isEqualTo(UPDATED_FILE_TEMPLATE);
        assertThat(testFileType.getFileTemplateContentType()).isEqualTo(UPDATED_FILE_TEMPLATE_CONTENT_TYPE);
        assertThat(testFileType.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingFileType() throws Exception {
        int databaseSizeBeforeUpdate = fileTypeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileTypeMockMvc.perform(put("/api/file-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fileType)))
            .andExpect(status().isBadRequest());

        // Validate the FileType in the database
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFileType() throws Exception {
        // Initialize the database
        fileTypeService.save(fileType);

        int databaseSizeBeforeDelete = fileTypeRepository.findAll().size();

        // Delete the fileType
        restFileTypeMockMvc.perform(delete("/api/file-types/{id}", fileType.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FileType> fileTypeList = fileTypeRepository.findAll();
        assertThat(fileTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
