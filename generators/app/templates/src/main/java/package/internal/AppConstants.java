package <%= packageName %>.internal;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AppConstants {

    // Regex for acceptable logins
    public static final String EMAIL_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final int TOKEN_BYTE_LENGTH = 20;
    public static final String DATETIME_FORMAT = "yyyy/MM/dd";
    public static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    public static final List<String> PROCESSED_TOKENS = new CopyOnWriteArrayList<>();
    public static final List<String> ENQUEUED_TOKENS = new CopyOnWriteArrayList<>();
    public static final List<String> ENQUEUED_DEPOSIT_TOKENS = new CopyOnWriteArrayList<>();
    public static final int MAX_DEPOSIT_UPDATE_SIZE = 5000;

    public static final int LIST_PAGE_SIZE = 5000;
    public static final int LIST_PAGE_SIZE_TABLES = 5;

    private AppConstants() {
    }
}
