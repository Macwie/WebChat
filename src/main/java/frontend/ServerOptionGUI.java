package frontend;

import backend.ChatServer;
import backend.ServerObject;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;

import java.util.regex.Pattern;

public class ServerOptionGUI extends WindowCore {

	private int port;
	private String serverName;
	private String IP;
	private String password;

	public void show() {

		contentPanel = new Panel();
		Panel mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
		contentPanel.setLayoutManager(new GridLayout(3));
		TextBox passBox = new TextBox().setMask('*').setPreferredSize(new TerminalSize(14, 1));
		TextBox textIp = new TextBox(ServerObject.getServerIP()).setPreferredSize(new TerminalSize(14, 1));
		TextBox textPort = new TextBox().setValidationPattern(Pattern.compile("[0-9]{0,5}"))
				.setPreferredSize(new TerminalSize(14, 1));
		TextBox textServerName = new TextBox().setValidationPattern(Pattern.compile("[a-zA-Z_0-9]{0,40}"))
				.setPreferredSize(new TerminalSize(14, 1));
		Label dane = new Label(" Dane serwera ").setPreferredSize(new TerminalSize(14, 2));
		Label ipLabel = new Label("IP:");
		Label portLabel = new Label("Port:");
		Label nameLabel = new Label("Server name:");
		Label passLabel = new Label("Password:");

		contentPanel.addComponent(new EmptySpace(new TerminalSize(30, 10)));
		contentPanel.addComponent(new EmptySpace(new TerminalSize(30, 10)));
		contentPanel.addComponent(new EmptySpace(new TerminalSize(30, 10)));

		contentPanel.addComponent(new EmptySpace(new TerminalSize(42, 30)));
		contentPanel.addComponent(mainPanel);
		contentPanel.addComponent(new EmptySpace(new TerminalSize(10, 30)));
		mainPanel.addComponent(dane);

		mainPanel.addComponent(ipLabel);
		mainPanel.addComponent(textIp);
		mainPanel.addComponent(nameLabel);
		mainPanel.addComponent(textServerName);
		mainPanel.addComponent(ipLabel);
		mainPanel.addComponent(portLabel);
		mainPanel.addComponent(textPort);
		mainPanel.addComponent(passLabel);
		mainPanel.addComponent(passBox);
		mainPanel.addComponent(new EmptySpace(new TerminalSize(14, 1)));
		Button Button = new Button("   Uruchom   ", new Runnable() {
			@Override
			public void run() {
				password = passBox.getText();
				IP = textIp.getText();
				port = Integer.parseInt(textPort.getText());
				serverName = textServerName.getText();
				ChatServer server = new ChatServer(port);
				ServerGUI serverGUI = new ServerGUI();
				server.startServer();
				serverGUI.show(IP, port, serverName, password);
			}
		});
		mainPanel.addComponent(Button);
		window.setComponent(contentPanel);
		textGUI.addWindowAndWait(window);
	}

}