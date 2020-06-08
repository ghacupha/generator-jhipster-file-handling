package io.github.deposits.app.messaging.sample;

import io.github.deposits.app.messaging.platform.MessageStreams;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Test configuration for greetings streams
 */
public interface GreetingsStreams extends MessageStreams {

    String INPUT = "greetings-in";
    String OUTPUT = "greetings-out";

    @Input(INPUT)
    SubscribableChannel inbound();
    @Output(OUTPUT)
    MessageChannel outbound();
}
