package frontend;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;
import java.util.Arrays;

public class WindowCore {

	protected static DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
	protected static Window window = null;
	protected static Screen screen = null;
	protected static WindowBasedTextGUI textGUI = null;
	protected static Panel contentPanel = null;
	protected static final int columns = 107;
	protected static final int rows = 30;

	public void init() {
		try {
			terminalFactory.setInitialTerminalSize(new TerminalSize(columns, rows));
			screen = terminalFactory.createScreen();
			screen.startScreen();
			textGUI = new MultiWindowTextGUI(screen);
			window = new BasicWindow("KCK Chat");
			window.setHints(Arrays.asList(Window.Hint.FULL_SCREEN));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
