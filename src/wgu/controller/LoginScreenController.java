package wgu.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import wgu.dao.AppointmentDAO;
import wgu.dao.UserDAO;
import wgu.model.Appointment;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class handles login screen.
 */
public class LoginScreenController implements Initializable {
    // window
    private Stage stage;
    private Parent root;
    private Scene scene;

    // GUI variables
    @FXML
    private Label invalidUserLabel;
    @FXML
    private Label loggedOutLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button loginButton;
    @FXML
    private TextField passwordText;
    @FXML
    private TextField usernameText;
    @FXML
    private Label zoneIdLabel;

    public LoginScreenController() throws IOException {
    }

    /**
     * Activates when user clicks on log in button.
     * Checks if username/password match a created users' username/password
     * Changes from login screen to main overview screen.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void onActionLogin(ActionEvent event) throws IOException {
        try {
            String userName = usernameText.getText();
            String password = passwordText.getText();

            if (UserDAO.authenticateUser(userName, password)) { // if user has been authenticated
                recordLoginActivity(true);
                invalidUserLabel.setOpacity(0);
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("/MainScreen.fxml"));
                scene = new Scene(root);

                stage.setScene(scene);
                stage.show();
                appointmentWithin15Mins();
            } else { // username or password is incorrect
                recordLoginActivity(false);
                invalidUserLabel.setOpacity(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method checks to see if any appointments are starting within 15 minutes.
     * Then alerts user if there are or not.
     *
     * @throws SQLException
     */
    public void appointmentWithin15Mins() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentDAO.getAppointmentsIn15Mins();
        if (appointments.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You have no appointments in 15 minutes.");
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        for (Appointment a : appointments) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment starts within 15 minutes" +
                    "Appointment ID: " + a.getId() + "\nStart Time: " + a.getStart() + "\nEnd Time: " + a.getEnd());
            Optional<ButtonType> result = alert.showAndWait();
        }

    }

    /**
     * Method gets and displays users zone id.
     */
    public void setZoneId() {
        zoneIdLabel.setText("ZoneID: " + String.valueOf(UserDAO.getZoneId()));
    }

    /**
     * Method sets login screen language to French, if computer system language is set to French.
     */
    public void setLanguage() {
        ResourceBundle rb = ResourceBundle.getBundle("login", Locale.getDefault());

        if (Locale.getDefault().getLanguage().equals("fr")) { // if system language is french
            usernameLabel.setTranslateX(-50);
            usernameLabel.setPrefWidth(130);
            usernameLabel.setText(rb.getString("username"));

            passwordLabel.setTranslateX(-30);
            passwordLabel.setPrefWidth(100);
            passwordLabel.setText(rb.getString("password"));

            loginButton.setText(rb.getString("loginButton"));
            loginButton.setPrefWidth(300);
            loginButton.setTranslateX(-50);

            invalidUserLabel.setText(rb.getString("invalidUser"));
            loggedOutLabel.setText(rb.getString("loggedOut"));
            //stage.setTitle(rb.getString("projectTitle"));
        }
    }

    /**
     * Method handles the recording of login activity.
     *
     * @param result
     * @throws IOException
     */
    public void recordLoginActivity(boolean result) throws IOException {
        var now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowFormatted = now.format(formatter);
        String logLine;
        if (result) {
            logLine = nowFormatted + ", LOGIN SUCCESS, username:" + usernameText.getText() + ", password:" + passwordText.getText();
        } else {
            logLine = nowFormatted + ", LOGIN FAILURE, username:" + usernameText.getText() + ", password:" + passwordText.getText();
        }

        var bufWriter = Files.newBufferedWriter(Paths.get("login_activity.txt"),
                StandardCharsets.UTF_8, StandardOpenOption.WRITE,
                StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        var printer = new PrintWriter(bufWriter, true);
        printer.println(logLine);

    }


    /**
     * Method initializes LoginScreenController class.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLanguage();
        setZoneId();
    }
}
