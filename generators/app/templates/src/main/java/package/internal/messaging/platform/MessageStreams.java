package <%= packageName %>.internal.messaging.platform;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * This interfce is mostly a marker to ensure a form that corresponds to
 * typical stream definition while real implementation are created by proxy at
 * runtime
 */
public interface MessageStreams {

    SubscribableChannel inbound();

    MessageChannel outbound();
}
