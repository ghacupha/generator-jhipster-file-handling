package <%= packageName %>.internal.messaging.fileNotification;

import <%= packageName %>.internal.messaging.platform.TokenizableMessage;
import <%= packageName %>.domain.enumeration.FileModelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a notification which contains metadata of a recently uploaded which a listener might act upon
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FileNotification implements TokenizableMessage<String> {
    private static final long serialVersionUID = -6472961232578342431L;
    private String fileId;

    private long timestamp;

    private String filename;

    private String messageToken;

    private String description;

    private FileModelType fileModelType;

}
