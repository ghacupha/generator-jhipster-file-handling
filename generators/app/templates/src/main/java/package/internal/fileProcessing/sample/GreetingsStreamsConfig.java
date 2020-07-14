package <%= packageName %>.internal.fileProcessing.sample;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(GreetingsStreams.class)
public class GreetingsStreamsConfig {
}
