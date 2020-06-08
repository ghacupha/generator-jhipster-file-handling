package <%= packageName %>.internal.messaging.platform;

/**
 * This is a marker of an interface which might turn out to be
 * streams processor
 *
 * @param <Payload>
 * @param <Response>
 */
public interface ResponsiveListener<Payload, Response> extends MuteListener<Payload> {

    Response attendMessage(Payload payload);

    default void handleMessage(Payload payload) {
        this.attendMessage(payload);
    }
}
