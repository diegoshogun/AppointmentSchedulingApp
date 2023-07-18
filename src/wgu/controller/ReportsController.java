package wgu.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wgu.dao.AppointmentDAO;
import wgu.dao.ContactDAO;
import wgu.dao.ReportsDAO;
import wgu.dao.UserDAO;
import wgu.model.Appointment;
import wgu.model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Class handles the reports screen of application.
 */

public class ReportsController implements Initializable {
    // stage/window
    private Stage stage;
    private Parent root;
    private Scene scene;

    // Data
    private ObservableList<String> contacts = ContactDAO.getAllContactNames();
    private String selectedContact;
    private int contactId;
    private int countryId;

    @FXML
    private TableColumn<Appointment, Integer> appCustomerIdCol;
    @FXML
    private TableColumn<Appointment, String> appDescriptionCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appEndCol;
    @FXML
    private TableColumn<Appointment, Integer> appContactIdCol;
    @FXML
    private TableColumn<Appointment, Integer> appIdCol;
    @FXML
    private TableColumn<Appointment, String> appLocationCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appStartCol;
    @FXML
    private TableColumn<Appointment, String> appTitleCol;
    @FXML
    private TableColumn<Appointment, String> appTypeCol;
    @FXML
    private Button backButton;
    @FXML
    private TableView<Appointment> contactAppointmentsTable;
    @FXML
    private ComboBox<String> contactCombo;
    @FXML
    private TableView<FirstLevelDivision> totalCustomersTable;
    @FXML
    private TableColumn<FirstLevelDivision, String> divisionCol;
    @FXML
    private TableColumn<FirstLevelDivision, Integer> divisionTotalCustomersCol;
    @FXML
    private Label loggedInLabel;
    @FXML
    private TableColumn<Appointment, String> monthAppointmentsCol;
    @FXML
    private TableView<Appointment> monthAppointmentsTable;
    @FXML
    private TableColumn<Appointment, Integer> monthTotalAppointmentsCol;
    @FXML
    private TableColumn<Appointment, String> typeAppointmentsCol;
    @FXML
    private TableColumn<Appointment, Integer> typeTotalAppointmentsCol;
    @FXML
    private TableView<Appointment> typeAppointmentsTable;
    @FXML
    private Label zoneIdLabel;

    /**
     * Method switches back to the main screen when user clicks back button.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void onActionBackButton(ActionEvent event) throws IOException {
        try {
            // switching back to main screen.
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/MainScreen.fxml"));
            scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets selected contact name and contact id that user selects from combo box.
     *
     * @param event
     */
    @FXML
    public void onActionContactCombo(ActionEvent event) {
        selectedContact = contactCombo.getSelectionModel().getSelectedItem();
        contactId = ContactDAO.getContactId(selectedContact);
        contactAppointmentsTable.setItems(AppointmentDAO.getAppointmentsBasedOnContactId(contactId));
        appIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        appTitleCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        appCustomerIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        appContactIdCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("contactId"));
        appDescriptionCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        appLocationCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        appTypeCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        appStartCol.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("start"));
        appEndCol.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("end"));
    }

    /**
     * Method initializes class.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoneIdLabel.setText("ZoneID: " + String.valueOf(UserDAO.getZoneId()));
        loggedInLabel.setText("User: " + String.valueOf(UserDAO.getCurrentUserName()));

        // contacts
        contactCombo.setItems(contacts);

        // appointments by type
        typeAppointmentsTable.setItems(ReportsDAO.getAllAppointmentTypes());
        typeAppointmentsCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        typeTotalAppointmentsCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("totalAppointmentsByType"));

        // appointments by month
        monthAppointmentsTable.setItems(ReportsDAO.getAllAppointmentMonths());
        monthAppointmentsCol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("month"));
        monthTotalAppointmentsCol.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("totalAppointmentsByMonth"));

        // total customers by country/division
        try {
            totalCustomersTable.setItems(ReportsDAO.getAllFLDWithCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        divisionCol.setCellValueFactory(new PropertyValueFactory<FirstLevelDivision, String>("divisionName"));
        divisionTotalCustomersCol.setCellValueFactory(new PropertyValueFactory<FirstLevelDivision, Integer>("totalCustomersPerDivision"));

    }
}
