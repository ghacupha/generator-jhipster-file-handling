package <%= packageName %>.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import <%= packageName %>.web.rest.TestUtil;

public class FileUploadTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileUpload.class);
        FileUpload fileUpload1 = new FileUpload();
        fileUpload1.setId(1L);
        FileUpload fileUpload2 = new FileUpload();
        fileUpload2.setId(fileUpload1.getId());
        assertThat(fileUpload1).isEqualTo(fileUpload2);
        fileUpload2.setId(2L);
        assertThat(fileUpload1).isNotEqualTo(fileUpload2);
        fileUpload1.setId(null);
        assertThat(fileUpload1).isNotEqualTo(fileUpload2);
    }
}
