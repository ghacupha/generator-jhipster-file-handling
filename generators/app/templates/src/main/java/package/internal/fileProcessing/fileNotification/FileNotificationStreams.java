package <%= packageName %>.internal.fileProcessing.fileNotification;

import <%= packageName %>.internal.fileProcessing.platform.MessageStreams;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Configuration of topics for file-upload notification.
 * <p/>
 * Please check application-properties configuration to ensure the
 * <p/>
 * topics have been configured.
 * <p/>
 * @return
 */
public interface FileNotificationStreams extends MessageStreams {

    String INPUT = "file-notifications-in";
    String OUTPUT = "file-notifications-out";

    @Input(INPUT)
    SubscribableChannel inbound();
    @Output(OUTPUT)
    MessageChannel outbound();
}
