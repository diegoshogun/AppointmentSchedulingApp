package wgu.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu.utils.JDBC;
import wgu.model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class handles database related functions for contact class.
 */
public class ContactDAO {

    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    private static ObservableList<String> allContactNames = FXCollections.observableArrayList();

    /**
     * Method queries sql table and retrieves every contact.
     * Adds each contact to allContacts observable list.
     *
     * @throws SQLException
     */
    public static void setContacts() throws SQLException {
        allContacts.clear();
        try {
            String sqlStatement = "SELECT * FROM contacts";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");
                allContacts.add(new Contact(contactId, contactName, contactEmail));
                allContactNames.add(contactName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method loops through every contact and returns contact name.
     *
     * @param contactId
     * @return name
     */
    public static String getContactName(int contactId) {
        for (Contact c : allContacts) {
            if (c.getId() == contactId) {
                return c.getName();
            }
        }
        return "";
    }

    /**
     * Method loops through every contact and returns contact id.
     *
     * @param contactName
     * @return id
     */
    public static int getContactId(String contactName) {
        for (Contact c : allContacts) {
            if (c.getName().equals(contactName)) {
                return c.getId();
            }
        }
        return -1;
    }


    /**
     * Returns observable list of all contacts
     *
     * @return allContacts
     */
    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

    /**
     * Returns String observable list of all contacts names
     *
     * @return allContactNames
     */
    public static ObservableList<String> getAllContactNames() {
        return allContactNames;
    }
}
