package wgu_c195.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static Connection connection;

    public static void init() {
        try {
            System.out.println("----> Trying to connect to the db..");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(Const.SERVER_URL, Const.SERVER_USER_ID, Const.SERVER_PASSWORD);
            System.out.println("----> Connected to db.");
        } catch (ClassNotFoundException e) {
            System.out.println("----> Check the driver file.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void disconnect() {
        try {
            connection.close();
            System.out.println("----> Connection closed.");
        } catch (Exception e) {
            System.out.println("----> Error occurred while disconnecting from the db.");
            e.printStackTrace();
        }
    }

}
