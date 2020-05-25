package <%= packageName %>.repository;

import <%= packageName %>.domain.FileUpload;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FileUpload entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long>, JpaSpecificationExecutor<FileUpload> {
}
