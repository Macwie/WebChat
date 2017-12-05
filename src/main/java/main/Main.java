package main;

import backend.Controllers;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static Stage window;
    @Override
    public void start(Stage primaryStage) throws Exception{
    	window = primaryStage;
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/mainView.fxml"));
    	Parent root = (Parent) loader.load();
		Controllers.mainViewController = loader.getController();
        primaryStage.setResizable(false);
        primaryStage.setTitle("WebChat");
        primaryStage.setScene(new Scene(root, 890, 565));
        primaryStage.show();
    }


    public static void main(String[] args) {


        /*WindowCore windowCore = new WindowCore();
        windowCore.init();

        SwitchGUI switchGUI = new SwitchGUI();
        switchGUI.show();*/


        launch(args);
    }
}
