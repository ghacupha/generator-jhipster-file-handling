package io.github.deposits.app.messaging.jsonStrings;

import io.github.deposits.app.messaging.platform.TokenizableMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class StringMessageDTO implements TokenizableMessage<String>, Serializable {
    private static final long serialVersionUID = -8758172609583254950L;
    private String jsonString;
    private String description;
    private String messageToken;
    private long timestamp;
}
