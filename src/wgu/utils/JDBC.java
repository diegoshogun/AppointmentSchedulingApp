package wgu.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Class handles starting and closing database connection.
 */
public abstract class JDBC {

    private static final String PROTOCOL = "jdbc";
    private static final String VENDOR = ":mysql:";
    private static final String LOCATION = "//localhost/";
    private static final String DATABASE_NAME = "client_schedule";
    private static final String JDBC_URL = PROTOCOL + VENDOR + LOCATION + DATABASE_NAME + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // driver reference
    private static final String USERNAME = "sqlUser"; // username
    private static final String PASSWORD = "Passw0rd!"; // password
    public static Connection CONNECTION; // connection interface

    /**
     * Starts the database connection.
     */
    public static void openConnection() {
        try {
            Class.forName(DRIVER); // locate driver
            CONNECTION = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD); // starting connection
            System.out.println("Connection Successful!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Closes/ends the database connection.
     */
    public static void closeConnection() {
        try {
            CONNECTION.close(); // closing connection
            System.out.println("Connection Closed...");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
