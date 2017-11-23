package backend.fxcontrollers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class chatViewController implements Initializable{
	
	String message;

    @FXML
    private Button sendButton;

    @FXML
    private TextField messageTextField;

    @FXML
    private TextArea activeUsersTextArea;

    @FXML
    private TextArea chatTextArea;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	public void addMessage(String message) {
		chatTextArea.setText("\n" + message);
	}
}
