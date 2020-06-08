package <%= packageName %>.internal.messaging.sample;

import <%= packageName %>.internal.messaging.platform.TokenizableMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * This is a sample object being transmitted in the kafka stream
 */
@Getter
@Setter
@Builder
public class Greetings implements TokenizableMessage<String> {
    private static final long serialVersionUID = 4561591600786173783L;
    private long timestamp;
    private String message;
    private String messageToken;
    private String description;

    @Override
    public String toString() {
        return message;
    }
}
