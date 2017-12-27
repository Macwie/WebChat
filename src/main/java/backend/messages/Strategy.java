package backend.messages;

import backend.messages.Message;

public interface Strategy {
    void censor(Message message);
}
