package backend;

import frontend.ServerGUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer implements Runnable {

	private int serverPort;
	private List<ClientThread> clients; 
	private ServerSocket serverSocket;
	private Thread thread;

	public ChatServer(int portNumber) {
		this.serverPort = portNumber;
		this.clients = new ArrayList<ClientThread>();
		this.thread = null;

		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("port listen error " + serverPort);
			ServerGUI.addLog("Could not listen on port: " + serverPort);
			System.exit(1);
		}
	}

	public List<ClientThread> getClients() {
		return clients;
	}

	public void startServer() {
		if (thread == null) {
			thread = new Thread(this);
			thread.setDaemon(true);
			thread.start();
		}
	}

	public void acceptClients(ServerSocket serverSocket) {

		ServerGUI.addLog("server starts port = " + serverSocket.getLocalSocketAddress());
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ServerGUI.addLog("accepts : " + socket.getRemoteSocketAddress());
				ClientThread client = new ClientThread(this, socket);
				Thread thread = new Thread(client);
				thread.start();
				clients.add(client);
			} catch (IOException ex) {
				ServerGUI.addLog("Accept failed on : " + serverPort);
			}
		}
	}

	@Override
	public void run() {
		acceptClients(serverSocket);
	}
}
