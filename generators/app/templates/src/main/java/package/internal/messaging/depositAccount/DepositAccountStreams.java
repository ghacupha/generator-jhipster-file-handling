package io.github.deposits.app.messaging.depositAccount;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface DepositAccountStreams {

    String INPUT = "deposit-accounts-in";
    String OUTPUT = "deposit-accounts-out";

    @Input(INPUT)
    SubscribableChannel inbound();
    @Output(OUTPUT)
    MessageChannel outbound();
}
