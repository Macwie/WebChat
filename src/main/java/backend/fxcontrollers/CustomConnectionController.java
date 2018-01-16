package backend.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import backend.database.FXTableGenerator;
import backend.database.ServerObject;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.Main;

public class CustomConnectionController implements Initializable {


    private ArrayList<ServerObject> list;
    public ServerObject server;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField ipTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private TextField nickTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list = FXTableGenerator.getList();

        //Animation
        ScaleTransition anim = new ScaleTransition(Duration.seconds(0.4), pane);
        anim.setFromX(0);
        anim.setFromY(0);
        anim.setByX(1.0);
        anim.setByY(1.0);
        anim.play();

    }

    @FXML
    private void startChat(ActionEvent event) {

        //Get data from inputs
        String ip = ipTextField.getText();
        String port = portTextField.getText();
        String password = passwordPasswordField.getText();
        String nick = nickTextField.getText();
        boolean goodPassword = true;

        if (server == null)  //ManualConnection
        {
            //Find server id using ip and port delivered by user
            for (ServerObject s : list) {
                if (s.getPort().equals(port) && s.getIp().equals(ip)) {
                    server = s;
                    //Server has password
                    if (server.getPassword() != null) {
                        //Passwords match
                        if (server.getPassword().equals(password)) {
                            goodPassword = true;
                        } else {
                            goodPassword = false;
                        }
                    }
                    break;
                }
            }
        } else {
            //Wrong password
            if (!server.getPassword().equals(password))
                goodPassword = false;
        }

        //Wrong password
        if (!goodPassword) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initStyle(StageStyle.TRANSPARENT);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/css/Dialog.css").toExternalForm());
            dialogPane.getStyleClass().add("view");
            dialogPane.getScene().setFill(Color.TRANSPARENT);
            alert.setTitle("ERROR");
            alert.setHeaderText("Wrong password !");
            alert.showAndWait();
        } else    //Password match -> open chat view
        {
            Parent root;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ChatView.fxml"));
                root = loader.load();
                Controllers.ChatController = loader.getController();
                Controllers.ChatController.init(server, nick);
                Scene scene = new Scene(root);
                Stage window = Main.WINDOW;
                window.setScene(scene);
                window.setTitle("WebChat");
                window.setUserData("Chat");
                Controllers.clientController.close();
                window.show();
                window.setOnCloseRequest(e -> {
                    Controllers.ChatController.exitChat();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void cancelCustomConnection(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        //Animations
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.4), pane);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToY(0.0);
        scaleTransition.setToX(0.0);
        scaleTransition.setByX(1.0);
        scaleTransition.setByY(1.0);
        scaleTransition.play();
        scaleTransition.setOnFinished(event1 -> {
            ClientController.getClient_stage().setEffect(null);
            stage.close();
        });
    }

    public void controlInputs() {
        if (server != null) {   //Connection from table
            ipTextField.setText(server.getIp());
            portTextField.setText(server.getPort());
            portTextField.setDisable(true);
            ipTextField.setDisable(true);
            if (server.getPassword().length() == 0)     //block password input if server doesn't need password
                passwordPasswordField.setDisable(true);
            else
                passwordPasswordField.setDisable(false);
        } else {
            //Connection manual from button
            ipTextField.setText("");
            portTextField.setText("");
            portTextField.setDisable(false);
            ipTextField.setDisable(false);
        }
    }

    public void setServerData(ServerObject serverObject) {
        server = serverObject;
        controlInputs();
    }

}
