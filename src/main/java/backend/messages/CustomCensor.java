package backend.messages;

import backend.messages.Message;
import backend.messages.Strategy;

public class CustomCensor implements Strategy {
    @Override
    public void censor(Message message) {
        message.setNick("CustomCensor");
    }
}
