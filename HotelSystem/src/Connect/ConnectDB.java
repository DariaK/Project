package Connect;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.Connection;

public class ConnectDB {

    private static final String USER = "postgres";
    private static final String PASS = "1234";
    private static final String URL = "jdbc:postgresql://localhost:5432/HotelDataBases?autoReconnect=true&useSSL=false";
    private static Connection connection;


    public static Connection getConnection() {
        if (connection==null)
            ConnectDB();
        return connection;
    }
    public static Connection Close() {
        connection=null;
        return connection ;
    }

    public static void ConnectDB() {

        Driver driver = null;
        try {

            driver = new org.postgresql.Driver();
            System.out.println("Работает Driver");
        } catch (Exception ex) {
            System.out.println("Ошибка создания драйвереа");
        }

        try {
            DriverManager.registerDriver(driver);
            System.out.println("Работает registrer");
        } catch (Exception ex2) {
            System.out.println("Не зарегистрирован драйвер");
        }

        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Cоединение установлено\n");

        } catch (Exception ex3) {
            ex3.printStackTrace();
            System.out.println("Соединение не произошло!");

        }
    }


}





