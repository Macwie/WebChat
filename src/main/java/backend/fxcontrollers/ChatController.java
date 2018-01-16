package backend.fxcontrollers;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import backend.database.ServerObject;
import backend.client.Client;
import backend.database.ClientsDAO;
import backend.messages.CustomCensor;
import backend.messages.Message;
import backend.messages.PredefinedCensor;
import backend.server.ConversationArchive;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;

public class ChatController implements Initializable {

    private static Client client;
    private static String nick;
    private static ClientsDAO clientsDAO;
    private static TextFlow chat;
    private static ServerObject server;

    @FXML
    private Label server_name;

    @FXML
    private TextField messageTextField;

    @FXML
    private TextFlow activeUsersTextFlow;

    @FXML
    private TextFlow chatTextFlow;

    @FXML
    private ChoiceBox<String> censorOptions;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        chat = chatTextFlow;

    }

    public void init(ServerObject server, String nick) {
        ChatController.server = server;
        ChatController.nick = nick;
        initializeCensorshipMethodChoiceBox();
        //Set ID of the server for clients DAO
        clientsDAO = ClientsDAO.getInstance(server.getId());
        clientsDAO.addClient(nick);
        //Set active users ++
        clientsDAO.updateCurrentUsers(true);
        //Load conversation archive
        ConversationArchive.read(chat, server.getName());
        server_name.setText("Server: " + server.getName() + " IP: " + server.getIp());
        //Client start
        ExecutorService async = Executors.newSingleThreadExecutor();
        async.execute(() -> {
            client = new Client(server.getIp(), server.getPort(), nick, chatTextFlow, activeUsersTextFlow, new PredefinedCensor());
            client.startConnection();
        });
    }

    public void send() {
        Message msg = new Message(messageTextField.getText());
        client.sendMsg(msg);
        messageTextField.setText("");

    }

    public void exitChat() {    //method for button
        exit();
    }

    public static void exit() {  //static method for closing chat view and removing active client
        clientsDAO.removeClient(nick);
        clientsDAO.updateCurrentUsers(false);
        ConversationArchive.write(chat, server.getName());
        client.stopConnection();
    }


    private void initializeCensorshipMethodChoiceBox() {

        ObservableList<String> list = FXCollections.observableArrayList("Censor1", "Censor2");
        censorOptions.setItems(list);
        censorOptions.setValue(list.get(0));

        censorOptions.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            if ("Censor1" == censorOptions.getValue()) {
                client.setStrategy(new CustomCensor());

            } else {
                client.setStrategy(new PredefinedCensor());
            }

        });

    }
}
