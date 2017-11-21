package backend.fxcontrollers;

import backend.ChatServer;
import backend.ServerObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class serverOptionController implements Initializable {

	String password;
	String IP;
	int port;
	String serverName;
	
    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

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
    void startServer(ActionEvent event) {
    	

      	 Parent root;
           try {
          	 Stage stage = new Stage();
          	 root = FXMLLoader.load(getClass().getResource(
                       "/layouts/serverView.fxml"));
          	password = passwordPasswordField.getText();
        	IP = ipTextField.getText();
        	port = Integer.parseInt(portTextField.getText());
        	serverName = serverNameTextField.getText();
    		ChatServer server = new ChatServer(port);
    		server.startServer();
          	 Scene scene = new Scene(root);
               stage.setScene(scene);
               stage.setTitle("Server LOG");
               stage.show();
               Stage stage1 = (Stage) StartButton.getScene().getWindow();
               stage1.hide();
               stage.show();
           }
           catch (IOException e) {
               e.printStackTrace();
           }

    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ipTextField.setText(ServerObject.getServerIP());
		
	}

}
