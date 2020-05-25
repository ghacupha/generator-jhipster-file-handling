package <%= packageName %>.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FileUploadMapperTest {

    private FileUploadMapper fileUploadMapper;

    @BeforeEach
    public void setUp() {
        fileUploadMapper = new FileUploadMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(fileUploadMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(fileUploadMapper.fromId(null)).isNull();
    }
}
