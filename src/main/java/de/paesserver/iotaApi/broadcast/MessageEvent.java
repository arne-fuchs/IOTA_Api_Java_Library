package de.paesserver.iotaApi.broadcast;

import de.paesserver.iotaApi.message.Message;

public interface MessageEvent {
    void receivedMessageEvent(Message message);
}
