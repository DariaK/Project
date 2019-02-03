package Control;

import Connect.ConnectDB;
import Entity.Client;
import Entity.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by user on 26.12.2018.
 */
public class ControllerEntry {
    @FXML
    private TextField passText;
    @FXML
    private ComboBox<String> CorpusComboBox;
    @FXML
    private TextField nameText;
    @FXML
    private TextField telText;
    @FXML
    private ComboBox<String> PlaseComboBox;
    @FXML
    private TextField costText;
    @FXML
    private TextField patrText;
    @FXML
    private TextField surnameTexr;
    @FXML
    private ComboBox<String> RoomComboBox;
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();
    @FXML
    private TableView<Client> tableClient;
    @FXML
    private TableColumn<Client, String> surnameColumn;
    @FXML
    private TableColumn<Client, String> nameColumn;
    @FXML
    private TableColumn<Client, String> patronymicColumn;
    @FXML
    private TableColumn<Client, String> phoneNumberColumn;
    @FXML
    private CheckBox pul;
    @FXML
    private CheckBox him;
    @FXML
    private CheckBox praz;
    @FXML
    private CheckBox sauna;
    @FXML
    private CheckBox clin;
    @FXML
    private CheckBox eda;
    @FXML
    private CheckBox biliard;
    @FXML
    private DatePicker DepDate;
    @FXML
    private DatePicker entryDate;
    public String check_pass_id = "";
    private Connection connection = ConnectDB.getConnection();
    @FXML
    void initialize() {
        //=================
        // устанавливаем тип и значение которое должно хранится в колонке
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("surname"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        patronymicColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("patronymic"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("phoneNumber"));
        tableClient.setItems(clientsList);
        refresh_Table_Client();

        //=================
        //устанавливаем комбо боксы
        set_Corpus();

    }

    public void refresh_Table_Client (){
        tableClient.getItems().clear();
        // обновляем данные в таблице с помощью SELECT запроса
        try {
            String sql_info = "SELECT Client.surname, Client.name, Client.patronymic, Client.phonenumber, Client.passportid" +
                    " From Client;";
            ArrayList<Client> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql_info);
            while(resultSet.next()){
                String surname = resultSet.getString(1);
                String name = resultSet.getString(2);
                String patronymic = resultSet.getString(3);
                String phonenumber = resultSet.getString(4);
                String passportid = resultSet.getString(5);
                buffer_ArrayList.add(new Client(surname, name, patronymic, phonenumber, passportid));
            }
            ObservableList <Client> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            Lst.forEach(item -> {
                clientsList.add(item);
            });
            tableClient.refresh();
            statement.close();
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.print("Ошибка");
        }
    }

    @FXML
    public void poisk(ActionEvent actionEvent) {
    }

    @FXML
    public void addClientInEntry(ActionEvent actionEvent) {
        Client client = tableClient.getSelectionModel().getSelectedItem();
        if (client != null) {
            surnameTexr.setText(tableClient.getSelectionModel().getSelectedItem().getSurname());
            nameText.setText(tableClient.getSelectionModel().getSelectedItem().getName());
            patrText.setText(tableClient.getSelectionModel().getSelectedItem().getPatronymic());
            telText.setText(tableClient.getSelectionModel().getSelectedItem().getPhoneNumber());
            passText.setText(tableClient.getSelectionModel().getSelectedItem().getPassport());
            check_pass_id = tableClient.getSelectionModel().getSelectedItem().getPassport();
        }
    }


    @FXML
    void AddEntry(ActionEvent event) {
        try{
            //получаем id клиента
            int id_client = check_id_client();
            int id_room = check_id_room();
            String id_reserv = null;
            String sql_add_entry = "INSERT INTO Entry (fk_room, fk_client, entrydate, departuredate, Costs) \n" +
                    "VALUES (?, ?, ?, ?, ?)";
            try {
                PreparedStatement ids = connection.prepareStatement(sql_add_entry);
                ids.setInt(1, id_room);
                ids.setInt(2, id_client);
                ids.setDate(3, java.sql.Date.valueOf(entryDate.getValue()));
                ids.setDate(4, java.sql.Date.valueOf(DepDate.getValue()));
                ids.setInt(5, 12);
                ids.execute();
                ids.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            int get_free_plase_in_room_entry = free_plase_in_room_entry(id_room);
            int plase_in_room = get_free_plase_in_room_entry - get_plase() +1;
            try {
                PreparedStatement ids1 = connection.prepareStatement("UPDATE RoomsExist SET freeplaces = ?, stateroom = ? WHERE RoomsExist.fk_room = "+ id_room);
                ids1.setInt(1, plase_in_room);
                ids1.setString(2, "Занята");
                ids1.execute();
                ids1.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.print("Ошибка");
        }



    }

    public  int free_plase_in_room_entry(int numroom){
        int free = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT roomsexist.freeplaces FROM roomsexist, room WHERE roomsexist.fk_room = Room.pk_room AND room.numberroom = " + numroom);
            while(resultSet.next()) {
                free = resultSet.getInt(1);
            }
            System.out.print(free);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return free;
    }

    public  int check_id_client (){
        int id_client = 0;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT client.pk_client FROM client WHERE client.passportid= '" + check_pass_id + "'");
            while(resultSet.next()) {
                id_client = resultSet.getInt(1);
            }
            System.out.print(id_client);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id_client;
    }

    public int get_plase (){
        int col_plase = Integer.parseInt(PlaseComboBox.getValue());
        return col_plase;
    }

    public  int check_id_room () {
        int pk_room = 0;
        try {
            Statement statement = connection.createStatement();
            String str =  RoomComboBox.getValue();
            int str2 = Integer.parseInt(str);
            ResultSet resultSet = statement.executeQuery("SELECT room.pk_room FROM room WHERE room.numberroom = " + str2);
            while(resultSet.next()) {
                pk_room = resultSet.getInt(1);
            }
            System.out.print(pk_room);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pk_room;
    }

    @FXML
    public void CorpusAction (ActionEvent actionEvent){
       /* biliard.setSelected(false);
        sauna.setSelected(false);
        clin.setSelected(false);
        him.setSelected(false);
        eda.setSelected(false);
        praz.setSelected(false);
        pul.setSelected(false);*/

        Statement statement = null;
        int pk_corp = 0;
        try {
            statement = connection.createStatement();
            String str =  CorpusComboBox.getValue();
            ResultSet resultSet = statement.executeQuery("SELECT hotelbuilding.pk_hotelbuilding FROM hotelbuilding, hotelclass WHERE hotelbuilding.fk_hotelclass = hotelclass.pk_hotelclass AND  hotelclass.category = '" + str + "'");
            while(resultSet.next()) {
                pk_corp = resultSet.getInt(1);
            }
            System.out.print(pk_corp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        set_Room(pk_corp);
        statement = null;

        try {
            String [] mass_str = new String[9];
            String str =  CorpusComboBox.getValue();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT hotelbuilding.billiards, hotelbuilding.cleaning, hotelbuilding.drycleaning, hotelbuilding.food, hotelbuilding.laundry, hotelbuilding.pool, hotelbuilding.sauna" +
                    " FROM hotelbuilding, hotelclass WHERE hotelbuilding.fk_hotelclass = hotelclass.pk_hotelclass AND  hotelclass.category = '" + str + "'");
            while(resultSet.next()) {
                mass_str[0] = resultSet.getString(1);
                mass_str[1] = resultSet.getString(2);
                mass_str[2] = resultSet.getString(3);
                mass_str[3] = resultSet.getString(4);
                mass_str[4] = resultSet.getString(5);
                mass_str[5] = resultSet.getString(6);
                mass_str[6] = resultSet.getString(7);
                System.out.print(mass_str[0] + mass_str[1]+ mass_str[2] + mass_str[3] + mass_str[4] + mass_str[5] + mass_str[6]);

            }
            if (mass_str[0].equals("1"))
                biliard.setSelected(true);
            else  biliard.setSelected(false);
            if (mass_str[1].equals("1"))
                clin.setSelected(true);
            else clin.setSelected(true);
            if (mass_str[2].equals("1"))
                him.setSelected(true);
            else him.setSelected(true);
            if (mass_str[3].equals("1"))
                eda.setSelected(true);
            else eda.setSelected(true);
            if (mass_str[4].equals("1"))
                praz.setSelected(true);
            else praz.setSelected(true);
            if (mass_str[5].equals("1"))
                pul.setSelected(true);
            else pul.setSelected(true);
            if (mass_str[6].equals("1"))
                sauna.setSelected(true);
            else sauna.setSelected(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    @FXML
    public void RoomAction (ActionEvent actionEvent){
        int pk_room = check_id_room();
        set_Plase(pk_room);
    }

    @FXML
    public void PlaseAction (ActionEvent actionEvent){

    }

    public void set_Corpus(){
        try{
            ArrayList<String> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT HotelClass.category FROM HotelClass");
            while(resultSet.next()){
                String name = resultSet.getString(1);
                buffer_ArrayList.add(name);
            }
            ObservableList <String> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            CorpusComboBox.setItems(Lst);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void set_Room(int pk_corp){
        try{
            ArrayList<String> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            String sql_first_Corpus = "SELECT Room.numberroom FROM Room, hotelbuilding WHERE Room.fk_hotel = hotelbuilding.pk_hotelbuilding AND hotelbuilding.fk_hotelclass = '" + pk_corp + "'";
            ResultSet resultSet = statement.executeQuery(sql_first_Corpus);
            while(resultSet.next()){
                String name = resultSet.getString(1);
                buffer_ArrayList.add(name);
            }
            ObservableList <String> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            RoomComboBox.setItems(Lst);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void set_Plase(int pk_room){
        try{
            int freepla = 0;
            ArrayList<String> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT roomsexist.freeplaces FROM roomsexist, room WHERE room.pk_room = roomsexist.fk_room AND room.pk_room = "+pk_room);
            while(resultSet.next()){
                freepla = resultSet.getInt(1);
                System.out.print(freepla);
            }
            if (freepla == 0){
                buffer_ArrayList.add("Эта комната занята");
            }
            else if (freepla == 1){
                buffer_ArrayList.add("1");
            }
            else if (freepla == 2){
                buffer_ArrayList.add("1");
                buffer_ArrayList.add("2");
            }
            else if (freepla == 3){
                buffer_ArrayList.add("1");
                buffer_ArrayList.add("2");
                buffer_ArrayList.add("3");
            }
            ObservableList <String> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            PlaseComboBox.setItems(Lst);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
