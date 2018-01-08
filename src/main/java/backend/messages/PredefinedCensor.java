package backend.messages;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PredefinedCensor implements Strategy {


    private Scanner x;
    String[] words = new String[6];
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
            // words[++count] = x.nextLine();

        }
    }

    @Override
    public void censor(Message message) {
        addCensor(message);
    }

    private void addCensor(Message message) {
        openFile();


        String[] splittedMsg = message.getMessage().split(" ");

        for (int i = 0; i < list.size(); i++) {
            message.getMessage().replaceAll(list.get(i),"*");
            int sizeWord=list.get(i).length();


                message.setMessage(message.getMessage().replaceAll(list.get(i), buildStarsString(sizeWord)));

        }
    }

    private String buildStarsString(int sizeWord) {

        StringBuilder censoredMessage=new StringBuilder();
        for(int j=0; j<sizeWord;j++){
            censoredMessage.append("*");
        }
        return censoredMessage.toString();
    }

}
