package frontend;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.*;

public class SwitchGUI extends WindowCore {

	String asciText = "\r\n" + "   ______ _____    ___   ______   ____   _   __ __     ____ _   __ ______\r\n"
			+ "  / ____//__  /   /   | /_  __/  / __ \\ / | / // /    /  _// | / // ____/\r\n"
			+ " / /       / /   / /| |  / /    / / / //  |/ // /     / / /  |/ // __/   \r\n"
			+ "/ /___    / /__ / ___ | / /    / /_/ // /|  // /___ _/ / / /|  // /___   \r\n"
			+ "\\____/   /____//_/  |_|/_/     \\____//_/ |_//_____//___//_/ |_//_____/   \r\n"
			+ "                                                                         \r\n" + "";

	String asciText2 = "    ______________           ______________\r\n"
			+ "  |  ___________  |        |  ___________  |\r\n" + "  | |           | |        | |           | |\r\n"
			+ "  | |   0   0   | |        | |   0   0   | |\r\n" + "  | |     -     | |        | |     -     | |\r\n"
			+ "  | |   \\___/   | |        | |   \\___/   | |\r\n" + "  | |___     ___| |        | |___________| |\r\n"
			+ "  |_____|\\_/|_____|        |_______________|\r\n" + "    _|__|/ \\|_|_.............._|________|_\r\n"
			+ "   / ********** \\            / ********** \\\r\n"
			+ " /  ************  \\        /  ************  \\\r\n"
			+ "--------------------      --------------------\r\n";

	public void startClient() {
		ClientGUI clientGUI = new ClientGUI();
		clientGUI.show();
	}

	public void startServer() {
		ServerOptionGUI serverOption = new ServerOptionGUI();
		serverOption.show();
	}

	public void show() {
		Theme theme = new SimpleTheme(TextColor.ANSI.BLACK, new TextColor.RGB(170, 170, 170), SGR.BLINK);

		Button serverButton = new Button("START SERVER", () -> startServer());
		Button clientButton = new Button("START CLIENT", () -> startClient());
		contentPanel = new Panel();
		contentPanel.setLayoutManager(new GridLayout(3));
		Panel asci1 = new Panel(new LinearLayout(Direction.HORIZONTAL));
		asci1.addComponent(new Label(asciText)).setTheme(theme);
		Panel asci2 = new Panel(new LinearLayout(Direction.HORIZONTAL));
		asci2.addComponent(new EmptySpace(new TerminalSize(11, 0)));
		asci2.addComponent(new Label(asciText2));
		Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
		buttonPanel.addComponent(new EmptySpace(new TerminalSize(14, 0)));
		buttonPanel.addComponent(serverButton);
		buttonPanel.addComponent(new EmptySpace(new TerminalSize(10, 0)));
		buttonPanel.addComponent(clientButton);

		contentPanel.addComponent(new EmptySpace(new TerminalSize(13, 7)));
		contentPanel.addComponent(asci1);
		contentPanel.addComponent(new EmptySpace(new TerminalSize(10, 7)));

		contentPanel.addComponent(new EmptySpace(new TerminalSize(13, 7)));
		contentPanel.addComponent(asci2);
		contentPanel.addComponent(new EmptySpace(new TerminalSize(10, 7)));

		contentPanel.addComponent(new EmptySpace(new TerminalSize(13, 7)));
		contentPanel.addComponent(buttonPanel);
		contentPanel.addComponent(new EmptySpace(new TerminalSize(13, 7)));

		window.setComponent(contentPanel);
		textGUI.addWindowAndWait(window);
	}

}
