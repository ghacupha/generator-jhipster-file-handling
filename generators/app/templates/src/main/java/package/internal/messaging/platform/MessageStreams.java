package <%= packageName %>.internal.messaging.platform;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * This interface is mostly a marker to ensure a form that corresponds to
 * <p>
 * typical stream definition while real implementation are created by proxy at
 * <p>
 * runtime
 */
public interface MessageStreams {

    SubscribableChannel inbound();

    MessageChannel outbound();
}
