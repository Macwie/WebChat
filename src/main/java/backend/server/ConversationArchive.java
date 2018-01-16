package backend.server;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;

public class ConversationArchive {

    private static String msg_style = "-fx-font-family: Calibri; -fx-font-weight: bold; -fx-font-size: 20px; -fx-fill: white;";

    public static void write(TextFlow chat, String serverName) {

        StringBuilder sb = new StringBuilder();
        for (Node node : chat.getChildren()) {
            if (node instanceof Text) {
                sb.append(((Text) node).getText());
            }
        }

        String conversation = sb.toString();

        FileWriter fileWriter;
        try {
            File file = new File("ChatConversations\\"+serverName+".txt");
            file.getParentFile().mkdirs();
            fileWriter = new FileWriter(file, false);
            BufferedWriter out = new BufferedWriter(fileWriter);
            out.write(conversation);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read(TextFlow chat, String serverName) {

        FileReader fileReader;
        try {
            File file = new File("ChatConversations\\"+serverName+".txt");
            file.getParentFile().mkdirs();
            fileReader = new FileReader(file);
            BufferedReader in = new BufferedReader(fileReader);

            String sCurrentLine;
            Text text;
            Text date;
            String split[];

            while ((sCurrentLine = in.readLine()) != null) {
                split = sCurrentLine.split("\t\t\t\t\t");
                text = new Text(split[0]);
                date = new Text("\t\t\t\t\t"+split[1]+"\n");
                date.setStyle(" -fx-fill: white;");
                text.setStyle(msg_style);
                chat.getChildren().addAll(text, date);
            }

            in.close();
        } catch (IOException e) {
            System.out.println("Convestation archive for that server doesn't exist yet.");
        }
    }

}
