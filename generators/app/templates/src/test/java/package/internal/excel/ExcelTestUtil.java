package <%= packageName %>.internal.excel;

import com.poiji.option.PoijiOptions;
import com.poiji.option.PoijiOptions.PoijiOptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ExcelTestUtil {

    private final static Logger log = LoggerFactory.getLogger(ExcelTestUtil.class);

    public static byte[] toBytes(File file) throws IOException {

        return Files.readAllBytes(file.toPath());
    }


    public static PoijiOptions getDefaultPoijiOptions() {

        // @formatter:off
        return PoijiOptionsBuilder.settings()
                           .ignoreHiddenSheets(true)
                           .preferNullOverDefault(true)
                           .datePattern("yyyy/MM/dd")
                           .dateTimeFormatter(DateTimeFormatter.ISO_DATE_TIME)
                           .build();
        // @formatter:on
    }

    public static File readFile(String filename) {

        log.info("\nReading file : {}...", filename);

    // @formatter:off
    return new File(
            Objects.requireNonNull(
                ClassLoader.getSystemClassLoader()
                           .getResource("files/" + filename))
                   .getFile());
    // @formatter:on
    }

}
