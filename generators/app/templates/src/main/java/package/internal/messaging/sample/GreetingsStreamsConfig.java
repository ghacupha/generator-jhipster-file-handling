package <%= packageName %>.internal.messaging.sample;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(GreetingsStreams.class)
public class GreetingsStreamsConfig {
}
