package backend.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import backend.Controllers;
import backend.FXTableGenerator;
import backend.ServerObject;
import javafx.animation.FadeTransition;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ClientViewController implements Initializable{
	//private ServersDAO db;
	private ArrayList<ServerObject> list;
	Stage window ;

	    @FXML
        private Pane progress;

	   @FXML
	    private Button connectManuallyButton;

	    @FXML
	    private Button switchonlineButton;

	    @FXML
	    private TableView<ServerObject> ServerTable;

	    @FXML
	    private TableColumn<?, ?> ipColumn;

	    @FXML
	    private TableColumn<?, ?> portColumn;

	    @FXML
	    private TableColumn<?, ?> usersColumn;

	    @FXML
	    private TableColumn<?, ?> protectedColumn;

	    @FXML
	    private TableColumn<?, ?> statusColumn;
	    
	    @FXML
	    private TableColumn<?, ?> nameColumn;
    
    
    public ArrayList<ServerObject> getList() {
			return list;
		}
    
    
    
    
    @FXML
    private void startMConnection(ActionEvent event) {
   	 Parent root;
        try {
        	
        	window = new Stage();
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/mConnectionView.fxml"));
        	root = (Parent) loader.load();
			Controllers.mConnectionViewController = loader.getController();
       	 	Scene scene = new Scene(root);
       	 	window.initModality(Modality.APPLICATION_MODAL);
            window.setScene(scene);
            window.setTitle("Server Settings");
            window.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void close() {
    	window.close();
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {

        progress.setTranslateY(200.0);
        progress.setTranslateX(400.0);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        portColumn.setCellValueFactory(new PropertyValueFactory<>("port"));
        usersColumn.setCellValueFactory(new PropertyValueFactory<>("current"));
        protectedColumn.setCellValueFactory(new PropertyValueFactory<>("protect"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("online"));

        final FXTableGenerator gen = new FXTableGenerator(ServerTable);

        //Here you tell your progress indicator is visible only when the service is runing
        progress.visibleProperty().bind(gen.runningProperty());
        gen.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                ServerTable = gen.getValue();   //here you get the return value of your service
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.0), ServerTable);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(0.95);
                fadeIn.play();
            }
        });

        gen.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                System.out.println("FAILED TO LOAD DB DATA");
            }
        });
        gen.restart(); //here you start your service

	}
    
    

}
