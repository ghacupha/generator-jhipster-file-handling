package <%= packageName %>.service.dto;

import java.io.Serializable;
import java.util.Objects;
import <%= packageName %>.domain.FileType;
import <%= packageName %>.web.rest.FileTypeResource;
import <%= packageName %>.domain.enumeration.FileMediumTypes;
import <%= packageName %>.domain.enumeration.FileModelType;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link io.github.domain.FileType} entity. This class is used
 * in {@link io.github.web.rest.FileTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /file-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FileTypeCriteria implements Serializable, Criteria {
    /**
     * Class for filtering FileMediumTypes
     */
    public static class FileMediumTypesFilter extends Filter<FileMediumTypes> {

        public FileMediumTypesFilter() {
        }

        public FileMediumTypesFilter(FileMediumTypesFilter filter) {
            super(filter);
        }

        @Override
        public FileMediumTypesFilter copy() {
            return new FileMediumTypesFilter(this);
        }

    }
    /**
     * Class for filtering FileModelType
     */
    public static class FileModelTypeFilter extends Filter<FileModelType> {

        public FileModelTypeFilter() {
        }

        public FileModelTypeFilter(FileModelTypeFilter filter) {
            super(filter);
        }

        @Override
        public FileModelTypeFilter copy() {
            return new FileModelTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fileTypeName;

    private FileMediumTypesFilter fileMediumType;

    private StringFilter description;

    private FileModelTypeFilter fileType;

    public FileTypeCriteria() {
    }

    public FileTypeCriteria(FileTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fileTypeName = other.fileTypeName == null ? null : other.fileTypeName.copy();
        this.fileMediumType = other.fileMediumType == null ? null : other.fileMediumType.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.fileType = other.fileType == null ? null : other.fileType.copy();
    }

    @Override
    public FileTypeCriteria copy() {
        return new FileTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFileTypeName() {
        return fileTypeName;
    }

    public void setFileTypeName(StringFilter fileTypeName) {
        this.fileTypeName = fileTypeName;
    }

    public FileMediumTypesFilter getFileMediumType() {
        return fileMediumType;
    }

    public void setFileMediumType(FileMediumTypesFilter fileMediumType) {
        this.fileMediumType = fileMediumType;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public FileModelTypeFilter getFileType() {
        return fileType;
    }

    public void setFileType(FileModelTypeFilter fileType) {
        this.fileType = fileType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FileTypeCriteria that = (FileTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fileTypeName, that.fileTypeName) &&
            Objects.equals(fileMediumType, that.fileMediumType) &&
            Objects.equals(description, that.description) &&
            Objects.equals(fileType, that.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fileTypeName,
        fileMediumType,
        description,
        fileType
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FileTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fileTypeName != null ? "fileTypeName=" + fileTypeName + ", " : "") +
                (fileMediumType != null ? "fileMediumType=" + fileMediumType + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (fileType != null ? "fileType=" + fileType + ", " : "") +
            "}";
    }

}
