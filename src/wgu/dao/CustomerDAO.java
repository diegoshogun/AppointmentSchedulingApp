package wgu.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu.utils.JDBC;
import wgu.model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * Database related class for Customer.
 */
public class CustomerDAO {

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    /**
     * Method gets and sets all customers
     *
     * @throws SQLException
     */
    public static void setCustomers() throws SQLException {
        allCustomers.clear();
        try {
            String sqlStatement = "SELECT * FROM Customers";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String customerAddress = rs.getString("Address");
                String customerPostalCode = rs.getString("Postal_Code");
                String customerPhoneNumber = rs.getString("Phone");
                int customerDivisionId = rs.getInt("Division_ID");

                allCustomers.add(new Customer(customerId, customerName, customerAddress, customerPostalCode, customerPhoneNumber, customerDivisionId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method queries sql table then adds new customer
     *
     * @param newCustomer
     * @throws SQLException
     */
    public static int insertNewCustomer(Customer newCustomer) throws SQLException {
        int rowsAffected = 0;
        try {
            String sqlStatement = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ps.setInt(1, newCustomer.getId());
            ps.setString(2, newCustomer.getName());
            ps.setString(3, newCustomer.getAddress());
            ps.setString(4, newCustomer.getPostalCode());
            ps.setString(5, newCustomer.getPhoneNumber());
            ps.setInt(6, newCustomer.getDivisionId());
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(8, UserDAO.getCurrentUserName());
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(10, UserDAO.getCurrentUserName());

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error adding new customer....");
            System.out.println(e.getErrorCode());
        }
        return rowsAffected;
    }


    /**
     * Method queries sql table then updates customer that user selected.
     *
     * @param customer
     * @throws SQLException
     */
    public static int updateCustomer(Customer customer) throws SQLException {
        int rowsAffected = 0;
        try {
            String sqlStatement = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?," +
                    "Division_ID=?, Last_Update = ?, Last_Updated_By = ? WHERE Customer_ID=?";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhoneNumber());
            ps.setInt(5, customer.getDivisionId());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, UserDAO.getCurrentUserName());
            ps.setInt(8, customer.getId());
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("error updating customer....");
            System.out.println(e.getMessage());
        }
        return rowsAffected;
    }

    /**
     * Method queries sql table then deletes customer that the user selected.
     *
     * @param customerId
     * @throws SQLException
     */
    public static int deleteCustomer(int customerId) throws SQLException {
        int rowsAffected = 0;
        try {
            String sqlStatement = "DELETE FROM customers WHERE Customer_ID=?";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ps.setInt(1, customerId);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting customer....");
            System.out.println(e.getErrorCode());
        }
        return rowsAffected;
    }

    /**
     * Method alters the auto increment of the customer id.
     * This is so that the new auto-generated customer id is the value of the max customer id + 1.
     *
     * @throws SQLException
     */
    public static void alterAutoIncrementCustomerId() throws SQLException {
        try {
            String sqlStatement = "ALTER TABLE customers AUTO_INCREMENT = ?";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ps.setInt(1, Collections.max(getAllCustomerIds(), null));
            int rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error altering customer auto increment....");
            e.printStackTrace();
        }
    }

    /**
     * Method returns observable list of all customer ids.
     *
     * @return allCustomerIds
     */
    public static ObservableList<Integer> getAllCustomerIds() {
        ObservableList<Integer> allCustomerIds = FXCollections.observableArrayList();
        for (Customer c : getAllCustomers()) {
            allCustomerIds.add(c.getId());
        }
        return allCustomerIds;
    }

    /**
     * returns observable list of all customers.
     *
     * @return allCustomers
     */
    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }
}
