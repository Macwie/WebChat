package backend;

import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable{

    private String userName;
    private String serverHost;
    private int serverPort;
    private Thread thread;
    private static boolean running = false;

    public Client(String userName, String host, int portNumber){
        this.userName = userName;
        this.serverHost = host;
        this.serverPort = portNumber;
        this.thread = null;
    }

    public void startClient() {
        if (thread == null)
        {
            thread = new Thread(this);
            thread.setDaemon(true);
            running = true;
            thread.start();
        }
    }

    public static void stopClient() {
        running = false;
    }

    private void startCommunication(){
        try{
            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000); 

            ServerThread serverThread = new ServerThread(socket, userName);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            while(serverAccessThread.isAlive() && running){
                if(Controllers.chatViewController.ready){
                    serverThread.addNextMessage(Controllers.chatViewController.getInput());//gettext
                    Controllers.chatViewController.ready = false;
                    //ChatGUI.textInput.setText("");
                    Controllers.chatViewController.clearInput();
                }
                else {
                   Thread.sleep(200);
                }
            }
        }catch(IOException ex){
            System.err.println("Fatal Connection error!");
            ex.printStackTrace();
        }catch(InterruptedException ex){
            System.out.println("Interrupted");
        }
    }

    @Override
    public void run() {
        startCommunication();
    }

}
