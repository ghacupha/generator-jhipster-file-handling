package <%= packageName %>.internal.messaging.sample;

import <%= packageName %>.internal.messaging.platform.MuteListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * This is an example of processed message received from the stream
 *
 */
@Component
@Slf4j
public class GreetingsListener implements MuteListener<Greetings> {

    @StreamListener(GreetingsStreams.INPUT)
    public void handleMessage(@Payload Greetings greetings) {
        log.info("Received greetings : {}", greetings);
    }
}
