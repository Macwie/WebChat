package backend.fxcontrollers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
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
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import backend.Controllers;

public class mainViewController implements Initializable{
	private Stage window;

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
        	
        	
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/serverView.fxml"));
        	root = (Parent) loader.load();
			Controllers.serverViewController = loader.getController();
       	    Scene scene = new Scene(root);
       	 	window = Main.window;
            window.setScene(scene);
            window.setTitle("WebChat");
            window.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    @FXML
    public void startClient() {
   	 Parent root;
        try {
        	
        	
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ClientView.fxml"));
        	root = (Parent) loader.load();
			Controllers.clientViewController = loader.getController();
       	    Scene scene = new Scene(root);
       	 	window = Main.window;
            window.setScene(scene);
            window.setTitle("WebChat");
            window.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.2), logo);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(0.95);
        fadeIn.play();
        fadeIn.setOnFinished(event -> {
            FadeTransition fadeIn2 = new FadeTransition(Duration.seconds(0.5), desc);
            fadeIn2.setFromValue(0.0);
            fadeIn2.setToValue(0.95);
            fadeIn2.play();
            fadeIn2.setOnFinished(event1 -> {
                FadeTransition fadeIn1 = new FadeTransition(Duration.seconds(1), startClientButton);
                fadeIn1.setFromValue(0.0);
                fadeIn1.setToValue(0.95);
                fadeIn1.play();
                fadeIn1 = new FadeTransition(Duration.seconds(1), startServerButton);
                fadeIn1.setFromValue(0.0);
                fadeIn1.setToValue(0.95);
                fadeIn1.play();
            });
        });
		
	}
}
