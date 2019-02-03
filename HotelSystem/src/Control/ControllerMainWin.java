package Control;

import Connect.ConnectDB;
import Entity.Client;
import Entity.Entry;
import Entity.Organization;
import Entity.Reserv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.sql.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ControllerMainWin {

    private Connection connection = ConnectDB.getConnection();
    @FXML
    private TextArea infoOfRoom;
    @FXML
    private TableColumn<Entry, String> ClientColumn;
    @FXML
    private TableColumn<Entry, String> NumberRoomColumn;
    @FXML
    private TableView<Entry> tableOfRooms;
    @FXML
    private TableColumn<Entry, String> CorpusColumn;
    @FXML
    private TableColumn<Entry, String> StateColumn;
    @FXML
    private TableColumn<Entry, String> DateEntryColumn;
    @FXML
    private TableColumn<Entry, String> DateDepColumn;
    @FXML
    private TableColumn<Entry, String> FloorColumn;
    private ObservableList<Entry> zaselenie_List = FXCollections.observableArrayList();
    //=========================

    @FXML
    private TableColumn<Reserv, String> PlaseCol;

    @FXML
    private TableColumn<Reserv, String> EntryCol;

    @FXML
    private TableColumn<Reserv, String> TselCol;

    @FXML
    private TableColumn<Reserv, String> RoomCol;

    @FXML
    private TableColumn<Reserv, String> OtmenaCol;

    @FXML
    private TableView<Reserv> tableReserv;

    @FXML
    private TableColumn<Reserv, String> OrgCol;

    @FXML
    private TableColumn<Reserv, String> FloorCol;
    private ObservableList<Reserv> reserv_List = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        // заносим информацию в поле слева
        infoOfRoom.setText("Заселено: " + get_client_entry() + "\n" + "Свободно комнат: " + get_free_room());
        // устанавливаем тип и значение которое должно хранится в колонке
        CorpusColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("corpus"));
        FloorColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("floor"));
        NumberRoomColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("numberRoom"));
        StateColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("state"));
        ClientColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("client"));
        DateEntryColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("DateEntry"));
        DateDepColumn.setCellValueFactory(new PropertyValueFactory<Entry, String>("DateDep"));

        tableOfRooms.setItems(zaselenie_List);
        table_ref();


        // устанавливаем тип и значение которое должно хранится в колонке
        OrgCol.setCellValueFactory(new PropertyValueFactory<Reserv, String>("organiz"));
        TselCol.setCellValueFactory(new PropertyValueFactory<Reserv, String>("obj"));
        FloorCol.setCellValueFactory(new PropertyValueFactory<Reserv, String>("floor"));
        RoomCol.setCellValueFactory(new PropertyValueFactory<Reserv, String>("rooms"));
        PlaseCol.setCellValueFactory(new PropertyValueFactory<Reserv, String>("plase"));
        EntryCol.setCellValueFactory(new PropertyValueFactory<Reserv, String>("entrydate"));
        OtmenaCol.setCellValueFactory(new PropertyValueFactory<Reserv, String>("otmdate"));

        tableReserv.setItems(reserv_List);
        table_ref2();
        set_Corpus();
        set_Plase();
        set_obj();
        refresh_org();
    }
    @FXML
    public void Btn_up() {
        table_ref();
    }

    public int get_free_room (){
        String sql = "SELECT COUNT (RoomsExist.fk_room)" +
                " From RoomsExist" +
                " WHERE RoomsExist.StateRoom = 'Свободна'";
        int count = 0;
        try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while(resultSet.next()){
                    count = resultSet.getInt(1);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        return count;
    }

    public int get_client_entry (){
        String sql = "SELECT COUNT (Entry.pk_entry) " +
                "From Entry " +
                "WHERE (SELECT CAST(GETDATE() AS DATE)) between Entry.EntryDate AND Entry.DepartureDate";
        int count = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                count = resultSet.getInt(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }

//Заселенные комнаты
    public void table_ref (){
        tableOfRooms.getItems().clear();
        String sql = "SELECT HotelClass.Category, Room.Floors, Room.NumberRoom, RoomsExist.StateRoom, Client.Surname, Entry.EntryDate, Entry.DepartureDate" +
                " FROM HotelBuilding, HotelClass, Room, RoomsExist, Entry, Client " +
                " WHERE Client.PK_client = Entry.FK_Client AND Entry.FK_Room = Room.PK_Room AND" +
                " Room.FK_Hotel = HotelBuilding.PK_HotelBuilding AND HotelClass.PK_HotelClass = HotelBuilding.FK_HotelClass AND " +
                " RoomsExist.FK_Room = Room.PK_Room AND \n" +
                " RoomsExist.StateRoom = 'Занята' AND (SELECT CAST(GETDATE() AS DATE)) between Entry.EntryDate AND Entry.DepartureDate";
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ArrayList<Entry> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String corpus = resultSet.getString(1);
                String floor  = resultSet.getString(2);
                String numberRoom = resultSet.getString(3);
                String state  = resultSet.getString(4);
                String client  = resultSet.getString(5);
                Date dateE = resultSet.getDate(6);
                String DateEntry = formatter.format(dateE);
                Date dateD = resultSet.getDate(7);
                String DateDep  = formatter.format(dateD);
                buffer_ArrayList.add(new Entry(corpus, floor, numberRoom, state, client, DateEntry, DateDep ));
            }
            ObservableList <Entry> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            Lst.forEach(item -> {
                zaselenie_List.add(item);
            });
            resultSet.close();
            tableOfRooms.refresh();
        }catch (Exception ex){
            System.out.print("Ошибка");
        }
    }

    @FXML
    private ComboBox<String> PlasComboBox;
    @FXML
    private ComboBox<String> orgComboBox;
    @FXML
    private ComboBox<String> objComboBox;
    @FXML
    private ComboBox<String> CorpusComboBox;
    @FXML
    private DatePicker dateEntryReserv;
    @FXML
    private ComboBox<String> FloorComboBox;

    public void table_ref2(){
        tableReserv.getItems().clear();
        String sql = " SELECT organization.nameorganization, objective.nameobjective, reservation.floors, reservation.numberofrooms, reservation.numberofplaces, " +
                "  reservation.entrydate, reservation.datecancel" +
                " FROM organization, objective, reservation, contract " +
                " WHERE organization.pk_organization = contract.fk_organization AND contract.fk_objective = objective.pk_objective AND " +
                "  contract.pk_contract = reservation.fk_contract";
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            ArrayList<Reserv> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String name = resultSet.getString(1);
                String obj  = resultSet.getString(2);
                String flo = resultSet.getString(3);
                String numr  = resultSet.getString(4);
                String numpl  = resultSet.getString(5);
                Date dateE = resultSet.getDate(6);
                String DateEntry = formatter.format(dateE);
                Date dateD = resultSet.getDate(7);
                String DateDep  = formatter.format(dateD);
                buffer_ArrayList.add(new Reserv(name, obj, flo, numr, numpl, DateEntry, DateDep ));
            }
            ObservableList <Reserv> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            Lst.forEach(item -> {
                reserv_List.add(item);
            });
            resultSet.close();
            tableOfRooms.refresh();
        }catch (Exception ex){
            System.out.print("Ошибка");
        }
    }

    @FXML
    void add_Reserv(ActionEvent event) {
        //получаем id
        int id_hotel = get_id_hotel();
        try{
            String sql_add_reserv = "INSERT INTO reservation (fk_hotel, floors, numberofplaces, entrydate) VALUES " +
                    "  (?, ?, ?, ?);";
                PreparedStatement ps1 =connection.prepareStatement(sql_add_reserv);
                ps1.setInt(1,id_hotel);
                ps1.setInt(2,Integer.parseInt(FloorComboBox.getValue()));
                ps1.setInt(3,Integer.parseInt(PlasComboBox.getValue()));
                ps1.setDate(4, java.sql.Date.valueOf(dateEntryReserv.getValue()));
                ps1.executeUpdate();
                ps1.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        table_ref2();

    }

    public int get_id_hotel (){
        Statement statement = null;
        int pk_hotel = 0;
        try {
            statement = connection.createStatement();
            String str =  CorpusComboBox.getValue();
            ResultSet resultSet = statement.executeQuery("SELECT hotelclass.pk_hotelclass FROM hotelclass WHERE hotelclass.category = '" + str +"'");
            while(resultSet.next()) {
                pk_hotel = resultSet.getInt(1);
            }
            System.out.print(pk_hotel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pk_hotel;
    }


    public void set_obj(){
        try{
            ArrayList<String> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT objective.nameobjective FROM objective");
            while(resultSet.next()){
                String name = resultSet.getString(1);
                buffer_ArrayList.add(name);
            }
            ObservableList <String> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            objComboBox.setItems(Lst);
        }catch (Exception e){
            e.printStackTrace();
        }
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

    public void  set_Floor(int pk_corp){

        try{
            int floor = 0;
            ArrayList<String> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT hotelbuilding.floors FROM hotelbuilding WHERE hotelbuilding.pk_hotelbuilding = "+pk_corp);
            while(resultSet.next()){
                floor = resultSet.getInt(1);
            }
           if (floor == 1){
                buffer_ArrayList.add("1");
            }
            else if (floor == 2){
                buffer_ArrayList.add("1");
                buffer_ArrayList.add("2");
            }
            else if (floor == 3){
                buffer_ArrayList.add("1");
                buffer_ArrayList.add("2");
                buffer_ArrayList.add("3");
            }
           else if (floor == 4){
               buffer_ArrayList.add("1");
               buffer_ArrayList.add("2");
               buffer_ArrayList.add("3");
               buffer_ArrayList.add("4");
           }
            ObservableList <String> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            FloorComboBox.setItems(Lst);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void set_Plase (){
        ArrayList<String> buffer_ArrayList = new ArrayList<>();
        buffer_ArrayList.add("1");
        buffer_ArrayList.add("2");
        buffer_ArrayList.add("3");
        buffer_ArrayList.add("4");
        buffer_ArrayList.add("5");
        buffer_ArrayList.add("6");
        buffer_ArrayList.add("7");
        buffer_ArrayList.add("8");
        buffer_ArrayList.add("9");
        ObservableList <String> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
        PlasComboBox.setItems(Lst);
    }

    public void refresh_org(){
        try{
            ArrayList<String> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Organization.nameorganization FROM organization");
            while(resultSet.next()){
                String name = resultSet.getString(1);
                buffer_ArrayList.add(name);
            }
            ObservableList <String> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            orgComboBox.setItems(Lst);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void  onClickCorp(){
            try {
                String str =  CorpusComboBox.getValue();
                int pk_corp = 0;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT hotelbuilding.pk_hotelbuilding FROM hotelbuilding, hotelclass WHERE hotelbuilding.fk_hotelclass = hotelclass.pk_hotelclass AND  hotelclass.category = '" + str + "'");
                while(resultSet.next()) {
                    pk_corp = resultSet.getInt(1);
                }
                System.out.print(pk_corp);
                set_Floor(pk_corp);
            } catch (SQLException e) {
                e.printStackTrace();
            }



}




}
