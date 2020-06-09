package <%= packageName %>.internal.excel;

import <%= packageName %>.<%= appName %>App;
import <%= packageName %>.internal.model.SchemeTableEVM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static <%= packageName %>.internal.excel.ExcelTestUtil.readFile;
import static <%= packageName %>.internal.excel.ExcelTestUtil.toBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = <%= appName %>App.class)
public class ExcelFileUtilsIT {

    @Autowired
    private  ExcelFileDeserializer<SchemeTableEVM> schemeTableExcelFileDeserializer;

    @Test
    public void deserializeSchemesFile() throws Exception {

        // @formatter:off
        List<SchemeTableEVM> schemes =
            schemeTableExcelFileDeserializer.deserialize(toBytes(readFile("schemes.xlsx")));
        // @formatter:on

        assertThat(schemes.size()).isEqualTo(3);
        assertThat(schemes.get(0)).isEqualTo(SchemeTableEVM.builder().rowIndex(1).schemeCode("scheme1").description("scheme1description").build());
        assertThat(schemes.get(1)).isEqualTo(SchemeTableEVM.builder().rowIndex(2).schemeCode("scheme2").description("scheme2description").build());
        assertThat(schemes.get(2)).isEqualTo(SchemeTableEVM.builder().rowIndex(3).schemeCode("scheme3").description("scheme3description").build());
    }
}
