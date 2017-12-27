package backend.client;


import backend.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Maciek on 12.12.2017.
 */
public class ClientThread {

	private Socket socket;
	private Client client;
	private ObjectInputStream streamIn;
	private ExecutorService async;
	private boolean isActive = false;

	public ClientThread(Client client, Socket socket) {
		this.socket = socket;
		this.client = client;
		async = null;
	}

	public void start() {

		openStream();
		isActive = true;

		async = Executors.newSingleThreadExecutor();

		async.execute(() -> {
			while(isActive) {
				try {
					client.handle((Message) streamIn.readObject());
				} catch (IOException e) {
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void openStream() {
		try {
			streamIn = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			isActive = false;
			if(streamIn != null)
				streamIn.close();

			async.shutdown();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
