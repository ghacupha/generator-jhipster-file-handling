package <%= packageName %>.internal.messaging.sample;

import <%= packageName %>.internal.messaging.platform.MessageStreams;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Test configuration for greetings streams. Check the
 * <p/>
 * application-properties to ensure this topics are configured
 */
public interface GreetingsStreams extends MessageStreams {

    // TODO Add these topics to the application properties
    String INPUT = "greetings-in";
    String OUTPUT = "greetings-out";

    @Input(INPUT)
    SubscribableChannel inbound();
    @Output(OUTPUT)
    MessageChannel outbound();
}
