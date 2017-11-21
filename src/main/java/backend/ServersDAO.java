package backend;

import java.sql.*;
import java.util.ArrayList;

public class ServersDAO implements DataBase {


	private Connection connection;
	private Statement statement;
	private String query;

	private static ServersDAO instance = null;

	private ServersDAO() {
	}

	public static ServersDAO getInstance() {
		if (instance == null)
			instance = new ServersDAO();

		return instance;
	}

	public ArrayList loadAll() {
		query = "SELECT * FROM servers";
		ArrayList<ServerObject> result = new ArrayList<>();
		try {
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			statement = connection.createStatement();
			ResultSet r = statement.executeQuery(query);
			if (r != null) {
				try {
					while (r.next()) {
						result.add(new ServerObject(r.getInt("id"), r.getString("name"), r.getString("ip"),
								r.getString("port"), r.getInt("current"), r.getString("password"),
								r.getBoolean("s_public")));
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
		return result;
	}

	public boolean addServer(String Sname, String Sip, String Sport, String Spassword, boolean s_public) {
		query = "INSERT INTO servers (name, ip, port, current, password, s_public) VALUES ('" + Sname + "','" + Sip
				+ "','" + Sport + "'," + 0 + ",'" + Spassword + "'," + s_public + ")";
		try {
			System.out.println("IP: " + Sip + " Port: " + Sport);
			String check = "SELECT ip FROM servers WHERE ip = '" + Sip + "' AND port = '" + Sport + "'";
			connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
			statement = connection.createStatement();
			ResultSet r = statement.executeQuery(check);
			if (!r.next()) {
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
}
