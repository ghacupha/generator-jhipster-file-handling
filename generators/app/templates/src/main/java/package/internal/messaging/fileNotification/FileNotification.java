package io.github.deposits.app.messaging.fileNotification;

import io.github.deposits.app.messaging.platform.TokenizableMessage;
import io.github.deposits.domain.enumeration.FileModelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
