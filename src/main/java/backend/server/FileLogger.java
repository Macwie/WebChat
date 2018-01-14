package backend.server;

import java.io.*;

public class FileLogger extends AbstractLogger {

    public FileLogger(int level){
        this.level = level;
    }

    @Override
    protected void write(String message) {
        FileWriter file;
        try {
            file = new FileWriter("LOGI.txt", true);
            BufferedWriter out = new BufferedWriter(file);
            out.write(message + "\n");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("PLIK " + message);
    }
}