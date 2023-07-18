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
import wgu.dao.ContactDAO;
import wgu.dao.UserDAO;
import wgu.model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class handles the updating of an existing appointment.
 */
public class UpdateAppointmentController implements Initializable {
    // window & stage
    private Stage stage;
    private Parent root;
    private Scene scene;

    // Data
    private int contactId;
    private ObservableList<String> contacts = ContactDAO.getAllContactNames();
    private String contactSelected;

    // GUI
    @FXML
    private Label appointmentESTHoursLabel;

    @FXML
    private TextField appointmentIdText;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> contactComboBox;

    @FXML
    private Label currentUserLabel;

    @FXML
    private TextField customerIdText;

    @FXML
    private TextField descriptionText;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<String> endTimeCombo;

    @FXML
    private TextField locationText;

    @FXML
    private Label nullErrorLabel;

    @FXML
    private Label overlapAppointmentLabel;
    @FXML
    private Label appointmentDateOrTimeErrorLabel;
    @FXML
    private Button saveButton;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<String> startTimeCombo;

    @FXML
    private TextField titleText;

    @FXML
    private TextField typeText;

    @FXML
    private TextField userIdText;

    @FXML
    private Label zoneIdLabel;

    /**
     * Method saves all new values and updates the selected appointment.
     *
     * @param event
     */
    @FXML
    public void onActionSave(ActionEvent event) throws IOException {
        setLabelsInvisible();
        try {
            //Appointment(int id, String title, int userId, int customerId, int contactId,
            //String description, String location, String type, LocalDateTime start, LocalDateTime end)
            int id = Integer.parseInt(appointmentIdText.getText());
            String title = titleText.getText();
            int userId = Integer.parseInt(userIdText.getText());
            int customerId = Integer.parseInt(customerIdText.getText());
            //contactId = contactId;
            String description = descriptionText.getText();
            String location = locationText.getText();
            String type = typeText.getText();

            //DateTime variables
            LocalDate startDate = startDatePicker.getValue();
            LocalTime startTime = LocalTime.parse(startTimeCombo.getSelectionModel().getSelectedItem());

            LocalDate endDate = endDatePicker.getValue();
            LocalTime endTime = LocalTime.parse(endTimeCombo.getSelectionModel().getSelectedItem());

            // validation checks ////////////////////////////////////////////////////////////

            // if fields are empty
            if (title.isBlank() || description.isBlank() || location.isBlank() || type.isBlank()
                    || startDate == null || endDate == null || startTime == null || endTime == null) {
                nullErrorLabel.setOpacity(1);
                return;
            }

            // creating start and end date time variables
            LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
            LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

            // if appointment start date/time is after appointment end date/time
            if (startDateTime.isAfter(endDateTime) || endDateTime.isBefore(startDateTime) || !startDate.equals(endDate)
                    || startDate.isBefore(LocalDate.now()) || endDate.isBefore(LocalDate.now())
                    || endDateTime.isEqual(startDateTime)) {
                appointmentDateOrTimeErrorLabel.setOpacity(1);
                return;
            }

            // if appointment overlaps with another
            if (AppointmentDAO.appointmentOverlap(startDateTime, endDateTime, id, customerId)) {
                overlapAppointmentLabel.setOpacity(1);
                return;
            }
            // if appointment is outside of business hours
            if (AppointmentDAO.appointmentOutsideBusinessHours(startDateTime, endDateTime)) {
                appointmentESTHoursLabel.setOpacity(1);
                return;
            }

            // if new appointment passes validation checks
            setLabelsInvisible();
            AppointmentDAO.updateAppointment(new Appointment(id, title, userId, customerId, contactId,
                    description, location, type, startDateTime, endDateTime));
            AppointmentDAO.setAppointments();

            // returning to main screen
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/MainScreen.fxml"));
            scene = new Scene(root);
            stage.setScene(scene);
        } catch (NullPointerException | SQLException | NumberFormatException e) {
            nullErrorLabel.setOpacity(1);
        }
    }

    /**
     * Method activates when user clicks on cancel button.
     * Asks user if they wish to cancel.
     * Discards any information entered and returns to the main screen.
     *
     * @param event
     */
    @FXML
    public void onActionCancel(ActionEvent event) throws IOException {
        setLabelsInvisible();
        // confirmation alert ensures user wishes to cancel and return to main screen
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all entered values, " +
                "do you wish to continue?");
        Optional<ButtonType> result = alert.showAndWait();

        // if user does wish to cancel and return to main screen
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // returning to main screen
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("/MainScreen.fxml"));
            scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * When contact combo box is selected.
     *
     * @param event
     */
    @FXML
    public void onActionContactCombo(ActionEvent event) {
        contactSelected = contactComboBox.getSelectionModel().getSelectedItem();
        contactId = ContactDAO.getContactId(contactSelected);
    }

    /**
     * Sets every label that notifies the user of something to 0 opacity.
     */
    public void setLabelsInvisible() {
        overlapAppointmentLabel.setOpacity(0);
        appointmentESTHoursLabel.setOpacity(0);
        appointmentDateOrTimeErrorLabel.setOpacity(0);
        nullErrorLabel.setOpacity(0);
    }


    /**
     * This method fills the text fields with selected original appointment data to update from the MainScreen.
     *
     * @param selectedAppointment
     */
    public void selectedAppointmentToModify(Appointment selectedAppointment) {
        appointmentIdText.setText(String.valueOf(selectedAppointment.getId()));
        customerIdText.setText(String.valueOf(selectedAppointment.getCustomerId()));
        userIdText.setText(String.valueOf(selectedAppointment.getUserId()));
        titleText.setText(selectedAppointment.getTitle());
        descriptionText.setText(selectedAppointment.getDescription());
        locationText.setText(selectedAppointment.getLocation());
        contactComboBox.setValue(ContactDAO.getContactName(selectedAppointment.getContactId()));
        contactSelected = ContactDAO.getContactName(selectedAppointment.getContactId());
        contactId = ContactDAO.getContactId(contactSelected);
        typeText.setText(selectedAppointment.getType());
        startDatePicker.setValue(selectedAppointment.getStart().toLocalDate());
        endDatePicker.setValue(selectedAppointment.getEnd().toLocalDate());
        startTimeCombo.setValue(selectedAppointment.getStart().toLocalTime().toString());
        endTimeCombo.setValue(selectedAppointment.getEnd().toLocalTime().toString());
    }

    /**
     * Method fills combo boxes with needed data.
     */
    public void fillComboBoxes() {
        contactComboBox.setItems(contacts);
        contactComboBox.setVisibleRowCount(5);

        startTimeCombo.setItems(AppointmentDAO.getAppointmentTimes());
        startTimeCombo.setVisibleRowCount(8);

        endTimeCombo.setItems(AppointmentDAO.getAppointmentTimes());
        endTimeCombo.setVisibleRowCount(8);
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
        currentUserLabel.setText("User: " + String.valueOf(UserDAO.getCurrentUserName()));
        fillComboBoxes();
    }
}
