package backend.fxcontrollers;

import backend.server.Server;
import backend.database.ServersDAO;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

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

	    //Pass data from inputs
        String password = passwordPasswordField.getText();
        String IP = ipTextField.getText();
        String serverName = serverNameTextField.getText();
        int port = Integer.parseInt(portTextField.getText());

        //Start server and it's logs
        serverLogArea.setText("Server: " + IP + " online on port: " + port + "\nName:" + serverName + " Password:" + password + "\nWaiting for clients ...");
		Server server = Server.getInstance(port);
		server.start(serverLogArea);

		//Adding new server to DB
		if (IP.equals(getPublicIP())) {
			ServersDAO serversDAO = ServersDAO.getInstance();
			serversDAO.addServer(serverName, IP, Integer.toString(port), password, true);
		} else {
			ServersDAO serversDAO = ServersDAO.getInstance();
			serversDAO.addServer(serverName, IP, Integer.toString(port), password, false);
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	    //Get public IP address as default server address
		ipTextField.setText(getPublicIP());


		//Animations
		Object[] inputs = new Object[]{
		    StartButton, ipLabel, serverNameLabel, portLabel, passwordLabel, ipTextField, serverNameTextField, portTextField,
                passwordPasswordField, serverLogArea
        };

		for (int i=0; i<inputs.length; i++)
        {
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.0), (Node) inputs[i]);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(0.95);
            fadeIn.play();
        }

	}

    private String getPublicIP() {
        URL whatismyip;
        String ip = "localhost";
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            ip = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ip;
    }

}
