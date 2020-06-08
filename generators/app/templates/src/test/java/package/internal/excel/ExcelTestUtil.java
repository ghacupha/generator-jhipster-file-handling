package io.github.deposits.app.excel;

import com.poiji.option.PoijiOptions;
import com.poiji.option.PoijiOptions.PoijiOptionsBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
public class ExcelTestUtil {

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
