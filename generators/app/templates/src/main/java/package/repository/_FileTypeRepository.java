package <%= packageName %>.repository;

import <%= packageName %>.domain.FileType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FileType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileTypeRepository extends JpaRepository<FileType, Long>, JpaSpecificationExecutor<FileType> {
}
