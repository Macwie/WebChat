package backend.server;

import backend.fxcontrollers.Controllers;
import javafx.scene.control.TextArea;

public class ConsoleLogger extends AbstractLogger {

    private TextArea serverLogs;
    public ConsoleLogger(int level){
        this.level = level;
        this.serverLogs = Controllers.ServerController.getServerLogArea();
    }

    @Override
    protected void write(String message) {
        serverLogs.appendText(message);
    }
}