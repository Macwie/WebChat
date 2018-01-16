package backend.messages;


public interface Strategy {
    void censor(Message message);
}
