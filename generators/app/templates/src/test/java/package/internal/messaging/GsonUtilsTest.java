package io.github.deposits.app.messaging;

import io.github.deposits.app.messaging.fileNotification.FileNotification;
import io.github.deposits.app.messaging.jsonStrings.GsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GsonUtilsTest {

    private List<FileNotification> fileNotifications;
    private long timeStamp;
    private String arrayString;

    @BeforeEach
    void setUp() {
        fileNotifications = new ArrayList<>();
        timeStamp = 1573720827609l;

        fileNotifications.add(FileNotification.builder().fileId("1001").timestamp(timeStamp).description("Test-file-notification-1").build());
        fileNotifications.add(FileNotification.builder().fileId("1002").timestamp(timeStamp).description("Test-file-notification-2").build());

        arrayString = "[{\"fileId\":\"1001\",\"timestamp\":1573720827609,\"description\":\"Test-file-notification-1\"},{\"fileId\":\"1002\",\"timestamp\":1573720827609," +
            "\"description\":\"Test-file-notification-2\"}]";

    }

    @Test
    void toJsonString() {

        String jsonString = GsonUtils.toJsonString(fileNotifications);
        String arrayString = "[{\"fileId\":\"1001\",\"timestamp\":1573720827609,\"description\":\"Test-file-notification-1\"},{\"fileId\":\"1002\",\"timestamp\":1573720827609," +
            "\"description\":\"Test-file-notification-2\"}]";

        assertThat(jsonString).isEqualTo(arrayString);
    }

    @Test
    void toList() {
        List<FileNotification> parsedNotifications = GsonUtils.stringToList(arrayString, FileNotification[].class);
        assertThat(parsedNotifications.get(0)).isEqualTo(fileNotifications.get(0));
        assertThat(parsedNotifications.get(1)).isEqualTo(fileNotifications.get(1));
    }
}
