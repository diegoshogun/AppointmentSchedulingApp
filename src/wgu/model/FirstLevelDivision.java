package wgu.model;

import wgu.dao.ReportsDAO;

import java.sql.SQLException;

/***
 * Class for First Level Division object.
 *
 */

public class FirstLevelDivision {
    private int divisionId;
    private String divisionName;
    private int countryId;
    private int totalCustomersPerDivision;

    /***
     * Constructor for FirstLevelDivision class.
     *
     */
    public FirstLevelDivision(int divisionId, String divisionName, int countryId) throws SQLException {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
        totalCustomersPerDivision = ReportsDAO.getAllFLDWithCustomersTotal(this.divisionName);
    }

    /**
     * returns division id.
     *
     * @return divisionId
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * sets division id.
     *
     * @param divisionId
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * returns division name.
     *
     * @return divisionName
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * sets division name.
     *
     * @param divisionName
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * returns country id.
     *
     * @return countryId
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * sets country id.
     *
     * @param countryId
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getTotalCustomersPerDivision() {
        return totalCustomersPerDivision;
    }
}
