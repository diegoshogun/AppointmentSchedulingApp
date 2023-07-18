package wgu.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu.utils.JDBC;
import wgu.model.Appointment;
import wgu.model.FirstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class handles the database side for reports.
 */
public class ReportsDAO {

    /**
     * Method loops through every appointment and gets every type.
     */
    public static ObservableList<Appointment> getAllAppointmentTypes() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        ObservableList<String> appointmentsByType = FXCollections.observableArrayList();
        for (Appointment a : AppointmentDAO.getAllAppointments()) {
            if (!appointmentsByType.contains(a.getType())) {
                appointmentsByType.add(a.getType());
                appointments.add(a);
            }
        }
        return appointments;
    }

    /**
     * Method loops through every appointment and gets every month.
     */
    public static ObservableList<Appointment> getAllAppointmentMonths() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        ObservableList<String> months = FXCollections.observableArrayList();

        for (Appointment a : AppointmentDAO.getAllAppointments()) {
            if (!months.contains(a.getStart().getMonth().toString())) {
                months.add(String.valueOf(a.getStart().getMonth()));
                appointments.add(a);
            }
        }

        return appointments;
    }

    /**
     * Method returns list of first level divisions with customers.
     *
     * @return fldWithCustomers
     * @throws SQLException
     */
    public static ObservableList<FirstLevelDivision> getAllFLDWithCustomers() throws SQLException {
        ObservableList<FirstLevelDivision> fldWithCustomers = FXCollections.observableArrayList();
        String sqlStatement = "SELECT DISTINCT FLD.Division" +
                " FROM first_level_divisions FLD" +
                " JOIN customers C ON C.Division_ID = FLD.Division_ID";
        PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            for (FirstLevelDivision fld : FirstLevelDivisionDAO.getAllFirstLevelDivisions()) {
                if (rs.getString("Division").toString().equals(fld.getDivisionName())) {
                    fldWithCustomers.add(fld);
                }
            }
        }
        return fldWithCustomers;
    }

    /**
     * Method returns total number of customers in each division.
     *
     * @param divisionName
     * @return totalCustomers
     * @throws SQLException
     */
    public static int getAllFLDWithCustomersTotal(String divisionName) throws SQLException {
        int totalCustomers = 0;
        String sqlStatement = "SELECT FLD.Division, COUNT(*) AS 'total_customers'" +
                " FROM first_level_divisions FLD" +
                " JOIN customers C ON C.Division_ID = FLD.Division_ID" +
                " WHERE FLD.Division = '" + divisionName + "' GROUP BY FLD.Division";
        PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            totalCustomers = Integer.parseInt(rs.getString("total_customers"));
        }

        return totalCustomers;
    }

}
