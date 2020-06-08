package <%= packageName %>.internal.messaging.platform;

/**
 * This is an abstraction for sending a services into a queue. It is expected that the implemntation internally has a way of persisting message-tokens generated
 *
 * @param <T> Type of services
 * @param <R> Type of response from service
 */
public interface MessageService<T, R> {

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    R sendMessage(final T message);
}
