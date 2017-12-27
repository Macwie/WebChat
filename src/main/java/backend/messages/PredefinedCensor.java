package backend.messages;

public class PredefinedCensor implements Strategy {

    private String[] words = new String[]{
            "test", "test2"
    };

    @Override
    public void censor(Message message) {
        addCensor(message);
    }

    private void addCensor(Message message) {
        String[] splittedMsg = message.getMessage().split(" ");

        for (int i=0;i<splittedMsg.length;i++) {
            for(int j=0;j<words.length;j++) {
                if(splittedMsg[i].equals(words[j])) {
                    splittedMsg[i] = applyStars(splittedMsg[i]);
                }
            }
        }
        String result = String.join(" ", splittedMsg);
        message.setMessage(result);
    }

    private String applyStars(String msg) {

        for(int i=0;i<msg.length();i++) {
            msg = msg.replace(msg.charAt(i), '*');
        }
        return msg;
    }

}
