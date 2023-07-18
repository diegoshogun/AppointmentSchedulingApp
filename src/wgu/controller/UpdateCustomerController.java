package wgu.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import wgu.dao.CountryDAO;
import wgu.dao.CustomerDAO;
import wgu.dao.FirstLevelDivisionDAO;
import wgu.dao.UserDAO;
import wgu.model.Customer;
import wgu.model.FirstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class handles the updating/modifying of customers.
 */
public class UpdateCustomerController implements Initializable {
    // window & stage
    private Stage stage;
    private Parent root;
    private Scene scene;

    // customer variables
    private int countryId;
    private int newDivisionId;

    // GUI variables
    @FXML
    private TextField addressText;
    @FXML
    private Button cancelButton;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private Label currentUserLabel;
    @FXML
    private TextField idText;
    @FXML
    private TextField nameText;
    @FXML
    private Label nullErrorLabel;
    @FXML
    private TextField phoneText;
    @FXML
    private TextField postalCodeText;
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<String> stateComboBox;
    @FXML
    private Label zoneIdLabel;

    /**
     * Saves all new values and updates existing customer.
     *
     * @param event
     */
    @FXML
    public void onActionSave(ActionEvent event) throws IOException {
        try {
            // public Customer(int id, String name, String address, String postalCode, String phoneNumber, int divisionId)
            int index = 0;
            int id = Integer.parseInt(idText.getText());
            String name = nameText.getText();
            String address = addressText.getText();
            String postalCode = postalCodeText.getText();
            String phoneNumber = phoneText.getText();
            if (name.isBlank() || address.isBlank() || postalCode.isBlank() || phoneNumber.isBlank()
                    || countryComboBox.getSelectionModel().getSelectedItem().isEmpty()
                    || stateComboBox.getSelectionModel().getSelectedItem().isEmpty()) {
                nullErrorLabel.setOpacity(1);
            } else {
                nullErrorLabel.setOpacity(0);
                CustomerDAO.updateCustomer(new Customer(id, name, address, postalCode, phoneNumber, newDivisionId));
                CustomerDAO.setCustomers();
                FirstLevelDivisionDAO.setFirstLevelDivisions();

                // returning to main screen
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("/MainScreen.fxml"));
                scene = new Scene(root);

                stage.setScene(scene);
            }

        } catch (NullPointerException e) {
            nullErrorLabel.setOpacity(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
     * Method activates when user selects a specific division.
     */
    @FXML
    public void onActionStateCombo() {
        String divisionSelected = stateComboBox.getSelectionModel().getSelectedItem();
        for (FirstLevelDivision fld : FirstLevelDivisionDAO.getAllFirstLevelDivisions()) {
            if (fld.getDivisionName().equals(divisionSelected)) {
                newDivisionId = fld.getDivisionId();
            }
        }
    }

    /**
     * Method activates when user selects a country.
     * Then populates the state combo box with correct divisions.
     *
     * @param event
     */
    @FXML
    public void onActionCountryCombo(ActionEvent event) {
        String countryName = countryComboBox.getSelectionModel().getSelectedItem();
        stateComboBox.setVisibleRowCount(5);
        stateComboBox.valueProperty().set(null);

        switch (countryName) {
            case ("U.S") -> {
                countryId = 1;
                stateComboBox.setItems(FirstLevelDivisionDAO.usDivisionNames);
            }
            case ("UK") -> {
                countryId = 2;
                stateComboBox.setItems(FirstLevelDivisionDAO.ukDivisionNames);
            }
            case ("Canada") -> {
                countryId = 3;
                stateComboBox.setItems(FirstLevelDivisionDAO.canadaDivisionNames);
            }
        }
    }


    /**
     * This method fills the text fields with selected original customer data to update from the MainScreen.
     *
     * @param selectedCustomer
     */
    public void selectedCustomerToModify(Customer selectedCustomer) {
        // public Customer(int id, String name, String address, String postalCode, String phoneNumber, int divisionId)
        idText.setText(String.valueOf(selectedCustomer.getId()));
        nameText.setText(selectedCustomer.getName());
        addressText.setText(selectedCustomer.getAddress());
        postalCodeText.setText(selectedCustomer.getPostalCode());
        phoneText.setText(selectedCustomer.getPhoneNumber());

        // filling country and state combo boxes accordingly
        stateComboBox.setValue(FirstLevelDivisionDAO.getDivisionIdName(selectedCustomer.getDivisionId()));
        stateComboBox.setVisibleRowCount(5);
        countryId = FirstLevelDivisionDAO.getCountryId(selectedCustomer.getDivisionId());
        newDivisionId = selectedCustomer.getDivisionId();
        switch (countryId) {
            case 1 -> {
                countryComboBox.setValue("U.S");
                stateComboBox.setItems(FirstLevelDivisionDAO.usDivisionNames);
            }
            case 2 -> {
                countryComboBox.setValue("UK");
                stateComboBox.setItems(FirstLevelDivisionDAO.ukDivisionNames);
            }
            case 3 -> {
                countryComboBox.setValue("Canada");
                stateComboBox.setItems(FirstLevelDivisionDAO.canadaDivisionNames);
            }
        }

    }

    /**
     * Fills Country combo box with all values.
     */
    public void fillCountryCombo() {
        try {
            countryComboBox.setItems(CountryDAO.getAllCountryNames());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method initializes UpdateCustomerController class.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zoneIdLabel.setText("ZoneID: " + String.valueOf(UserDAO.getZoneId()));
        currentUserLabel.setText("User: " + String.valueOf(UserDAO.getCurrentUserName()));
        fillCountryCombo();
    }
}
