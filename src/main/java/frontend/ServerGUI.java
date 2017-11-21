package frontend;

import backend.ServerObject;
import backend.ServersDAO;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;

public class ServerGUI extends WindowCore {

	private static String serverLogs = "";
	private static TextBox serverConsole = new TextBox("");

	public void show(String ip, int port, String serverName, String password) {
		Theme theme = new SimpleTheme(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
		if (ip.equals(ServerObject.getServerIP())) {
			ServersDAO serversDAO = ServersDAO.getInstance();
			serversDAO.addServer(serverName, ip, Integer.toString(port), password, true);
		} else {
			ServersDAO serversDAO = ServersDAO.getInstance();
			serversDAO.addServer(serverName, ip, Integer.toString(port), password, false);
		}

		serverLogs = " Server: " + ip + " online on port: " + port + "\n Name:" + serverName + " Password:" + password;

		serverConsole = new TextBox(serverLogs);
		serverConsole.setTheme(theme);
		serverConsole.setReadOnly(true);
		serverConsole.setPreferredSize(new TerminalSize(105, 40));
		contentPanel = new Panel();
		contentPanel.addComponent(serverConsole.withBorder(Borders.singleLine("Server console")));
		window.setComponent(contentPanel);
		textGUI.addWindowAndWait(window);

	}

	public static void addLog(String log) {
		serverConsole.addLine(" " + log);
	}
}
