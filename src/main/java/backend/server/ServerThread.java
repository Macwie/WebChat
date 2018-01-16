package backend.server;

import backend.messages.Message;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThread {

	private Socket socket;
	private Server server;

	private int ID = -1;    //id of connected client
	private ObjectInputStream streamIn;
	private ObjectOutputStream streamOut;
	private ExecutorService async;

	public ServerThread(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		ID = socket.getPort();
		System.out.println("ServerThread - ServerThread ");
	}

    public void start() {
		System.out.println("ServerThread - Start ");
		async = Executors.newSingleThreadExecutor();

		async.execute(() -> {
			openStream();
			sendUpdateCommandToAll();
			sendMessagesToAll();
		});
	}

	private void openStream() {
		try {
			streamIn = new ObjectInputStream(socket.getInputStream());
			streamOut = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("ServerThread - OpenStream ");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection()  {
		System.out.println("ServerThread - closeConnection");
		try {
			if (socket != null)
				socket.close();
			if (streamIn != null)
				streamIn.close();
			if (streamOut != null)
				streamOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(Message msg) {

		if(!msg.getNick().equals("JqK9ZG5TSabOAND81Clp")) {
			AbstractLogger.loggerChain.logMessage(AbstractLogger.LOG,
					"WIADOMOSC PRZESLANA: nick: " + msg.getNick() + "tresc: " + msg.getMessage() + "IP: " + socket.getInetAddress());
		}

		//serverLogs.appendText(msg.getNick() + " " +msg.getMessage() + " " +socket.getInetAddress());
		try {
			streamOut.writeObject(msg);
			streamOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
			server.remove(ID);
			async.shutdown();

		}
	}

	private void sendMessagesToAll() {

		while(true) {
			try {
				Message msg = (Message) streamIn.readObject();
				server.handle(msg);
			} catch (IOException e) {
				server.remove(ID);
				sendUpdateCommandToAll();
				async.shutdown();


				AbstractLogger.loggerChain.logMessage(AbstractLogger.INFO,
						"Client disconnected: "+socket);


               // serverLogs.appendText("\nClient disconnected: "+socket);

				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendUpdateCommandToAll() {
		server.handle(new Message("JqK9ZG5TSabOAND81Clp", "update"));
	}

	public int getID() {
		return ID;
	}

}
