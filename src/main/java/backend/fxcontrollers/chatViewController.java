package backend.fxcontrollers;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

import backend.Client;
import backend.ClientsDAO;
import backend.Controllers;
import frontend.ClientGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class chatViewController implements Initializable{

    public boolean ready = false;
    private HashMap<String, String> colors;

	private String style = "-fx-font-family: Calibri; -fx-font-weight: bold; -fx-font-size: 20px;";
	private String msg_style = "-fx-font-family: Calibri; -fx-font-size: 20px;";

	@FXML
	private Label server_name;

    @FXML
    private Button sendButton;

    @FXML
    private TextField messageTextField;

    @FXML
    private TextFlow activeUsersTextFlow;

    @FXML
    private TextFlow chatTextFlow;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private ScrollPane scrollPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Runtime.getRuntime().addShutdownHook(new Thread() // usun usera z listy online w przypadku wyjscia przez X
				{
					@Override
					public void run() {
						ClientsDAO clientsDAO = new ClientsDAO();
						clientsDAO.removeClient(Controllers.mConnectionViewController.serverId, Controllers.mConnectionViewController.nick);
						clientsDAO.updateCurrentUsers(Controllers.mConnectionViewController.serverId, false);
					}
				});

	    colors = new HashMap<>();

        if(mConnectionViewController.IP != null)
            server_name.setText("IP: "+mConnectionViewController.IP);
        else
            server_name.setText("IP: z listy TODO");
		
	}
	
	public String getInput() {
		return messageTextField.getText();
	}

	public void clearInput() {
		messageTextField.setText("");
	}

	public void send() {
		ready = true;
	}

	public void addMessage(String message) {
        Platform.runLater(() -> {
            String[] split = message.split(":");

            if(!(split[1].length() > 1))
                return;

            Text nick = new Text(split[0]);
            if(colors.get(split[0]) != null)
                nick.setStyle(style+" -fx-fill: "+colors.get(split[0])+";");
            else
                nick.setStyle(style+" -fx-fill: white;");
            Text msg = new Text(": "+split[1]+"\n");
            msg.setStyle(style+" -fx-fill: white;");
            chatTextFlow.getChildren().addAll(nick, msg);
            chatScrollPane.setVvalue(1.0);
        });
		//chatTextArea.setText(chatTextArea.getText() +"\n" + message);
	}

	public void addUsers(String user, String color) {
		//activeUsersTextArea.setText(activeUsersTextArea.getText() +"\n" + user);
        Platform.runLater(() -> {
            Text temp = new Text(user+"\n");
            temp.setStyle(style+" -fx-fill: "+color+";");
            colors.put(user, color);
            activeUsersTextFlow.getChildren().add(temp);
        });
	}

	public void clearUsers() {
		//activeUsersTextArea.setText("");
        Platform.runLater(() -> {
		    activeUsersTextFlow.getChildren().clear();
        });
	}
	
	public void exitChat() {
		ClientsDAO clientsDAO = new ClientsDAO();
		clientsDAO.removeClient(Controllers.mConnectionViewController.serverId, Controllers.mConnectionViewController.nick);
		clientsDAO.updateCurrentUsers(Controllers.mConnectionViewController.serverId, false);
		Client.stopClient();
		Controllers.mainViewController.startClient();
	}
}
