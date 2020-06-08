package <%= packageName %>.service.mapper;

import <%= packageName %>.domain.FileUpload;
import <%= packageName %>.service.dto.FileUploadDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link FileUpload} and its DTO {@link FileUploadDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FileUploadMapper extends EntityMapper<FileUploadDTO, FileUpload> {

    default FileUpload fromId(Long id) {
        if (id == null) {
            return null;
        }
        FileUpload fileUpload = new FileUpload();
        fileUpload.setId(id);
        return fileUpload;
    }
}
