package io.github.deposits.app.messaging.schemeTable;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface SchemeTableStreams {

    String INPUT = "scheme-table-in";
    String OUTPUT = "scheme-table-out";

    @Input(INPUT)
    SubscribableChannel inbound();
    @Output(OUTPUT)
    MessageChannel outbound();
}
