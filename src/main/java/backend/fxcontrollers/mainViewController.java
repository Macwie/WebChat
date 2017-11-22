package backend.fxcontrollers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class mainViewController implements Initializable{

    @FXML
    private ImageView logo;

    @FXML
    private Label desc;

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
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), logo);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(0.95);
        fadeIn.play();
        fadeIn.setOnFinished(event -> {
            FadeTransition fadeIn1 = new FadeTransition(Duration.seconds(1), startClientButton);
            fadeIn1.setFromValue(0.0);
            fadeIn1.setToValue(0.95);
            fadeIn1.play();
            fadeIn1 = new FadeTransition(Duration.seconds(1), startServerButton);
            fadeIn1.setFromValue(0.0);
            fadeIn1.setToValue(0.95);
            fadeIn1.play();
            fadeIn1 = new FadeTransition(Duration.seconds(1), desc);
            fadeIn1.setFromValue(0.0);
            fadeIn1.setToValue(0.95);
            fadeIn1.play();
        });
		
	}
}
