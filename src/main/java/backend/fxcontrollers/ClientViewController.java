package backend.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import backend.Controllers;
import backend.FXTableGenerator;
import backend.ServerObject;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

public class ClientViewController implements Initializable{

	    private Stage window;
	    private static VBox vbox;
	    public static boolean onOrOf;  //true to all false to online

	    private ServerObject s;
	    @FXML
        private Pane progress;

        @FXML
        private VBox client_stage;

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

    
        private void startMConnection(ActionEvent event) {
         Parent root;
            try {
                BoxBlur blur = new BoxBlur();
                blur.setIterations(3);
                client_stage.setEffect(blur);

                window = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/mConnectionView.fxml"));
                root = (Parent) loader.load();
                Controllers.mConnectionViewController = loader.getController();
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                window.initModality(Modality.APPLICATION_MODAL);
                window.initStyle(StageStyle.TRANSPARENT);
                window.setScene(scene);
                window.setTitle("WebChat");

                //Centrowanie mConnectionView według pozycji ClientView
                Node source = (Node) event.getSource();
                Window parentStage = source.getScene().getWindow();

                ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
                    double stageWidth = newValue.doubleValue();
                    window.setX(parentStage.getX() + parentStage.getWidth() / 2 - stageWidth / 2);
                };
                ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> {
                    double stageHeight = newValue.doubleValue();
                    window.setY(parentStage.getY() + parentStage.getHeight() / 2 - stageHeight / 2);
                };

                window.widthProperty().addListener(widthListener);
                window.heightProperty().addListener(heightListener);

                window.setOnShown(e -> {
                    window.widthProperty().removeListener(widthListener);
                    window.heightProperty().removeListener(heightListener);
                });
                Controllers.mConnectionViewController.tableConnection=false;

                window.show();

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        
        
        
        private void startConnection(MouseEvent event) {
            Parent root;
               try {
                   BoxBlur blur = new BoxBlur();
                   blur.setIterations(3);
                   client_stage.setEffect(blur);

                   window = new Stage();
                   FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/mConnectionView.fxml"));
                   root = (Parent) loader.load();
                   Controllers.mConnectionViewController = loader.getController();
                   Scene scene = new Scene(root);
                   scene.setFill(Color.TRANSPARENT);
                   window.initModality(Modality.APPLICATION_MODAL);
                   window.initStyle(StageStyle.TRANSPARENT);
                   window.setScene(scene);
                   window.setTitle("Server Settings");

                   //Centrowanie mConnectionView według pozycji ClientView
                   Node source = (Node) event.getSource();
                   Window parentStage = source.getScene().getWindow();

                   ChangeListener<Number> widthListener = (observable, oldValue, newValue) -> {
                       double stageWidth = newValue.doubleValue();
                       window.setX(parentStage.getX() + parentStage.getWidth() / 2 - stageWidth / 2);
                   };
                   ChangeListener<Number> heightListener = (observable, oldValue, newValue) -> {
                       double stageHeight = newValue.doubleValue();
                       window.setY(parentStage.getY() + parentStage.getHeight() / 2 - stageHeight / 2);
                   };

                   window.widthProperty().addListener(widthListener);
                   window.heightProperty().addListener(heightListener);

                   window.setOnShown(e -> {
                       window.widthProperty().removeListener(widthListener);
                       window.heightProperty().removeListener(heightListener);
                   });
                   Controllers.mConnectionViewController.tableConnection=true;
                   window.show();

               }
               catch (IOException e) {
                   e.printStackTrace();
               }
           }

        public void close() {
            window.close();
        }

        
        
        public void init2() {


            vbox = client_stage;

            progress.setTranslateY(200.0);
            progress.setTranslateX(400.0);

            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
            portColumn.setCellValueFactory(new PropertyValueFactory<>("port"));
            usersColumn.setCellValueFactory(new PropertyValueFactory<>("current"));
            protectedColumn.setCellValueFactory(new PropertyValueFactory<>("protect"));
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("online"));

            final FXTableGenerator gen = new FXTableGenerator(ServerTable);


            progress.visibleProperty().bind(gen.runningProperty());
            gen.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    ServerTable = gen.getValue();
                    FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.0), ServerTable);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(0.95);
                    fadeIn.play();
                    connectManuallyButton.setDisable(false);
                    switchonlineButton.setDisable(false);
                }
            });

            gen.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    System.out.println("FAILED TO LOAD DB DATA");
                }
            });
            gen.restart();

        }

        public void clicker(MouseEvent event) {
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                Node node = ((Node) event.getTarget()).getParent();
                TableRow row;
                if (node instanceof TableRow) {
                    row = (TableRow) node;
                } else {
                    // clicking on text part
                    row = (TableRow) node.getParent();
                }
                s = (ServerObject) row.getItem();
                if(s.getOnline().equals("OFFLINE")) {
            		Alert alert = new Alert(AlertType.WARNING);
            		
            		
            		
            		DialogPane dialogPane = alert.getDialogPane();
            		dialogPane.getStylesheets().add(getClass().getResource("/css/dialog.css").toExternalForm());
            		dialogPane.getStyleClass().add("view");
            		
            		
            		
            		alert.setTitle("WARNING");
            		alert.setHeaderText("THIS SERVER IS OFFLINE");

            		alert.showAndWait();
            		
            	}else
            	{
            		
                if(s.getPassword().length() == 0)
                	Controllers.mConnectionViewController.isPassword = true;
                else 
                	Controllers.mConnectionViewController.isPassword = false;
                Controllers.mConnectionViewController.tableConnection = true;
                Controllers.mConnectionViewController.IP = s.getIp();
                Controllers.mConnectionViewController.port = s.getPort();
            	
                startConnection(event);
            }
                //System.out.println(s.getIp());
            }
        }
        public void clicker2(ActionEvent event) {
        	

        		Controllers.mConnectionViewController.tableConnection = false;
            	Controllers.mConnectionViewController.isPassword = false;
            	startMConnection(event);
        	
        }
        
        @Override
        public void initialize(URL location, ResourceBundle resources) {
        	onOrOf = true;
        	connectManuallyButton.setDisable(true);
            switchonlineButton.setDisable(true);
        	init2();
        }


        public static VBox getClient_stage() {
            return vbox;
        }
        @FXML
        public void filter() {
        	if(switchonlineButton.getText().equals("Show online only")) {
        		switchonlineButton.setText("Show all servers");
        		onOrOf = false;
        		connectManuallyButton.setDisable(true);
                switchonlineButton.setDisable(true);
        		init2();
        	}
        	else if(switchonlineButton.getText().equals("Show all servers")) {
        		switchonlineButton.setText("Show online only");
        		onOrOf = true;
        		connectManuallyButton.setDisable(true);
                switchonlineButton.setDisable(true);
        		init2();
        	}
        }
    

}
