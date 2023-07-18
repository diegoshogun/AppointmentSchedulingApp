package wgu.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu.utils.JDBC;
import wgu.model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database related class for FirstLevelDivision.
 */
public class FirstLevelDivisionDAO {

    private static ObservableList<FirstLevelDivision> allFirstLevelDivisions = FXCollections.observableArrayList();
    private static ObservableList<String> allFirstLevelDivisionNames = FXCollections.observableArrayList();
    public static ObservableList<String> usDivisionNames = FXCollections.observableArrayList();
    public static ObservableList<String> ukDivisionNames = FXCollections.observableArrayList();
    public static ObservableList<String> canadaDivisionNames = FXCollections.observableArrayList();


    /**
     * Method gets and sets all first level divisions from sql table.
     *
     * @throws SQLException
     */
    public static void setFirstLevelDivisions() throws SQLException {
        allFirstLevelDivisions.clear();
        try {
            String sqlStatement = "SELECT * FROM first_level_divisions";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs.getInt("Country_ID");

                switch (countryId) {
                    case 1:
                        usDivisionNames.add(divisionName);
                        break;
                    case 2:
                        ukDivisionNames.add(divisionName);
                        break;
                    case 3:
                        canadaDivisionNames.add(divisionName);
                        break;
                }

                allFirstLevelDivisions.add(new FirstLevelDivision(divisionId, divisionName, countryId));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method returns name of division that corresponds to division id
     *
     * @param divisionId
     * @return divisionName
     */
    public static String getDivisionIdName(int divisionId) {
        for (FirstLevelDivision fld : allFirstLevelDivisions) {
            if (fld.getDivisionId() == divisionId) {
                return fld.getDivisionName();
            }
        }
        return "oof no such division id exists!";
    }

    /**
     * Method returns country id that corresponds to first level division
     *
     * @param divisionId
     * @return countryId
     */
    public static int getCountryId(int divisionId) {
        for (FirstLevelDivision fld : allFirstLevelDivisions) {
            if (fld.getDivisionId() == divisionId) {
                return fld.getCountryId();
            }
        }
        return -1;
    }

    /**
     * returns observable list of all first level divisions.
     *
     * @return allFirstLevelDivisions
     */
    public static ObservableList<FirstLevelDivision> getAllFirstLevelDivisions() {
        return allFirstLevelDivisions;
    }

    /**
     * returns observable list of all first level division names.
     *
     * @return allFirstLevelDivisionNames
     */
    public static ObservableList<String> getAllFirstLevelDivisionNames() {
        return allFirstLevelDivisionNames;
    }
}
