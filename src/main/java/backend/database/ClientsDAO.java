package backend.database;

import backend.fxcontrollers.Controllers;
import backend.server.Server;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ClientsDAO implements Runnable, DataBase {


	private Connection connection;
	private Statement statement;
	private String query;
	private Thread thread;
    private static int server;

    private HashMap<String, String> usersMap;

	private static ClientsDAO instance = null;

	private ClientsDAO(int serverId) {
	    server = serverId;
    }

	public static ClientsDAO getInstance(int serverId) {
		if(instance == null || server != serverId)
			instance = new ClientsDAO(serverId);

		return instance;
	}

    public static ClientsDAO getInstance() {
        return instance;
    }

	public void startUpdatingUsers(int server) {
		//this.server = server;
		if (thread == null) {
			thread = new Thread(this);
			thread.setDaemon(true);
			thread.start();
		}
	}

	public boolean addClient(String nick) {
		//this.server = server;
		String color = generateColor();
		query = "INSERT INTO users (name, color) VALUES ('" + nick + "', "+"'" + color + "')"; // dodaj usera
		try {
			String queryUserId = "SELECT id FROM users WHERE name = '" + nick + "'"; // pobierz jego id
			int usrId;
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			statement = connection.createStatement();
			String check = "SELECT id FROM users WHERE name = '" + nick + "'"; // sprawdz czy taki user juz istnieje
			ResultSet r = statement.executeQuery(check);
			if (!r.next()) {
				statement.executeUpdate(query);
			}
			ResultSet r2 = statement.executeQuery(queryUserId);
			if (r2.next()) {
				usrId = r2.getInt("id");
				String query2 = "INSERT INTO srvUsr (srvId, usrId) VALUES (" + server + "," + usrId + ")"; // dodaj go
																											// do
																											// lacznika
																											// (jest
																											// online)
				statement.executeUpdate(query2);
				statement.close();
				connection.close();
			}

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeClient(String nick) {
		//this.server = server;
		try {
			String queryUserId = "SELECT id FROM users WHERE name = '" + nick + "'"; // pobierz id usera
			int usrId;
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			statement = connection.createStatement();
			ResultSet r = statement.executeQuery(queryUserId);
			if (r.next()) {
				usrId = r.getInt("id");
				query = "DELETE FROM srvUsr WHERE srvId =" + server + " AND usrId =" + usrId + " LIMIT 1";
				statement.executeUpdate(query);
			}
			statement.close();
			connection.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public HashMap<String, String> getAllClients() {
		query = "SELECT name, color FROM users WHERE id IN (SELECT usrId FROM srvUsr WHERE srvId = " + server + ")";
        HashMap<String, String> data = new HashMap<>();
		try {
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			statement = connection.createStatement();
			ResultSet r = statement.executeQuery(query);
			if (r != null) {
				try {
					while (r.next()) {
                        data.put(r.getString("name"), r.getString("color"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		usersMap = data;
		return usersMap;
	}

	public void updateCurrentUsers(int server, boolean add) {
		//this.server = server;

		try {
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			statement = connection.createStatement();
			if (add) { // dodanie do currentUsers
				query = "UPDATE servers SET current = current + 1 WHERE id =" + server;
			} else {
				query = "UPDATE servers SET current = current - 1 WHERE id =" + server;
			}
			try {
				statement.executeUpdate(query);
			} catch (SQLException s) {
				s.printStackTrace();
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static String generateColor() {
		Random rand = new Random();
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();

		Color c = new Color(r, g, b);
		String hex = String.format("#%06x", c.getRGB() & 0x00FFFFFF);
		return hex;
	}

    public HashMap<String, String> getUsersMap() {
        return usersMap;
    }

	@Override
	public void run() {
		while (true) {
			try {
				//getAllClients();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
