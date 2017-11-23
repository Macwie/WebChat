package backend.fxcontrollers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

import backend.Controllers;
import backend.ServerObject;
import backend.ServersDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.Main;

public class ClientViewController implements Initializable{
	private ServersDAO db;
	private ArrayList<ServerObject> list;
	Stage window ;

	   @FXML
	    private Button connectManuallyButton;

	    @FXML
	    private Button switchonlineButton;

	    @FXML
	    private TableView<ServerObject> ServerTable;

	    @FXML
	    private TableColumn<?, ?> ipColumn;

	    @FXML
	    private TableColumn<?, ?> portColumn;

	    @FXML
	    private TableColumn<?, ?> usersColumn;

	    @FXML
	    private TableColumn<?, ?> protectedColumn;

	    @FXML
	    private TableColumn<?, ?> statusColumn;
	    
	    @FXML
	    private TableColumn<?, ?> nameColumn;
    
    
    public ArrayList<ServerObject> getList() {
			return list;
		}




	public ObservableList<ServerObject> getServers(){
    	ObservableList<ServerObject> servers = FXCollections.observableArrayList();
    	 for (int i = 0; i<list.size();i++) {
    		 if(list.get(i).isS_public()) {
    			 if(list.get(i).isOnline() && list.get(i).getPassword() != null){
    				list.get(i).setProtect("TAK");
    				list.get(i).setOnline("ONLINE");
    				servers.add(list.get(i));
    			 }else if(list.get(i).isOnline() && list.get(i).getPassword() == null) {
    				list.get(i).setProtect("NIE");
				 	list.get(i).setOnline("ONLINE");
				 	servers.add(list.get(i));
    			 }
                 else if(!list.get(i).isOnline() && list.get(i).getPassword() != null) {
                	 list.get(i).setProtect("TAK");
    				 list.get(i).setOnline("OFFLINE");
    				 servers.add(list.get(i));
                 }
                 else {
                	 list.get(i).setProtect("NIE");
    				 list.get(i).setOnline("OFFLINE");
    				 servers.add(list.get(i));
                 }
    		 }
    	 }
		return servers;
    	
    }
    
    
    
    
    @FXML
    private void startMConnection(ActionEvent event) {
   	 Parent root;
        try {
        	
        	window = new Stage();
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/mConnectionView.fxml"));
        	root = (Parent) loader.load();
			Controllers.mConnectionViewController = loader.getController();
       	 	Scene scene = new Scene(root);
       	 	window.initModality(Modality.APPLICATION_MODAL);
            window.setScene(scene);
            window.setTitle("Server Settings");
            window.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public void close() {
    	window.close();
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		db = ServersDAO.getInstance();
        list = db.loadAll();
 
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        portColumn.setCellValueFactory(new PropertyValueFactory<>("port"));
        usersColumn.setCellValueFactory(new PropertyValueFactory<>("current"));
        protectedColumn.setCellValueFactory(new PropertyValueFactory<>("protect"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("online"));
        ServerTable.setItems(getServers());
	}
    
    

}
