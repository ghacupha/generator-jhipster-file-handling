package <%= packageName %>.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import <%= packageName %>.web.rest.TestUtil;

public class FileTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileType.class);
        FileType fileType1 = new FileType();
        fileType1.setId(1L);
        FileType fileType2 = new FileType();
        fileType2.setId(fileType1.getId());
        assertThat(fileType1).isEqualTo(fileType2);
        fileType2.setId(2L);
        assertThat(fileType1).isNotEqualTo(fileType2);
        fileType1.setId(null);
        assertThat(fileType1).isNotEqualTo(fileType2);
    }
}
