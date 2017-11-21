package frontend;

import backend.Client;
import backend.ClientsDAO;
import backend.ServerObject;
import backend.ServersDAO;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialog;
import com.googlecode.lanterna.gui2.table.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maciek on 25.10.2017.
 */
public class ClientGUI extends WindowCore {

	private ServersDAO db;
	private boolean dbLoaded = false;
	private ArrayList<ServerObject> list;
	private int onOrOf = 0; // 0 to all 1 to tylko online
	private String butonlineOroffline = "Pokaz Online";
	private int notPublicCount = 0;


	private Table publicServers() {
		Table<String> table = new Table<>("Nazwa serwera", "Adres ip", "Port", "Uzytkownicy", "Zabezpieczony", "Status");
		table.setPreferredSize(new TerminalSize(105, 20));
		table.setVisibleRows(16);

		if (dbLoaded == false) {
            db = ServersDAO.getInstance();
            list = db.loadAll();
            dbLoaded = true;
        }

		if(onOrOf == 0) {
			for (ServerObject s : list) {
			    if(s.isS_public())
                {
                    boolean isOnline = s.isOnline();
                    if (isOnline && s.getPassword() != null)
                        table.getTableModel().addRow(s.getName(), s.getIp(), s.getPort(), String.valueOf(s.getCurrent()), "Tak", "Online");
                    else if(isOnline && s.getPassword() == null)
                        table.getTableModel().addRow(s.getName(), s.getIp(), s.getPort(), String.valueOf(s.getCurrent()), "", "Online");
                    else if(!isOnline && s.getPassword() != null)
                        table.getTableModel().addRow(s.getName(), s.getIp(), s.getPort(), String.valueOf(s.getCurrent()), "Tak", "Offline");
                    else
                        table.getTableModel().addRow(s.getName(), s.getIp(), s.getPort(), String.valueOf(s.getCurrent()), "", "Offline");
                }
                else
                    notPublicCount++;

			}
		}else {
			for (ServerObject s : list) {
                if(s.isS_public())
                {
                    boolean isOnline = s.isOnline();
                    if (isOnline && s.getPassword() != null)
                        table.getTableModel().addRow(s.getName(), s.getIp(), s.getPort(), String.valueOf(s.getCurrent()), "Tak", "Online");
                    else if(isOnline && s.getPassword() == null)
                        table.getTableModel().addRow(s.getName(), s.getIp(), s.getPort(), String.valueOf(s.getCurrent()), "", "Online");
                }
                else
                    notPublicCount++;
			}
		}

		table.setSelectAction(new Runnable() {
			@Override
			public void run() {
				List<String> data = table.getTableModel().getRow(table.getSelectedRow());
				int rowNumber = table.getSelectedRow() + notPublicCount - 1;
				if (data.get(data.size() - 1).equals("Online")) {

					String nick;
					String port = data.get(2);
					String ip = data.get(1);

					ServerObject server = null;
					for (ServerObject s : list) {
						if (s.getPort().equals(port) && s.getIp().equals(ip)) {
							server = s;
							break;
						}
					}

					if (data.get(data.size() - 2) != null) // zabezpieczony
					{
						String password = TextInputDialog.showPasswordDialog(textGUI, "", "Podaj haslo", "");

						if (server.getPassword().equals(password)) {
							nick = TextInputDialog.showDialog(textGUI, "", "Podaj nick", "");
							ChatGUI chatGUI = new ChatGUI();
							Client client = new Client(nick, server.getIp(), Integer.parseInt(server.getPort()));
							ClientsDAO clientsDAO = new ClientsDAO(); // dodawanie do online list
							clientsDAO.addClient(server.getId(), nick);
							clientsDAO.updateCurrentUsers(server.getId(), true);
							clientsDAO.startUpdatingUsers(server.getId());
					        client.startClient();
                            chatGUI.show(server.getId(), nick);
                        }
                        else
                            MessageDialog.showMessageDialog(textGUI, "INFO", "Zle haslo!");
                    }
                    else {
                        nick = TextInputDialog.showDialog(textGUI, "", "Podaj nick", "");
                        ChatGUI chatGUI = new ChatGUI();
                        Client client = new Client(nick, server.getIp(), Integer.parseInt(server.getPort()));
                        ClientsDAO clientsDAO = new ClientsDAO();   //dodawanie do online list
                        clientsDAO.addClient(server.getId(), nick);
                        clientsDAO.updateCurrentUsers(server.getId(), true);
                        clientsDAO.startUpdatingUsers(server.getId());
                        client.startClient();
                        chatGUI.show(server.getId(), nick);
                    }
                } else {
                    MessageDialog.showMessageDialog(textGUI, "INFO", "Serwer offline !");
                }

			}
		});
		return table;
	}

	public void show() {

		contentPanel = new Panel();
		Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
		contentPanel.setLayoutManager(new GridLayout(3));
		Panel publicServers = new Panel();
		publicServers.setPreferredSize(new TerminalSize(77, 20));

		contentPanel.removeAllComponents();

		Button custom = new Button("Polacz recznie", new Runnable() {
			@Override
			public void run() {
				String ip = TextInputDialog.showDialog(textGUI, "", "Podaj IP", "");
				String port = TextInputDialog.showDialog(textGUI, "", "Podaj port", "");
				int port2 = Integer.parseInt(port);

                ServerObject server = null;
				for (ServerObject s : list) {
					if (s.getPort().equals(port) && s.getIp().equals(ip)) {
						server = s;
						break;
					}
				}
                String password;
                if (server != null && !server.getPassword().isEmpty())
                {
                    password = TextInputDialog.showPasswordDialog(textGUI, "", "Podaj haslo", "");

                    if (server.getPassword().equals(password)) {    //dobre haslo
                        String nick = TextInputDialog.showDialog(textGUI, "", "Podaj nick", "");
                        if (server.isOnline()) {
                            ChatGUI chatGUI = new ChatGUI();
                            Client client = new Client(nick, ip, port2);
                            ClientsDAO clientsDAO = new ClientsDAO();   //dodawanie do online list
                            clientsDAO.addClient(server.getId(), nick);
                            clientsDAO.updateCurrentUsers(server.getId(), true);
                            clientsDAO.startUpdatingUsers(server.getId());
                            client.startClient();
                            chatGUI.show(server.getId(),nick);
                        }else
                            MessageDialog.showMessageDialog(textGUI, "INFO", "Serwer offline !");
                    }else
                        MessageDialog.showMessageDialog(textGUI, "INFO", "Bledne haslo !");
                }else if(server == null)                                                                    //brak serwera
                    MessageDialog.showMessageDialog(textGUI, "INFO", "Serwer nie istnieje !");
                else {                                                                                      //serwer bez hasla
                    String nick = TextInputDialog.showDialog(textGUI, "", "Podaj nick", "");
                    if (server.isOnline()) {
                        ChatGUI chatGUI = new ChatGUI();
                        Client client = new Client(nick, ip, port2);
                        ClientsDAO clientsDAO = new ClientsDAO();   //dodawanie do online list
                        clientsDAO.addClient(server.getId(), nick);
                        clientsDAO.updateCurrentUsers(server.getId(), true);
                        clientsDAO.startUpdatingUsers(server.getId());
                        client.startClient();
                        chatGUI.show(server.getId(),nick);
                    }else
                        MessageDialog.showMessageDialog(textGUI, "INFO", "Serwer offline !");
                }

			}
		});

		Button onlineOffline = new Button(butonlineOroffline, new Runnable() {
			@Override
			public void run() {
				if (onOrOf == 0) {
					butonlineOroffline = "Pokaz All";
					onOrOf = 1;
				} else {
					butonlineOroffline = "Pokaz Online";
					onOrOf = 0;
				}
				show();
			}
		});
		buttonPanel.addComponent(new EmptySpace(new TerminalSize(15, 10)));
		buttonPanel.addComponent(custom);
		buttonPanel.addComponent(new EmptySpace(new TerminalSize(4, 10)));
		buttonPanel.addComponent(onlineOffline);
		publicServers.addComponent(publicServers());
		contentPanel.addComponent(new EmptySpace(new TerminalSize(13, 20)));
		contentPanel.addComponent(publicServers.withBorder(Borders.singleLine("Serwery publiczne")));
		contentPanel.addComponent(new EmptySpace(new TerminalSize(5, 20)));
		
		contentPanel.addComponent(new EmptySpace(new TerminalSize(3, 3)));
		contentPanel.addComponent(new EmptySpace(new TerminalSize(3, 3)));
		contentPanel.addComponent(new EmptySpace(new TerminalSize(3, 3)));
		
		contentPanel.addComponent(new EmptySpace(new TerminalSize(13, 10)));
		contentPanel.addComponent(buttonPanel);
		contentPanel.addComponent(new EmptySpace(new TerminalSize(13, 10)));
		window.setComponent(contentPanel);
		textGUI.addWindowAndWait(window);

	}

}
