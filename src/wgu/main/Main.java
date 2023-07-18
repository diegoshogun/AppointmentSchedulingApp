package wgu.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wgu.dao.*;
import wgu.utils.JDBC;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main class for project. Creates application and loads the MainScreen.
 */
public class Main extends Application {
    /**
     * Start method for class. Loads MainScreen FXML and creates application window.
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        try {
            // setting up stage and scene
            Parent root = FXMLLoader.load(getClass().getResource("/LoginScreen.fxml"));
            Scene scene = new Scene(root);

            // checking to see if system language is set to french
            // then setting application window title to french
            ResourceBundle rb = ResourceBundle.getBundle("login", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr")) { // french
                stage.setTitle(rb.getString("projectTitle"));
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } else { // english
                stage.setTitle("Software II - Scheduling Application");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method sets global time zone in DB to UTC
     *
     * @throws SQLException
     */
    private static void setGlobalTimeZoneUTC() throws SQLException {
        String sqlStatement = "SET GLOBAL TIME_ZONE='+00:00'";
        PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
        int rowsAffected = ps.executeUpdate();
    }

    /**
     * Method calls functions that query DB to retrieve all data needed.
     *
     * @throws SQLException
     */
    private static void queryDB() throws SQLException {
        UserDAO.setUsers(); // gets all users from sql table then adds to allUsers observable list
        CustomerDAO.setCustomers(); // gets all customers from sql table then adds to allCustomers observable list
        CountryDAO.setCountries(); // gets every country from sql table then adds to allCountries observable list
        FirstLevelDivisionDAO.setFirstLevelDivisions(); // gets all first level divisions from sql table then adds to allFirstLevelDivisions observable list
        AppointmentDAO.setAppointments(); // gets all appointments from sql table then adds to all appointments observable list
        ContactDAO.setContacts(); // gets all contacts from sql table then adds to all contacts observable list
    }

    /**
     * Main method launches application and initializes database connection.
     * Also closes database connection when user exits app.
     *
     * @param args main
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        JDBC.openConnection(); // opens connection
        queryDB();
        setGlobalTimeZoneUTC();
        Application.launch(); // launches app

        JDBC.closeConnection(); // ends connection
    }
}
