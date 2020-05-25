package io.github.files;


import io.github.app.CustomEntityMapping;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link FileUpload} and its DTO {@link FileUploadDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FileUploadMapper extends CustomEntityMapping<FileUploadDTO, FileUpload> {



    default FileUpload fromId(Long id) {
        if (id == null) {
            return null;
        }
        FileUpload fileUpload = new FileUpload();
        fileUpload.setId(id);
        return fileUpload;
    }
}
