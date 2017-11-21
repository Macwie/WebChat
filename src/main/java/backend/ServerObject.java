package backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class ServerObject {
	protected int id;
	protected String name;
	protected String ip;
	protected String port;
	protected int current;
	protected String password;
	protected boolean s_public;

	public ServerObject(int id, String name, String ip, String port, int current, String password, boolean s_public) {
		this.id = id;
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.current = current;
		this.password = password;
		this.s_public = s_public;
	}

	public boolean isOnline() {
		boolean b = true;
		try {
			InetSocketAddress sa = new InetSocketAddress(ip, Integer.parseInt(port));
			Socket ss = new Socket();
			ss.connect(sa, 100);
			ss.close();
		} catch (Exception e) {
			b = false;
		}
		return b;
	}

	public static String getServerIP() {
		URL whatismyip = null;
		String ip = "localhost";
		try {
			whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			ip = in.readLine();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isS_public() {
		return s_public;
	}

	public void setS_public(boolean s_public) {
		this.s_public = s_public;
	}
}