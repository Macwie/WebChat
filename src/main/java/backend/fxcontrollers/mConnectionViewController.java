package backend.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import backend.Client;
import backend.ClientsDAO;
import backend.Controllers;
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
import main.Main;

public class mConnectionViewController implements Initializable {

	
	static String IP;
	static String port;
	static String nick;
	static String password;
	private ArrayList<ServerObject> list;
    @FXML
    private Button openChatButton;

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextLabel;

    @FXML
    private Label ipLabel;

    @FXML
    private Label portLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label nickLabel;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private TextField nickTextField;

    @FXML
    private void startChat(ActionEvent event) {
    	
    	password = passwordPasswordField.getText();
		IP = ipTextField.getText();
		port = portTextLabel.getText();
		password = passwordPasswordField.getText();
    	
   	 Parent root;
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/chatView.fxml"));
        	root = (Parent) loader.load();
			Controllers.chatViewController = loader.getController();
       	 Scene scene = new Scene(root);
       	 	Stage window = Main.window;
            window.setScene(scene);
            window.setTitle("Server Settings");
            int port2 = Integer.parseInt(port);
            Client client = new Client(nick, IP, port2);
            ClientsDAO clientsDAO = new ClientsDAO();   //dodawanie do online list
            
            ServerObject server = null;
			for (ServerObject s : list) {
				System.out.println(s.getPort() + " " + s.getIp());
				if (s.getPort().equals(port) && s.getIp().equals(IP)) {
					server = s;
					break;
				}}
			
		    System.out.println(server.getId());
            clientsDAO.addClient(server.getId(), nick);
            clientsDAO.updateCurrentUsers(server.getId(), true);
            clientsDAO.startUpdatingUsers(server.getId());
            client.startClient();
            Controllers.clientViewController.close();
            window.show();
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		list = Controllers.clientViewController.getList();
		
	}
	
	
	

}
