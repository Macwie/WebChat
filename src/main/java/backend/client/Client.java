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


    public Client(String serverIP, String serverPort, String nick, TextFlow chatBox, TextFlow activeUsers) {
        this.serverIP = serverIP;
        this.serverPort = Integer.parseInt(serverPort);
        this.nick = nick;
        this.chatBox = chatBox;
        this.activeUsers = activeUsers;
        clientThread = null;
    }

    public void openStreams() throws IOException {
        streamOut = new ObjectOutputStream(socket.getOutputStream());
    }

    public void startConnection() {
        System.out.println("Establishing connection. Please wait ...");

        try {
            socket = new Socket(serverIP, serverPort);
            System.out.println("Connected: " + socket);
            openStreams();

            clientThread = new ClientThread(this, socket);
            clientThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopConnection() {
        try
        {
            if (streamOut != null)
                streamOut.close();
            if (socket    != null)
                socket.close();

            clientThread.closeConnection();

            System.exit(0);
        }
        catch(IOException ioe)
        {
            System.out.println("Error closing ...");
        }
    }

    public void handle(Message msg) {
        iMessage temp;
        if(!msg.getNick().equals("JqK9ZG5TSabOAND81Clp")) { //notify client for updating active users
            temp = new MessageDateDecorator(new MessageCensorDecorator(new Message(msg), new PredefinedCensor()));
        }else{
            temp = new Message(msg);
        }
            temp.show(chatBox, activeUsers);
    }

    public void sendMsg(Message message) {
        if(message.getMessage().equals("END"))
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

