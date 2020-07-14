package <%= packageName %>.internal.fileProcessing.fileNotification;

import org.springframework.cloud.stream.annotation.EnableBinding;
import <%= packageName %>.internal.fileProcessing.fileNotification.FileNotificationStreams;

@EnableBinding(FileNotificationStreams.class)
public class FileNotificationStreamsConfig {
}
