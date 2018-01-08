package main;

import backend.fxcontrollers.ChatController;
import backend.fxcontrollers.Controllers;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static Stage WINDOW;


    @Override
    public void start(Stage primaryStage) throws Exception{
    	WINDOW = primaryStage;
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/GreetingView.fxml"));
    	Parent root = loader.load();
		Controllers.GreetingController = loader.getController();
        primaryStage.setResizable(false);
        primaryStage.setTitle("WebChat");
        primaryStage.setScene(new Scene(root, 890, 565));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> {   //zakoncz wszystkie watki jak zamyka sie aplkacje Xem

            if(WINDOW.getUserData() != null && WINDOW.getUserData().equals("Chat")) {   //do stuff on ChatView X end
                ChatController.exit();
            }else{  //end all threads on every other view
                Platform.exit();
                System.exit(0);
            }

        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
