package backend.client;

import backend.messages.*;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private Socket socket;
    private ObjectOutputStream streamOut; //stream wysylany na server
    private ClientThread clientThread;
    private String serverIP;
    private int serverPort;
    private String nick;
    private TextFlow chatBox;
    private TextFlow activeUsers;
    private Strategy strategy;

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Client(String serverIP, String serverPort, String nick, TextFlow chatBox, TextFlow activeUsers, Strategy strategy) {
        this.serverIP = serverIP;
        this.serverPort = Integer.parseInt(serverPort);
        this.nick = nick;
        this.chatBox = chatBox;
        this.activeUsers = activeUsers;
        clientThread = null;
        this.strategy = strategy;
    }

    public void openStreams() throws IOException {
        streamOut = new ObjectOutputStream(socket.getOutputStream());
    }

    public void startConnection() {

        try {
            socket = new Socket(serverIP, serverPort);
            openStreams();

            clientThread = new ClientThread(this, socket);
            clientThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopConnection() {
        try {
            if (streamOut != null)
                streamOut.close();
            if (socket != null)
                socket.close();

            clientThread.closeConnection();

            System.exit(0);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void handle(Message msg) {
        iMessage temp;
        if (!msg.getNick().equals("JqK9ZG5TSabOAND81Clp")) { //notify client for updating active users
            temp = new MessageDateDecorator(new MessageCensorDecorator(new Message(msg), strategy));
        } else {
            temp = new Message(msg);
        }
        temp.show(chatBox, activeUsers);
    }

    public void sendMsg(Message message) {
        if (message.getMessage().equals("END"))
            stopConnection();
        else {
            message.setNick(nick);
            try {
                streamOut.writeObject(message);
                streamOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
                stopConnection();
            }
        }

    }

}

