package backend.database;

import backend.fxcontrollers.Controllers;

import java.awt.*;
import java.sql.*;
import java.util.Random;

public class ClientsDAO implements Runnable, DataBase {


	private Connection connection;
	private Statement statement;
	private String query;
	private int server;
	private Thread thread;

	public void startUpdatingUsers(int server) {
		this.server = server;
		if (thread == null) {
			thread = new Thread(this);
			thread.setDaemon(true);
			thread.start();
		}
	}

	public boolean addClient(int server, String nick) {
		this.server = server;
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

	public boolean removeClient(int server, String nick) {
		this.server = server;
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

	public void getAllClients() {
		query = "SELECT name, color FROM users WHERE id IN (SELECT usrId FROM srvUsr WHERE srvId = " + server + ")";
		try {
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			statement = connection.createStatement();
			ResultSet r = statement.executeQuery(query);
			//ChatGUI.usersBox.setText("");
			Controllers.ChatController.clearUsers();
			if (r != null) {
				try {
					while (r.next()) {
						//ChatGUI.usersBox.addLine(" > " + r.getString("name"));
						Controllers.ChatController.addUsers(r.getString("name"), r.getString("color"));
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
	}

	public void updateCurrentUsers(int server, boolean add) {
		this.server = server;

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

	@Override
	public void run() {
		while (true) {
			try {
				getAllClients();
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
