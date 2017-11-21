package backend.fxcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainViewController implements Initializable{

    @FXML
    private Button startServerButton;

    @FXML
    private Button startClientButton;

    @FXML
    private ImageView startServerImage;

    @FXML
    private ImageView startClientImage;

    @FXML
    private void startServer(ActionEvent event) {
   	 Parent root;
        try {
       	 Stage stage = new Stage();
       	 root = FXMLLoader.load(getClass().getResource(
                    "/layouts/serverOptionView.fxml"));
       	 Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Server Settings");
            stage.show();
            Stage stage1 = (Stage) startServerButton.getScene().getWindow();
            stage1.hide();
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
