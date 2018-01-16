package backend.messages;

import backend.database.ClientsDAO;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maciek on 13.12.2017.
 */
public class Message implements Serializable, iMessage {

    private String nick;
    private String message;
    private String msg_style = "-fx-font-family: Calibri; -fx-font-weight: bold; -fx-font-size: 20px; -fx-fill: white;";
    private String nick_style = "-fx-font-family: Calibri; -fx-font-weight: bold; -fx-font-size: 20px;";
    private HashMap<String, String> usersMap;    //nick, color of active users

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
    public void show(TextFlow chatBox, TextFlow activeUsers) {

        if (nick.equals("JqK9ZG5TSabOAND81Clp")) //Update current active users
        {
            usersMap = ClientsDAO.getInstance().getAllClients();  //download current users
            Platform.runLater(() -> {
                activeUsers.getChildren().clear();
                for (Map.Entry<String, String> entry : usersMap.entrySet()) {
                    Text user = new Text(entry.getKey() + "\n");
                    user.setStyle(nick_style + " -fx-fill: " + entry.getValue() + ";");
                    activeUsers.getChildren().add(user);
                }
            });
        } else {
            usersMap = ClientsDAO.getInstance().getUsersMap();      //get current users
            //Style message
            Text t_nick = new Text(nick);
            Text t_msg = new Text(": " + message);
            t_nick.setStyle(nick_style + " -fx-fill: " + usersMap.get(nick) + ";");
            t_msg.setStyle(msg_style);
            //Display message
            Platform.runLater(() -> {
                chatBox.getChildren().addAll(t_nick, t_msg);
            });
        }
    }
}
