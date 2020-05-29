package <%= packageName %>.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import <%= packageName %>.web.rest.TestUtil;

public class FileUploadDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileUploadDTO.class);
        FileUploadDTO fileUploadDTO1 = new FileUploadDTO();
        fileUploadDTO1.setId(1L);
        FileUploadDTO fileUploadDTO2 = new FileUploadDTO();
        assertThat(fileUploadDTO1).isNotEqualTo(fileUploadDTO2);
        fileUploadDTO2.setId(fileUploadDTO1.getId());
        assertThat(fileUploadDTO1).isEqualTo(fileUploadDTO2);
        fileUploadDTO2.setId(2L);
        assertThat(fileUploadDTO1).isNotEqualTo(fileUploadDTO2);
        fileUploadDTO1.setId(null);
        assertThat(fileUploadDTO1).isNotEqualTo(fileUploadDTO2);
    }
}
