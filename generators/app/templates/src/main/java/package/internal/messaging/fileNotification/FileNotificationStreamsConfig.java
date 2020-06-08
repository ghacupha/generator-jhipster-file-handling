package <%= packageName %>.app.messaging.fileNotification;

import org.springframework.cloud.stream.annotation.EnableBinding;
import <%= packageName %>.internal.messaging.fileNotification.FileNotificationStreams

@EnableBinding(FileNotificationStreams.class)
public class FileNotificationStreamsConfig {
}
