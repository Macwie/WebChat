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
	private TextArea serverLogs;

	public ServerThread(Server server, Socket socket, TextArea serverLogs) {
		this.server = server;
		this.socket = socket;
		this.serverLogs = serverLogs;
		ID = socket.getPort();
	}

    public void start() {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection()  {
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
                serverLogs.appendText("\nClient disconnected: "+socket);

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
