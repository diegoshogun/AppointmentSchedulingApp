package wgu.dao;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu.utils.JDBC;
import wgu.model.Country;
import wgu.model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database related class for Country.
 */
public class CountryDAO {

    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private static ObservableList<String> allCountryNames = FXCollections.observableArrayList();

    /**
     * Method gets and sets all countries from sql table.
     *
     * @throws SQLException
     */
    public static void setCountries() throws SQLException {
        try {
            String sqlStatement = "SELECT * FROM Countries";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                allCountryNames.add(countryName);

                allCountries.add(new Country(countryId, countryName));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method returns country name using customer id.
     *
     * @param customerId
     * @return countryName
     */
    public static String getCountryBasedOnCustomerId(int customerId) {
        for (Customer c : CustomerDAO.getAllCustomers()) {
            if (c.getId() == customerId) {
                int countryId = FirstLevelDivisionDAO.getCountryId(c.getDivisionId());
                return getCountryName(countryId);
            }
        }
        return "";
    }

    /**
     * Loops through every country and returns country name using country id.
     *
     * @param countryId
     * @return countryName
     */
    public static String getCountryName(int countryId) {
        for (Country c : getAllCountries()) {
            if (c.getId() == countryId) {
                return c.getName();
            }
        }
        return "";
    }

    /**
     * returns observable list of all countries.
     *
     * @return allCountries
     */
    public static ObservableList<Country> getAllCountries() {
        return allCountries;
    }

    /**
     * returns observable list of all country names.
     *
     * @return allCountryNames
     */
    public static ObservableList<String> getAllCountryNames() {
        return allCountryNames;
    }

}
