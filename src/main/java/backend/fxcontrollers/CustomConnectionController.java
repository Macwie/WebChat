package backend.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import backend.*;
import backend.database.ClientsDAO;
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


    static String IP;
    static String port;
    static String nick;
    static String password;
    private ArrayList<ServerObject> list;
    static int serverId;
    static String passwordMatch;
    public static boolean tableConnection;
    public static boolean isPassword;

    @FXML
    private AnchorPane pane;

    @FXML
    private Button connectButton;

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

    @FXML
    private void startChat(ActionEvent event) {
        ServerObject server = null;
        for (ServerObject s : list) {
            if (s.getPort().equals(port) && s.getIp().equals(IP)) {
                server = s;
                if (s.getPassword() == null)
                    passwordMatch = "";
                else
                    passwordMatch = s.getPassword();
                serverId = s.getId();
                break;
            }
        }


        password = passwordPasswordField.getText();
        nick = nickTextField.getText();
        if (isTableConnection() == false) {
            IP = ipTextField.getText();
            port = portTextField.getText();
        }

        if (!password.equals(passwordMatch)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initStyle(StageStyle.TRANSPARENT);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/css/Dialog.css").toExternalForm());
            dialogPane.getStyleClass().add("view");
            dialogPane.getScene().setFill(Color.TRANSPARENT);
            alert.setTitle("ERROR");
            alert.setHeaderText("BAD PASSWORD");
            alert.showAndWait();
            return;
        }

        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/ChatView.fxml"));
            root = loader.load();
            Controllers.ChatController = loader.getController();
            Controllers.ChatController.init(IP, port, nick);

            Scene scene = new Scene(root);
            Stage window = Main.WINDOW;
            window.setScene(scene);
            window.setTitle("WebChat");
            ClientsDAO clientsDAO = new ClientsDAO();   //dodawanie do online list

            // System.out.println(server.getId());
            clientsDAO.addClient(server.getId(), nick);
            clientsDAO.updateCurrentUsers(server.getId(), true);
            clientsDAO.startUpdatingUsers(server.getId());
            Controllers.clientController.close();
            window.show();
            window.setOnCloseRequest(e -> {
                Controllers.ChatController.exitChat();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelCustomConnection(ActionEvent event) {
        // get a handle to the stage
        Stage stage = (Stage) cancelButton.getScene().getWindow();
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list = FXTableGenerator.getList();
        if (isTableConnection() == true) {
            ipTextField.setText(IP);
            portTextField.setText(port);
            portTextField.setDisable(true);
            ipTextField.setDisable(true);
        } else {
            ipTextField.setText("");
            portTextField.setText("");
            portTextField.setDisable(false);
            ipTextField.setDisable(false);
        }
        if (isPassword)
            passwordPasswordField.setDisable(true);
        else
            passwordPasswordField.setDisable(false);

        ScaleTransition test = new ScaleTransition(Duration.seconds(0.4), pane);
        test.setFromX(0);
        test.setFromY(0);
        test.setByX(1.0);
        test.setByY(1.0);
        test.play();

    }
    public static boolean isTableConnection() {
        return tableConnection;
    }

    public static void setTableConnection(boolean tableConnection) {
        CustomConnectionController.tableConnection = tableConnection;
    }


}