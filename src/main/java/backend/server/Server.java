package backend.server;

import backend.messages.Message;
import javafx.scene.control.TextArea;

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
	private static TextArea serverLogs;
	AbstractLogger loggerChain = getChainOfLoggers();

	private static Server instance = null;

	private Server (int port) {
		System.out.println("Server - Server ");
		this.port = port;
		async = null;
		serverThreads = new ArrayList<>();
	}

	public static Server getInstance(int port) {
		System.out.println("Server - getInstance ");
		if(instance == null)
			instance = new Server(port);

		return instance;
	}

	private int findClient(int ID) {
		System.out.println("Server - findClient ");
		for (int i=0; i<serverThreads.size();i++) {
			if(serverThreads.get(i).getID() == ID)
				return i;
		}
		return -1;
	}

	public synchronized void handle(Message msg) {
		System.out.println("Server - handle ");
		for(int i=0; i<serverThreads.size(); i++) {
			serverThreads.get(i).send(msg);


			/*if(!msg.getNick().equals("JqK9ZG5TSabOAND81Clp")){
				serverLogs.appendText("przesylam wiadomosc" + " " +msg.getMessage() + msg.getNick() + " ");
			}*/

		}
	}

	public synchronized void remove(int ID) {
		System.out.println("Server - remove ");
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

	public void start(TextArea serverLogs) {
		System.out.println("Server - start ");
		try {
			System.out.println("Binding to port " + port + ", please wait  ...");
			serverSocket = new ServerSocket(port);
			isOnline = true;
			this.serverLogs = serverLogs;
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


			loggerChain.logMessage(AbstractLogger.INFO,
					"\nClient connected: "+socket);

            //serverLogs.appendText("\nClient connected: "+socket);
			ServerThread serverThread = new ServerThread(this, socket, serverLogs);
			serverThreads.add(serverThread);
			serverThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static AbstractLogger getChainOfLoggers(){
		AbstractLogger fileLogger = new FileLogger(AbstractLogger.LOG);
		AbstractLogger consoleLogger = new ConsoleLogger(AbstractLogger.INFO,serverLogs);
		consoleLogger.setNextLogger(fileLogger);
		return consoleLogger;
	}
}
