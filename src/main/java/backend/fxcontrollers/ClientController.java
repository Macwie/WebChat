package backend.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import backend.FXTableGenerator;
import backend.ServerObject;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

public class ClientController implements Initializable {

    private Stage window;
    private static VBox vbox;
    public static boolean showOnlyPublic;  //true to all false to online

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

    @FXML
    private void startManuallyConnection(ActionEvent event) {
        Parent root;
        Controllers.CustomConnectionController.tableConnection = false;
        Controllers.CustomConnectionController.isPassword = false;
        try {
            BoxBlur blur = new BoxBlur();
            blur.setIterations(3);
            client_stage.setEffect(blur);

            window = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/CustomConnectionView.fxml"));
            root = loader.load();
            Controllers.CustomConnectionController = loader.getController();
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
            Controllers.CustomConnectionController.tableConnection = false;

            window.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void startConnection(MouseEvent event) {
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
            if (s.getOnline().equals("OFFLINE")) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.initStyle(StageStyle.TRANSPARENT);
                BoxBlur blur = new BoxBlur();
                blur.setIterations(3);
                client_stage.setEffect(blur);
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/css/Dialog.css").toExternalForm());
                dialogPane.getStyleClass().add("view");
                dialogPane.getScene().setFill(Color.TRANSPARENT);
                alert.setTitle("WARNING");
                alert.setHeaderText("THIS SERVER IS OFFLINE");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    client_stage.setEffect(null);
                }
                //oke button is pressed
            } else {
                if (s.getPassword().length() == 0)
                    Controllers.CustomConnectionController.isPassword = true;
                else
                    Controllers.CustomConnectionController.isPassword = false;
                Controllers.CustomConnectionController.tableConnection = true;
                Controllers.CustomConnectionController.IP = s.getIp();
                Controllers.CustomConnectionController.port = s.getPort();
            }
        }

        Parent root;
        try {
            BoxBlur blur = new BoxBlur();
            blur.setIterations(3);
            client_stage.setEffect(blur);
            window = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/CustomConnectionView.fxml"));
            root = loader.load();
            Controllers.CustomConnectionController = loader.getController();
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
            Controllers.CustomConnectionController.tableConnection = true;
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void close() {
        window.close();
    }


    public void refresh() {
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
        gen.setOnSucceeded(workerStateEvent -> {
            ServerTable = gen.getValue();
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.0), ServerTable);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(0.95);
            fadeIn.play();
            connectManuallyButton.setDisable(false);
            switchonlineButton.setDisable(false);
        });
        gen.setOnFailed(workerStateEvent -> System.out.println("FAILED TO LOAD DB DATA"));
        gen.restart();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showOnlyPublic = true;
        connectManuallyButton.setDisable(true);
        switchonlineButton.setDisable(true);
        refresh();
    }


    public static VBox getClient_stage() {
        return vbox;
    }

    @FXML
    public void filter() {
        if (switchonlineButton.getText().equals("Show online only")) {
            switchonlineButton.setText("Show all servers");
            showOnlyPublic = false;
        } else if (switchonlineButton.getText().equals("Show all servers")) {
            switchonlineButton.setText("Show online only");
            showOnlyPublic = true;
        }
        connectManuallyButton.setDisable(true);
        switchonlineButton.setDisable(true);
        refresh();
    }
}
