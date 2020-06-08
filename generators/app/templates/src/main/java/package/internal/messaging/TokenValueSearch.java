package io.github.deposits.app.messaging;

import io.github.deposits.service.dto.MessageTokenDTO;

/**
 * To search for Message-Token entity with a certain message-token-value
 * @param <T>
 */
public interface TokenValueSearch<T> {

    MessageTokenDTO getMessageToken(final T tokenValue);
}
