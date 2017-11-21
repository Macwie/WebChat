package frontend;

import backend.Client;
import backend.ClientsDAO;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.*;

public class ChatGUI extends WindowCore {

	private static TextBox chatBox = new TextBox("");
	public static TextBox textInput = new TextBox("");
	public static TextBox usersBox = new TextBox("");
	public static boolean ready = false;

	public void show(int serverId, String nick) {
		Theme theme = new SimpleTheme(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);
		contentPanel = new Panel();
		Panel chatWindowPanel = new Panel().setPreferredSize(new TerminalSize(75, 27));
		Panel onlineWindowPanel = new Panel().setPreferredSize(new TerminalSize(32, 27));
		Panel infoPanel = new Panel().setPreferredSize(new TerminalSize(32, 3));
		Panel messagePanel = new Panel().setPreferredSize(new TerminalSize(75, 3));
		messagePanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		infoPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));

		Button send = new Button("Wyslij", new Runnable() {
			@Override
			public void run() {
				ready = true;
			}
		});

		chatBox.setPreferredSize(new TerminalSize(73, 25)).setTheme(theme);
		chatBox.setReadOnly(true);
		chatWindowPanel.addComponent(chatBox);

		textInput.setPreferredSize(new TerminalSize(60, 1)).setTheme(theme);
		messagePanel.addComponent(textInput);
		messagePanel.addComponent(send);

		usersBox.setPreferredSize(new TerminalSize(30, 25)).setTheme(theme);
		usersBox.setReadOnly(true);
		onlineWindowPanel.addComponent(usersBox);

		contentPanel.setLayoutManager(new GridLayout(2));
		contentPanel.addComponent(chatWindowPanel.withBorder(Borders.singleLine("Czat")));
		contentPanel.addComponent(onlineWindowPanel.withBorder(Borders.singleLine("Aktywni użytkownicy")));
		contentPanel.addComponent(messagePanel);
		contentPanel.addComponent(infoPanel);

		Button Button = new Button("Wyjdz", new Runnable() {
			@Override
			public void run() {
				ClientsDAO clientsDAO = new ClientsDAO();
				clientsDAO.removeClient(serverId, nick);
				clientsDAO.updateCurrentUsers(serverId, false);
				Client.stopClient();
				ClientGUI clientGUI = new ClientGUI();
				clientGUI.show();
			}
		});

		Runtime.getRuntime().addShutdownHook(new Thread() // usun usera z listy online w przypadku wyjscia przez X
		{
			@Override
			public void run() {
				ClientsDAO clientsDAO = new ClientsDAO();
				clientsDAO.removeClient(serverId, nick);
				clientsDAO.updateCurrentUsers(serverId, false);
			}
		});

		infoPanel.addComponent(new EmptySpace(new TerminalSize(10, 1)));
		infoPanel.addComponent(Button);
		window.setComponent(contentPanel);
		textGUI.addWindowAndWait(window);
	}

	public static void addMessage(String message) {
		chatBox.addLine(" " + message);
		System.out.println(message);
	}

}
