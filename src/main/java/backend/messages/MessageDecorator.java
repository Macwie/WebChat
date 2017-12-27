package backend.messages;

import javafx.scene.text.TextFlow;

public abstract class MessageDecorator implements iMessage {

    protected iMessage message;

    public MessageDecorator(iMessage message) {
        this.message = message;
    }

    public void show(TextFlow chatBox) {
        message.show(chatBox);
    }

}
