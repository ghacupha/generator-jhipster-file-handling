package io.github.deposits.app.messaging.sample;

import io.github.deposits.app.messaging.platform.MuteListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GreetingsListener implements MuteListener<Greetings> {

    @StreamListener(GreetingsStreams.INPUT)
    public void handleMessage(@Payload Greetings greetings) {
        log.info("Received greetings : {}", greetings);
    }
}
