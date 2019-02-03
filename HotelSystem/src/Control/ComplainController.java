package Control;

import Connect.ConnectDB;
import Entity.Client;
import Entity.Complain;
import Entity.Entry;
import Entity.Organization;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.ArrayList;

public class ComplainController {
    private Connection connection = ConnectDB.getConnection();

    @FXML
    private TextField text;
    @FXML
    private ComboBox<String> fioClient;
    @FXML
    private TableView<Complain> tableCom;
    @FXML
    private TableColumn<Complain, String> famColum;
    @FXML
    private TableColumn<Complain, String> ComplainsColum;
    private ObservableList<Complain> comList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        famColum.setCellValueFactory(new PropertyValueFactory<Complain, String>("fio"));
        ComplainsColum.setCellValueFactory(new PropertyValueFactory<Complain, String>("text"));
        tableCom.setItems(comList);
        ref();
        ref_Client();
    }
    //добавляем жалобу
    @FXML
    public void addCom(ActionEvent actionEvent) {
        try {
            String sql_add_org = "INSERT INTO Complaints (fk_client, textcomplaints) VALUES (?,?); ";
            PreparedStatement ps1 = connection.prepareStatement(sql_add_org);
            ps1.setInt(1, get_id_client());
            ps1.setString(2, text.getText());
            ps1.executeUpdate();
            ps1.close();
            text.clear();
            ref();
        } catch (Exception ex) {
            System.out.print("Ошибка");
        }
    }
    //удаляем жалобу
    @FXML
    public void delete(ActionEvent actionEvent) {
        String del = "Delete From complaints WHERE complaints.textcomplaints = ?";
        Complain client = tableCom.getSelectionModel().getSelectedItem();
        if (client!= null){
            try {
                PreparedStatement ids = connection.prepareStatement(del);
                ids.setString(1,tableCom.getSelectionModel().getSelectedItem().getText());
                ids.execute();
                ids.close();
                tableCom.getItems().clear();
                tableCom.refresh(); ref ();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //обновление таблицы
    public void ref(){
        tableCom.getItems().clear();
        try {
            ArrayList<Complain> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT client.surname, client.name, client.patronymic, complaints.textcomplaints FROM complaints, client WHERE complaints.fk_client = client.pk_client");
            while(resultSet.next()){
                String name = resultSet.getString(1);
                String name2 = resultSet.getString(2);
                String name3 = resultSet.getString(3);
                String name4 = resultSet.getString(4);
                String name_res = name + " " + name2 + " " + name3;
                buffer_ArrayList.add(new Complain(name_res, name4));
            }
            ObservableList <Complain> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            Lst.forEach(item -> {
                comList.add(item);
            });
            resultSet.close();
            tableCom.refresh();
        }catch (Exception ex){
            System.out.print("Ошибка");
        }
    }
    //получаем ид клиента
    public int get_id_client (){
        Statement statement = null;
        int pk_client = 0;
        try {
            statement = connection.createStatement();
            String str =  fioClient.getValue();
            String [] mass_fio = str.split(" ");
            ResultSet resultSet = statement.executeQuery("SELECT client.pk_client FROM client " +
                    " WHERE client.surname = '" + mass_fio[0] + "' AND client.name = '" + mass_fio[1] + "' AND Client.patronymic = '" + mass_fio[2] +"'");
            while(resultSet.next()) {
                pk_client = resultSet.getInt(1);
            }
            System.out.print(pk_client);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pk_client;
    }
    //обработка нажатия на кнопку обновить
    @FXML
    public void refresh_Client(ActionEvent actionEvent) {
        ref_Client();
    }
    //обновляем список клиентов
    public void ref_Client(){
        try{
            ArrayList<String> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT client.surname, client.name, client.patronymic FROM client");
            while(resultSet.next()){
                String name = resultSet.getString(1);
                String name2 = resultSet.getString(2);
                String name3 = resultSet.getString(3);
                String name_res = name + " " + name2 + " " + name3;
                buffer_ArrayList.add(name_res);
            }
            ObservableList <String> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            fioClient.setItems(Lst);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}




