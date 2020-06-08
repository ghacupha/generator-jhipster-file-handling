package io.github.deposits.app.messaging.sample;

import io.github.deposits.app.messaging.platform.TokenizableMessage;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
