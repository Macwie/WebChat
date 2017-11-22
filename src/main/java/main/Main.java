package main;

import frontend.SwitchGUI;
import frontend.WindowCore;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/layouts/mainView.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 900, 565));
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
