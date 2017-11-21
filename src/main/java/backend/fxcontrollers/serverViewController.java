package backend.fxcontrollers;

import backend.fxcontrollers.serverOptionController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class serverViewController implements Initializable {

	private static String serverLogs = "";
	@FXML
    private serverOptionController sOc;
    @FXML
    private TextArea logTextArea;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		serverLogs = " Server: " + sOc.getIP() + " online on port: " + sOc.getPort() + "\n Name:" + sOc.getServerName() + " Password:" + sOc.getPassword();
		
	}
    
    
}
