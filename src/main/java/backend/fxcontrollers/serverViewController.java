package backend.fxcontrollers;

import backend.ChatServer;
import backend.Controllers;
import backend.ServerObject;
import backend.ServersDAO;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class serverViewController implements Initializable {

	Stage window;

	static String password;
	static String IP;
	static int port;
	static String serverName;
	private String serverLogs = "";

	@FXML
	private Button StartButton;

	@FXML
	private Label ipLabel;

	@FXML
	private Label serverNameLabel;

	@FXML
	private Label portLabel;

	@FXML
	private Label passwordLabel;

	@FXML
	private TextField ipTextField;

	@FXML
	private TextField serverNameTextField;

	@FXML
	private TextField portTextField;

	@FXML
	private PasswordField passwordPasswordField;

	@FXML
	private TextArea serverLogArea;

	@FXML
	void startServer() {
		password = passwordPasswordField.getText();
		IP = ipTextField.getText();
		port = Integer.parseInt(portTextField.getText());
		serverName = serverNameTextField.getText();

		ChatServer server = new ChatServer(port);
		server.startServer();
		if (IP.equals(ServerObject.getServerIP())) {
			ServersDAO serversDAO = ServersDAO.getInstance();
			serversDAO.addServer(serverName, IP, Integer.toString(port), password, true);
		} else {
			ServersDAO serversDAO = ServersDAO.getInstance();
			serversDAO.addServer(serverName, IP, Integer.toString(port), password, false);
		}
		
		serverLogs = " Server: " + IP + " online on port: " + port + "\n Name:" + serverName + " Password:" + password;
		serverLogArea.setText(serverLogs);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ipTextField.setText(ServerObject.getServerIP());

		Object[] test = new Object[]{
		    StartButton, ipLabel, serverNameLabel, portLabel, passwordLabel, ipTextField, serverNameTextField, portTextField,
                passwordPasswordField, serverLogArea
        };

		for (int i=0; i<test.length; i++)
        {
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.0), (Node) test[i]);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(0.95);
            fadeIn.play();
        }

	}

	public void addLog(String log) {
		serverLogArea.setText(serverLogs + "\n" + log);
	}

}
