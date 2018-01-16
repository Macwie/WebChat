package backend.server;

import backend.messages.Message;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {


	private ServerSocket serverSocket;
	private ExecutorService async;
	private List<ServerThread> serverThreads;
	private boolean isOnline;
	private int port;
	private String infoStart;
	private static Server instance = null;

	private Server (int port) {
		this.port = port;
		async = null;
		serverThreads = new ArrayList<>();
	}

	public static Server getInstance(int port) {
		if(instance == null)
			instance = new Server(port);

		return instance;
	}

	private int findClient(int ID) {
		for (int i=0; i<serverThreads.size();i++) {
			if(serverThreads.get(i).getID() == ID)
				return i;
		}
		return -1;
	}

	public synchronized void handle(Message msg) {
		for(int i=0; i<serverThreads.size(); i++) {
			serverThreads.get(i).send(msg);
		}
	}

	public synchronized void remove(int ID) {
		int index = findClient(ID);
		if(index >= 0) {
			ServerThread toTerminate = serverThreads.get(index);
			for (int i=0; i<serverThreads.size();i++) {
				if(serverThreads.get(i).getID() == ID)
					serverThreads.remove(i);
			}
			toTerminate.closeConnection();
		}
	}

	public void start(String infoStart) {
		try {
			serverSocket = new ServerSocket(port);
			isOnline = true;
			AbstractLogger.loggerChain.logMessage(AbstractLogger.INFO, infoStart);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(async == null) {
			async = Executors.newSingleThreadExecutor();
			async.execute(() -> {
				while(isOnline) {
					acceptClients();
				}
				async.shutdown();
			});
		}
	}

	private void acceptClients() {
		try {
			Socket socket = serverSocket.accept();
			AbstractLogger.loggerChain.logMessage(AbstractLogger.INFO,
					"Client connected: "+socket);
			ServerThread serverThread = new ServerThread(this, socket);
			serverThreads.add(serverThread);
			serverThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
