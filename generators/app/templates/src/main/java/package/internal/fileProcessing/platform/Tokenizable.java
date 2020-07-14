package <%= packageName %>.internal.fileProcessing.platform;

import java.io.Serializable;

/**
 * This transfer object contains a message token
 *
 * @param <T> Data type for token
 */
public interface Tokenizable<T> extends Serializable {

    T getMessageToken();

    void setMessageToken(T messageToken);
}
