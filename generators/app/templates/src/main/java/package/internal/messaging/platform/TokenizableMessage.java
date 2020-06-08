package <%= packageName %>.internal.messaging.platform;

/**
 * This is a message that is tokenizable that contains description and timestamp as well
 *
 * @param <T>
 */
public interface TokenizableMessage<T> extends Tokenizable<T> {

    String getDescription();

    long getTimestamp();

    void setTimestamp(long timestamp);

    void setDescription(String description);
}
