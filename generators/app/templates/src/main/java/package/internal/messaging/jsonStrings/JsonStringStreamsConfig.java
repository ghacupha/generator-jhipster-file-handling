package <%= packageName %>.internal.messaging.jsonStrings;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(JsonStringStreams.class)
public class JsonStringStreamsConfig {
}