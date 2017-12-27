package backend.messages;

import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.Serializable;

/**
 * Created by Maciek on 13.12.2017.
 */
public class Message implements Serializable,iMessage {

    private String nick;
    private String message;
    private String style = "-fx-font-family: Calibri; -fx-font-weight: bold; -fx-font-size: 20px; -fx-fill: white;";

    public Message(String nick, String message) {
        this.nick = nick;
        this.message = message;
    }

    public Message(Message msg) {
        this.nick = msg.nick;
        this.message = msg.message;
    }

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public void show(TextFlow chatBox) {
        Text t_nick = new Text(nick);
        Text t_msg = new Text(": "+message);
        t_nick.setStyle(style);
        t_msg.setStyle(style);

        Platform.runLater(() -> {
            chatBox.getChildren().addAll(t_nick, t_msg);
        });

    }
}
