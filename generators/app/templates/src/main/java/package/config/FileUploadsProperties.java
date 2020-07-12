package <%= packageName %>.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configurations related to file-upload process
 */
@Configuration
@ConfigurationProperties(prefix = "reader")
@PropertySource(value = "classpath:config/fileUploads.yml", factory = FileUploadsPropertyFactory.class)
public class FileUploadsProperties {

    private int listSize;

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public int getListSize() {
        return listSize;
    }
}
