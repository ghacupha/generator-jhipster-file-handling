package <%= packagename %>.internal.messaging.jsonStrings;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface JsonStringStreams {
    String INBOUND = "json-deposits-create-inbound";
    String OUTBOUND = "json-deposits-create-outbound";

    @Input(INBOUND)
    SubscribableChannel depositsCreateInbound();

    @Output(OUTBOUND)
    MessageChannel depositsCreateOutbound();
}
