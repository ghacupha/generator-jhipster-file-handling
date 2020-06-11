package <%= packageName %>.internal.messaging.jsonStrings;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * This is the basic configuration for json strings streams
 */
public interface JsonStringStreams {

    // todo configure these topics in the application properties
    String INBOUND = "json-deposits-create-inbound";
    String OUTBOUND = "json-deposits-create-outbound";

    @Input(INBOUND)
    SubscribableChannel depositsCreateInbound();

    @Output(OUTBOUND)
    MessageChannel depositsCreateOutbound();
}
