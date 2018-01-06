package backend.messages;

import javafx.scene.text.TextFlow;

public interface iMessage {

    void show(TextFlow chatBox, TextFlow activeUsers);
}
