package <%= packageName %>.internal.messaging.platform;

/**
 * This is mostly a marker and corresponds to listeners who give no response
 * <p>
 * values for a given single-value parameter here referred to as the payload
 */
public interface MuteListener<Payload> {

    void handleMessage(Payload payload);
}
