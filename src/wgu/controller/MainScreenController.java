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
import wgu.dao.CustomerDAO;
import wgu.dao.UserDAO;
import wgu.model.Appointment;
import wgu.model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class handles the main screen of application.
 */

public class MainScreenController implements Initializable {

    private boolean appointmentWarningShown = false;

    // window
    private Stage stage;
    private Parent root;
    private Scene scene;

    // GUI variables
    @FXML
    private ToggleGroup appointmentsRadioBtns;
    @FXML
    private RadioButton allAppointmentsRadioBtn;
    @FXML
    private RadioButton currentMonthRadioBtn;
    @FXML
    private RadioButton currentWeekRadioBtn;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button reportsButton;
    @FXML
    private TableColumn<Appointment, Integer> appContactIdCol;
    @FXML
    private TableColumn<Appointment, Integer> appCustomerIdCol;
    @FXML
    private TableColumn<Appointment, String> appDescriptionCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appEndCol;
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
    private TableView<Appointment> appointmentsTableView;
    @FXML
    private TableColumn<Customer, String> customerAddressCol;
    @FXML
    private TableColumn<Customer, Integer> customerDivisionIdCol;
    @FXML
    private TableColumn<Customer, Integer> customerIdCol;
    @FXML
    private TableColumn<Customer, String> customerNameCol;
    @FXML
    private TableColumn<Customer, String> customerPhoneCol;
    @FXML
    private TableColumn<Customer, String> customerPostalCol;
    @FXML
    private TableView<Customer> customersTableView;
    @FXML
    private Button deleteAppointmentButton;
    @FXML
    private Button deleteCustomerButton;
    @FXML
    private Label loggedInLabel;
    @FXML
    private Label customerAppointmentDeletedLabel;
    @FXML
    private Label deleteCustomerErrorLabel;
    @FXML
    private Label customerHasAppointmentLabel;
    @FXML
    private Label updateCustomerErrorLabel;
    @FXML
    private Label updateAppointmentErrorLabel;
    @FXML
    private Label scheduleAppointmentErrorLabel;
    @FXML
    private Label appointmentNotSelectedLabel;
    @FXML
    private Button updateAppointmentButton;
    @FXML
    private Button updateCustomerButton;
    @FXML
    private Label zoneIdLabel;


    /***
     * Method switches to AddCustomer window when button is clicked.
     *
     * @throws IOException
     */
    @FXML
    public void onActionAddCustomer(ActionEvent event) throws IOException {
        setLabelsInvisible();
        try {
            // switching to add customer screen
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/AddCustomer.fxml"));
            scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method switches to UpdateCustomerController screen.
     */
    @FXML
    public void onActionUpdateCustomer(ActionEvent event) throws IOException {
        setLabelsInvisible();
        try {
            // switching to add customer screen
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/UpdateCustomer.fxml"));
            fxmlLoader.load();

            UpdateCustomerController updateCustomerController = fxmlLoader.getController();
            updateCustomerController.selectedCustomerToModify(customersTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            root = fxmlLoader.getRoot();

            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (NullPointerException e) {
            updateCustomerErrorLabel.setOpacity(1);
        }
    }

    /**
     * Method deletes customer user selected
     *
     * @param event - deletes customer
     * @throws SQLException
     */
    @FXML
    public void onActionDeleteCustomer(ActionEvent event) throws SQLException {
        Customer selectedCustomerToDelete = customersTableView.getSelectionModel().getSelectedItem();
        boolean stillHasAppointment;
        setLabelsInvisible();

        if (selectedCustomerToDelete != null) {
            deleteCustomerErrorLabel.setOpacity(0);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?" +
                    " Do you wish to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (!AppointmentDAO.customerHasAppointment(selectedCustomerToDelete)) {
                    CustomerDAO.deleteCustomer(selectedCustomerToDelete.getId());
                    CustomerDAO.setCustomers();
                    customersTableView.refresh();
                    customerAppointmentDeletedLabel.setText("Customer successfully deleted! (ID: " + selectedCustomerToDelete.getId() + ")");
                    customerAppointmentDeletedLabel.setOpacity(1);
                } else {
                    customerHasAppointmentLabel.setOpacity(1);
                }
            }
        } else {
            customerAppointmentDeletedLabel.setOpacity(0);
            deleteCustomerErrorLabel.setOpacity(1);
            customerAppointmentDeletedLabel.setOpacity(0);

        }
    }

    /***
     * Method switches to AddAppointment window when customer is selected and add appointment button is clicked.
     *
     * @throws IOException
     */
    @FXML
    public void onActionAddAppointment(ActionEvent event) throws IOException {
        setLabelsInvisible();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/AddAppointment.fxml"));
            fxmlLoader.load();

            AddAppointmentController addAppointmentController = fxmlLoader.getController();
            addAppointmentController.selectedCustomerAddAppointment(customersTableView.getSelectionModel().getSelectedItem());

            // switching to add appointment screen
            scheduleAppointmentErrorLabel.setOpacity(0);
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            root = fxmlLoader.getRoot();

            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (NullPointerException e) {
            scheduleAppointmentErrorLabel.setOpacity(1);
        }
    }

    /**
     * Method switches to UpdateAppointmentController screen.
     */
    @FXML
    public void onActionUpdateAppointment(ActionEvent event) throws IOException {
        setLabelsInvisible();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/UpdateAppointment.fxml"));
            fxmlLoader.load();

            UpdateAppointmentController updateAppointmentController = fxmlLoader.getController();
            updateAppointmentController.selectedAppointmentToModify(appointmentsTableView.getSelectionModel().getSelectedItem());

            // switching to update appointment screen
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            root = fxmlLoader.getRoot();

            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (NullPointerException e) {
            updateAppointmentErrorLabel.setOpacity(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method deletes appointment selected by user
     *
     * @param event - deletes customer
     * @throws SQLException
     */
    @FXML
    public void onActionDeleteAppointment(ActionEvent event) throws SQLException {
        setLabelsInvisible();
        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            appointmentNotSelectedLabel.setOpacity(0);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this appointment?" +
                    " Do you wish to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                AppointmentDAO.deleteAppointment(selectedAppointment.getId());
                AppointmentDAO.setAppointments();
                appointmentsTableView.refresh();
                customerAppointmentDeletedLabel.setText("Appointment successfully deleted! " +
                        "(ID: " + selectedAppointment.getId() + ", Type: " + selectedAppointment.getType() + ")");
                customerAppointmentDeletedLabel.setOpacity(1);

            }
        } else {
            setLabelsInvisible();
            appointmentNotSelectedLabel.setOpacity(1);
        }
    }

    /**
     * Method changes to reports screen when user clicks on reports button.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void onActionReports(ActionEvent event) throws IOException {
        setLabelsInvisible();
        try {
            // switching to reports screen.
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/Reports.fxml"));
            scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method handles the radio buttons to view appointments.
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    public void onActionRadioButton(ActionEvent event) throws SQLException {
        if (currentWeekRadioBtn.isSelected()) {
            appointmentsTableView.setItems(AppointmentDAO.getAllAppointmentsThisWeek());
        } else if (currentMonthRadioBtn.isSelected()) {
            appointmentsTableView.setItems(AppointmentDAO.getAllAppointmentsThisMonth());
        } else {
            appointmentsTableView.setItems(AppointmentDAO.getAllAppointments());
        }
    }

    /**
     * Sets every label that notifies the user of something to 0 opacity.
     */
    public void setLabelsInvisible() {
        customerAppointmentDeletedLabel.setOpacity(0);
        deleteCustomerErrorLabel.setOpacity(0);
        updateCustomerErrorLabel.setOpacity(0);
        updateAppointmentErrorLabel.setOpacity(0);
        appointmentNotSelectedLabel.setOpacity(0);
        scheduleAppointmentErrorLabel.setOpacity(0);
        customerHasAppointmentLabel.setOpacity(0);
    }

    /**
     * Method checks to see if any appointments are starting within 15 minutes.
     * Then alerts user if there are.
     *
     * @throws SQLException
     */
    public void appointmentWithin15Mins() throws SQLException {
        ObservableList<Appointment> appointments = AppointmentDAO.getAppointmentsIn15Mins();
        if (appointments.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You have no appointments in 15 minutes.");
            Optional<ButtonType> result = alert.showAndWait();
            appointmentWarningShown = true;
            return;
        }

        for (Appointment a : appointments) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Appointment starts within 15 minutes" +
                    "Appointment ID: " + a.getId() + "\nStart Time: " + a.getStart() + "\nEnd Time: " + a.getEnd());
            Optional<ButtonType> result = alert.showAndWait();
            appointmentWarningShown = true;
        }

    }

    /**
     * Method initializes MainScreenController class and sets up table views with data.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoneIdLabel.setText("ZoneID: " + String.valueOf(UserDAO.getZoneId()));
        loggedInLabel.setText("User: " + String.valueOf(UserDAO.getCurrentUserName()));
        /*
        try {
            if(!appointmentWarningShown) {
                appointmentWithin15Mins();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
         */
        // customers table view
        customersTableView.setItems(CustomerDAO.getAllCustomers());

        customerIdCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        customerPostalCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));
        customerDivisionIdCol.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("divisionId"));

        // appointments table view
        appointmentsTableView.setItems(AppointmentDAO.getAllAppointments());
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
}
