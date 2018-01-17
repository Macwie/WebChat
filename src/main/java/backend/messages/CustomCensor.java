package backend.messages;

import backend.messages.Message;
import backend.messages.Strategy;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomCensor implements Strategy {

    private Scanner x;
    String[] words = new String[200];
    List<String> list = new ArrayList<>();


    public void openFile() {
        try {
            x = new Scanner(new File("cenzura.txt"));
        } catch (FileNotFoundException e) {
            //do something with e, or handle this case
        }
        int count = -1;

        while (x.hasNext()) {
            list.add(x.nextLine());
            //words[++count] = x.nextLine();
        }
    }


    @Override
    public void censor(Message message) {
        addCensor(message);
    }

    private void addCensor(Message message) {
        openFile();
        String[] splittedMsg = message.getMessage().split(" ");

        for (int i = 0; i < splittedMsg.length; i++) {
            for (int j = 0; j < list.size(); j++) {
                if (splittedMsg[i].equals(list.get(j))) {
                    splittedMsg[i] = applyStars(splittedMsg[i]);
                }
            }
        }
        String result = String.join(" ", splittedMsg);
        message.setMessage(result);
    }

    private String applyStars(String msg) {

        for (int i = 0; i < msg.length(); i++) {
            msg = msg.replaceAll(msg, "&");
        }
        return msg;
    }
}
