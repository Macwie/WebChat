package backend.fxcontrollers;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import backend.ServerObject;
import backend.client.Client;
import backend.database.ClientsDAO;
import backend.messages.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ChatController implements Initializable{

    public boolean ready = false;
    private HashMap<String, String> colors;

    private Client client;
    private ServerObject server;
    private String nick;

	private String style = "-fx-font-family: Calibri; -fx-font-weight: bold; -fx-font-size: 20px;";
	private String msg_style = "-fx-font-family: Calibri; -fx-font-size: 20px;";

	@FXML
	private Label server_name;

    @FXML
    private Button sendButton;

    @FXML
    private TextField messageTextField;

    @FXML
    private TextFlow activeUsersTextFlow;

    @FXML
    private TextFlow chatTextFlow;

    @FXML
    private ScrollPane chatScrollPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/*Runtime.getRuntime().addShutdownHook(new Thread() // usun usera z listy online w przypadku wyjscia przez X
				{
					@Override
					public void run() {
						//ClientsDAO clientsDAO = new ClientsDAO();
						//clientsDAO.removeClient(Controllers.CustomConnectionController.serverId, Controllers.CustomConnectionController.nick);
						//clientsDAO.updateCurrentUsers(Controllers.CustomConnectionController.serverId, false);
						client.stopConnection();
					}
				});*/

	    colors = new HashMap<>();

        //if(CustomConnectionController.IP != null)
            //server_name.setText("IP: "+ CustomConnectionController.IP);
		
	}

	public void init(ServerObject server, String nick) {
        this.server = server;
        this.nick = nick;

        //Client start
        ExecutorService async = Executors.newSingleThreadExecutor();

        async.execute(() -> {
            client = new Client(server.getIp(), server.getPort(), nick, chatTextFlow);
            client.startConnection();
        });

	}
	
	public String getInput() {
		return messageTextField.getText();
	}

	public void clearInput() {
		messageTextField.setText("");
	}

	public void send() {
		//ready = true;
        Message msg = new Message(messageTextField.getText());
        client.sendMsg(msg);
        messageTextField.setText("");

	}

	public void addMessage(String message) {
        Platform.runLater(() -> {
            String[] split = message.split(":");

            if(!(split[1].length() > 1))
                return;

            Text nick = new Text(split[0]);
            if(colors.get(split[0]) != null)
                nick.setStyle(style+" -fx-fill: "+colors.get(split[0])+";");
            else
                nick.setStyle(style+" -fx-fill: white;");
            Text msg = new Text(": "+split[1]+"\n");
            msg.setStyle(style+" -fx-fill: white;");
            chatTextFlow.getChildren().addAll(nick, msg);
            chatScrollPane.setVvalue(1.0);
        });
		//chatTextArea.setText(chatTextArea.getText() +"\n" + message);
	}

	public void addUsers(String user, String color) {
		//activeUsersTextArea.setText(activeUsersTextArea.getText() +"\n" + user);
        Platform.runLater(() -> {
            Text temp = new Text(user+"\n");
            temp.setStyle(style+" -fx-fill: "+color+";");
            colors.put(user, color);
            activeUsersTextFlow.getChildren().add(temp);
        });
	}

	public void clearUsers() {
		//activeUsersTextArea.setText("");
        Platform.runLater(() -> {
		    activeUsersTextFlow.getChildren().clear();
        });
	}
	
	public void exitChat() {
		//ClientsDAO clientsDAO = new ClientsDAO();
		//clientsDAO.removeClient(Controllers.CustomConnectionController.serverId, Controllers.CustomConnectionController.nick);
		//clientsDAO.updateCurrentUsers(Controllers.CustomConnectionController.serverId, false);
		//Client.stopClient();
        client.stopConnection();
		Controllers.GreetingController.startClient();
	}
}
