package backend.messages;

import javafx.scene.text.TextFlow;

public class MessageCensorDecorator extends MessageDecorator {

    private Strategy strategy;

    public MessageCensorDecorator(Message message, Strategy strategy) {
        super(message);
        this.strategy = strategy;
        strategy.censor(message);
    }

    @Override
    public void show(TextFlow chatBox, TextFlow activeUsers) {
        super.show(chatBox, activeUsers);
    }
}
