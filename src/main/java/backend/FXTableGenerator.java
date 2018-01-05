package backend;

import backend.database.ServersDAO;
import backend.fxcontrollers.Controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;

import java.util.ArrayList;

public class FXTableGenerator extends Service<TableView> {

    private TableView<ServerObject> ServerTable;
    public static ArrayList<ServerObject> list;

    public FXTableGenerator(TableView<ServerObject> ServerTable) {
        this.ServerTable = ServerTable;
    }

    @Override
    protected Task<TableView> createTask() {
        return new Task<TableView>() {
            @Override
            protected TableView call() throws Exception {
                ServersDAO db = ServersDAO.getInstance();
                list = db.loadAll();
                ServerTable.setItems(getServers());

                return ServerTable;
            }
        };
    }

    public ObservableList<ServerObject> getServers(){
        ObservableList<ServerObject> servers = FXCollections.observableArrayList();
        boolean isOnline;
        
        if(Controllers.clientController.showOnlyPublic) {
        	for (int i = 0; i<list.size();i++) {
                isOnline = list.get(i).isOnline();
                if(list.get(i).isS_public()) {
                    if(isOnline && list.get(i).getPassword().length() != 0){
                        list.get(i).setProtect("YES");
                        list.get(i).setOnline("ONLINE");
                        servers.add(list.get(i));
                    }else if(isOnline && list.get(i).getPassword().length() == 0) {
                        list.get(i).setProtect("NO");
                        list.get(i).setOnline("ONLINE");
                        servers.add(list.get(i));
                    }
                    else if(!isOnline && list.get(i).getPassword().length() != 0) {
                        list.get(i).setProtect("YES");
                        list.get(i).setOnline("OFFLINE");
                        System.out.println(list.get(i).getPassword().length());
                        servers.add(list.get(i));
                    }
                    else {
                        list.get(i).setProtect("NO");
                        list.get(i).setOnline("OFFLINE");
                        servers.add(list.get(i));
                    }
                }
            }
        }else {
        	for (int i = 0; i<list.size();i++) {
                isOnline = list.get(i).isOnline();
                if(list.get(i).isS_public()) {
                    if(isOnline && list.get(i).getPassword().length()!= 0){
                        list.get(i).setProtect("YES");
                        list.get(i).setOnline("ONLINE");
                        servers.add(list.get(i));
                    }else if(isOnline && list.get(i).getPassword().length() == 0) {
                        list.get(i).setProtect("NO");
                        list.get(i).setOnline("ONLINE");
                        servers.add(list.get(i));
                    }
                }
            }
        }
        	
        	
        
        return servers;
    }

    public static ArrayList<ServerObject> getList() {
        return  list;
    }
}
