package Control;

import Connect.ConnectDB;
import Entity.Organization;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.ArrayList;

public class ControllerOrg {

    private Connection connection = ConnectDB.getConnection();
    @FXML
    private TextField nameOrg;
    @FXML
    private TextField adrOrg;
    @FXML
    private CheckBox checkDel;
    @FXML
    private TableView<Organization> tableOrganiz;
    @FXML
    private TableColumn<Organization, String> nameOrgColumn;
    @FXML
    private TableColumn<Organization, String> adrOrgColumn;
    private ObservableList<Organization> organizList = FXCollections.observableArrayList();

    private String src_name;
    private String src_org;

    @FXML
    void initialize() {
        // устанавливаем тип и значение которое должно хранится в колонке
        nameOrgColumn.setCellValueFactory(new PropertyValueFactory<Organization, String>("nameOrg"));
        adrOrgColumn.setCellValueFactory(new PropertyValueFactory<Organization, String>("adresOrg"));
        tableOrganiz.setItems(organizList);
        refresh_Table ();
    }

    @FXML
    void addNewOrg(ActionEvent event) {
        if (valid_data() == true) {

            try {
                String sql_add_org = "INSERT INTO organization (NameOrganization,AddressOrganization) VALUES (?,?); ";
                PreparedStatement ps1 = connection.prepareStatement(sql_add_org);
                ps1.setString(1, nameOrg.getText());
                ps1.setString(2, adrOrg.getText());
                ps1.executeUpdate();
                ps1.close();
                nameOrg.clear();
                adrOrg.clear();
                refresh_Table();
            } catch (Exception ex) {
                System.out.print("Ошибка");
            }
        }
    }

    @FXML
    void editOrg(ActionEvent event) {
        Organization org = tableOrganiz.getSelectionModel().getSelectedItem();
        if (org != null) {
            src_name = tableOrganiz.getSelectionModel().getSelectedItem().getNameOrg();
            src_org = tableOrganiz.getSelectionModel().getSelectedItem().getAdresOrg();
            nameOrg.setText(tableOrganiz.getSelectionModel().getSelectedItem().getNameOrg());
            adrOrg.setText(tableOrganiz.getSelectionModel().getSelectedItem().getAdresOrg());
        }
    }

    @FXML
    void saveEditText(ActionEvent event) {
        if (valid_data() == true) {
            String edit = "UPDATE organization SET nameorganization=?, addressorganization=? " +
                    "WHERE pk_organization =" +
                    "(Select pk_organization From organization Where nameorganization=? AND addressorganization=?);";
            try {
                PreparedStatement ids = connection.prepareStatement(edit);
                ids.setString(1, nameOrg.getText());
                ids.setString(2, adrOrg.getText());
                ids.setString(3, src_name);
                ids.setString(4, src_org);
                ids.execute();
                ids.close();
                nameOrg.clear();
                adrOrg.clear();
                tableOrganiz.getItems().clear();
                tableOrganiz.refresh();
                refresh_Table();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void deleteOrg(ActionEvent event) {
        String del= "Delete From organization WHERE pk_organization" +
                "=(Select pk_organization From organization Where nameorganization=? AND addressorganization=?);";
        Organization org = tableOrganiz.getSelectionModel().getSelectedItem();
        if (checkDel.isSelected() && org != null){
            try {
                PreparedStatement ids = connection.prepareStatement(del);
                ids.setString(1,tableOrganiz.getSelectionModel().getSelectedItem().getNameOrg());
                ids.setString(2,tableOrganiz.getSelectionModel().getSelectedItem().getAdresOrg());
                ids.execute();
                ids.close();
                tableOrganiz.getItems().clear();
                tableOrganiz.refresh(); refresh_Table ();
                checkDel.setSelected(false);
            } catch (SQLException e) {
                System.out.print("Ошибка");
                new Message("Ошибка при удалении", "Вы пытаетесь удалить орагнизацию, которая используется").showAlertDialog();
                }
        }
    }

    public void refresh_Table (){
        tableOrganiz.getItems().clear();
        try {
            ArrayList<Organization> buffer_ArrayList = new ArrayList<>();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM organization");
            while(resultSet.next()){
                String name = resultSet.getString(2);
                String adr = resultSet.getString(3);
                buffer_ArrayList.add(new Organization(name, adr));
            }
            ObservableList <Organization> Lst  =  FXCollections.observableArrayList(buffer_ArrayList);
            Lst.forEach(item -> {
                organizList.add(item);
            });
            resultSet.close();
            tableOrganiz.refresh();
        }catch (Exception ex){
            System.out.print("Ошибка");
        }
    }


    public boolean valid_data( ){
        String name_str = nameOrg.getText();
        String adrOrg_str = adrOrg.getText();


        if (name_str.length() < 2){
            new Message("Неправильное заполнение поля Название организации!",
                    " Поле не должно быть пустым и(или) содержать меньше 2 символов").showAlertDialog();
            return false;
        }
        if (adrOrg_str.length() <3){
            new Message("Неправильное заполнение поля Адрес!", " Поле не должно быть пустым и(или) содержать меньше 2 символов").showAlertDialog();

            return false;
        }
        return true;
    }

}
