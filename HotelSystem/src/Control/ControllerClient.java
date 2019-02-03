package Control;


import Connect.ConnectDB;
import Entity.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.ArrayList;

public class ControllerClient {

    private Connection connection = ConnectDB.getConnection();
    //---------------Клиент
    @FXML
    private TextField patronymic;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField passport;
    @FXML
    private TextField surname;
    @FXML
    private TextField name;
    @FXML
    private TextField adres;
    @FXML
    private ComboBox<String> orgList;
    @FXML
    private CheckBox delClCheckBox;
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
    private TableColumn<Client, String> adresColumn;
    @FXML
    private TableColumn<Client, String> passColumn;
    @FXML
    private TableColumn<Client, String> phoneNumberColumn;
    @FXML
    private TableColumn<Client, String> orgColumn;
    private String check_pass_id;
    @FXML
    void initialize() {
        // устанавливаем тип и значение которое должно хранится в колонке
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("surname"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        patronymicColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("patronymic"));
        adresColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("adres"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("phoneNumber"));
        passColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("passport"));
        orgColumn.setCellValueFactory(new PropertyValueFactory<Client, String>("org"));
        tableClient.setItems(clientsList);
        refresh_Table();
        refresh_org();
    }

    // добавляем клиента
    @FXML
    void addNewClient1(ActionEvent event) {

        if (valid_data() == true){
            try{
                String sql_add_client = "INSERT INTO Client (fk_organization, surname, name, patronymic, phonenumber, passportid, address) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?); ";
                String sql_add_client_no_org = "INSERT INTO Client (surname, name, patronymic, phonenumber, passportid, address) " +
                        "VALUES (?, ?, ?, ?, ?, ?); ";
                String org = orgList.getValue();
                if (org == "[Не выбрано]"){
                    PreparedStatement ps1 =connection.prepareStatement(sql_add_client_no_org);
                    ps1.setString(1,surname.getText());
                    ps1.setString(2,name.getText());
                    ps1.setString(3,patronymic.getText());
                    ps1.setString(4,phoneNumber.getText());
                    ps1.setString(5,passport.getText());
                    ps1.setString(6,adres.getText());
                    ps1.executeUpdate();
                    ps1.close();
                }else {
                    PreparedStatement ps2 =connection.prepareStatement(sql_add_client);
                    ps2.setInt(1, get_id_org());
                    ps2.setString(2,surname.getText());
                    ps2.setString(3,name.getText());
                    ps2.setString(4,patronymic.getText());
                    ps2.setString(5,phoneNumber.getText());
                    ps2.setString(6,passport.getText());
                    ps2.setString(7,adres.getText());
                    ps2.executeUpdate();
                    ps2.close();
                }
                clear_table_and_editText();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    //редактируем клиента
    @FXML
    void editClient(ActionEvent event){
        Client client = tableClient.getSelectionModel().getSelectedItem();
        if (client != null) {
            surname.setText(tableClient.getSelectionModel().getSelectedItem().getSurname());
            name.setText(tableClient.getSelectionModel().getSelectedItem().getName());
            patronymic.setText(tableClient.getSelectionModel().getSelectedItem().getPatronymic());
            phoneNumber.setText(tableClient.getSelectionModel().getSelectedItem().getPhoneNumber());
            passport.setText(tableClient.getSelectionModel().getSelectedItem().getPassport());
            adres.setText(tableClient.getSelectionModel().getSelectedItem().getAdres());
            check_pass_id = tableClient.getSelectionModel().getSelectedItem().getPassport();
            orgList.setValue(tableClient.getSelectionModel().getSelectedItem().getOrg());
        }
    }
    //сохранить редактируемое
    @FXML
    void saveeditClient(ActionEvent event){

        String edit = "UPDATE Client SET surname=?, name=?, patronymic=?, phonenumber=?, passportid=?, address=?, fk_organization =?  \n" +
                "                WHERE pk_client =\n" +
                "                (Select pk_client From Client Where passportid=?);";
        String edit_no_org= "UPDATE Client SET surname=?, name=?, patronymic=?, phonenumber=?, passportid=?, address=?  \n" +
                "                WHERE pk_client =\n" +
                "                (Select pk_client From Client Where passportid=?);";

        if (valid_data() == true) {
            try {
                if (orgList.getValue() == "[Не выбрано]" || orgList.getValue() == "-") {
                    PreparedStatement ids2 = connection.prepareStatement(edit_no_org);
                    ids2.setString(1, surname.getText());
                    ids2.setString(2, name.getText());
                    ids2.setString(3, patronymic.getText());
                    ids2.setString(4, phoneNumber.getText());
                    ids2.setString(5, passport.getText());
                    ids2.setString(6, adres.getText());
                    ids2.setString(7, check_pass_id);
                    ids2.execute();
                    ids2.close();
                } else {
                    PreparedStatement ids = connection.prepareStatement(edit);
                    ids.setString(1, surname.getText());
                    ids.setString(2, name.getText());
                    ids.setString(3, patronymic.getText());
                    ids.setString(4, phoneNumber.getText());
                    ids.setString(5, passport.getText());
                    ids.setString(6, adres.getText());
                    ids.setInt(7, get_id_org());
                    ids.setString(8, check_pass_id);
                    ids.execute();
                    ids.close();
                }
                clear_table_and_editText();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //удалить клиента
    @FXML
    void delClient(ActionEvent event){
        String del = "Delete From Client WHERE passportid = ?";
        Client client = tableClient.getSelectionModel().getSelectedItem();
        if (delClCheckBox.isSelected() && client!= null){
            try {
                PreparedStatement ids = connection.prepareStatement(del);
                ids.setString(1,tableClient.getSelectionModel().getSelectedItem().getPassport());
                ids.execute();
                ids.close();
                tableClient.getItems().clear();
                tableClient.refresh(); refresh_Table ();
                delClCheckBox.setSelected(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void refresh_org(ActionEvent event) {
        refresh_org();
    }

    public void refresh_org(){
        try{
            ArrayList<String> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Organization.nameorganization FROM organization");
            buffer_ArrayList.add("[Не выбрано]");
            while(resultSet.next()){
                String name = resultSet.getString(1);
                buffer_ArrayList.add(name);
            }
            ObservableList <String> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            orgList.setItems(Lst);
            orgList.setValue("[Не выбрано]");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int get_id_org (){
        Statement statement = null;
        int pk_org = 0;
        try {
            statement = connection.createStatement();
            String str =  orgList.getValue();
            ResultSet resultSet = statement.executeQuery("SELECT organization.pk_organization FROM organization WHERE organization.nameorganization = '" + str + "'");
            while(resultSet.next()) {
                pk_org = resultSet.getInt(1);
            }
            System.out.print(pk_org);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pk_org;
    }

    public void clear_table_and_editText (){
        surname.clear();
        name.clear();
        patronymic.clear();
        phoneNumber.clear();
        passport.clear();
        adres.clear();
        orgList.setValue("[Не выбрано]");
        tableClient.getItems().clear();
        tableClient.refresh();
        refresh_Table();
    }

    public void refresh_Table (){
        tableClient.getItems().clear();
        // обновляем данные в таблице с помощью SELECT запроса
        try {
            String sql_info = "SELECT Client.surname, Client.name, Client.patronymic, Client.phonenumber, Client.passportid, Client.address, Organization.nameorganization " +
                    "From Client, Organization " +
                    "WHERE Client.fk_organization = Organization.pk_organization;";
            String sql_info_no_organiz = "SELECT Client.surname, Client.name, Client.patronymic, Client.phonenumber, Client.passportid, Client.address " +
                    "From Client WHERE Client.fk_organization IS NULL";
            ArrayList<Client> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql_info);
            while(resultSet.next()){
                String surname = resultSet.getString(1);
                String name = resultSet.getString(2);
                String patronymic = resultSet.getString(3);
                String phonenumber = resultSet.getString(4);
                String pass = resultSet.getString(5);
                String address = resultSet.getString(6);
                String nameorganization = resultSet.getString(7);
                buffer_ArrayList.add(new Client(surname, name, patronymic, phonenumber, pass, address,nameorganization));
            }
            ResultSet resultSet2 = statement.executeQuery(sql_info_no_organiz);
            while(resultSet2.next()){
                String surname2 = resultSet2.getString(1);
                String name2 = resultSet2.getString(2);
                String patronymic2 = resultSet2.getString(3);
                String phonenumber2 = resultSet2.getString(4);
                String pass2 = resultSet2.getString(5);
                String address2 = resultSet2.getString(6);
                buffer_ArrayList.add(new Client(surname2, name2, patronymic2, phonenumber2, pass2, address2, "-"));
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

    public boolean valid_data( ){
        String surname_str = surname.getText();
        String name_str = name.getText();
        String patronymic_str = patronymic.getText();
        String passport_str = passport.getText();
        String phoneNumber_str = phoneNumber.getText();
        String adres_str = adres.getText();

        if (surname_str.matches("(^[A-ZА-Я]{1}[a-zа-я]{1,24}?$)") == false){
            Message mes = new Message("Неправильное заполнение поля Фамилия!", "Пример: Иванов ").showAlertDialog();
            return false;
        }
        if (name_str.matches("(^[A-ZА-Я]{1}[a-zа-я]{1,24}?$)") == false){
            new Message("Неправильное заполнение поля Имя!", "Пример: Иван ").showAlertDialog();
            return false;
        }
        if (patronymic_str.matches("(^[A-ZА-Я]{1}[a-zа-я]{1,24}?$)") == false){
            new Message("Неправильное заполнение поля Отчество!", "Пример: Иванов ").showAlertDialog();
            return false;
        }
        if (phoneNumber_str.matches("^[+][0-9]{3}[0-9]{9}") == false){
            new Message("Неправильное заполнение поля Телефон!", "Пример: +375330000000").showAlertDialog();
            return false;
        }
        if (passport_str.matches("(^[A-Z]{2}[0-9]{7}?$)") == false){
            new Message("Неправильное заполнение поля Паспорт!", "Пример: CCDDDDDDD  (С — латинские буквы, D — арабские цифры) ").showAlertDialog();
            return false;
        }
        if (adres_str.length() <3){
            new Message("Неправильное заполнение поля Адрес!", "адрес не должен быть пустым").showAlertDialog();

            return false;
        }
        return true;
    }

}
