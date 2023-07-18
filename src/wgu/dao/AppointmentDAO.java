package wgu.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu.utils.JDBC;
import wgu.model.Appointment;
import wgu.model.Customer;
import wgu.utils.TimeUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;

/**
 * This class handles the database side of the appointment class.
 */
public class AppointmentDAO {

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<String> appointmentTimes = FXCollections.observableArrayList();

    /**
     * Method queries sql table then adds new appointment
     *
     * @param newAppointment
     * @throws SQLException
     */
    public static int insertNewAppointment(Appointment newAppointment) throws SQLException {
        int rowsAffected = 0;
        try {
            String sqlStatement =
                    "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, " +
                            "Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ps.setInt(1, newAppointment.getId());
            ps.setString(2, newAppointment.getTitle());
            ps.setString(3, newAppointment.getDescription());
            ps.setString(4, newAppointment.getLocation());
            ps.setString(5, newAppointment.getType());
            ps.setTimestamp(6, Timestamp.valueOf(newAppointment.getStart()));
            ps.setTimestamp(7, Timestamp.valueOf(newAppointment.getEnd()));
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, "admin");
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(11, "admin");
            ps.setInt(12, newAppointment.getCustomerId());
            ps.setInt(13, newAppointment.getUserId());
            ps.setInt(14, newAppointment.getContactId());

            rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error adding new appointment....");
            System.out.println(e.getErrorCode());
            e.printStackTrace();
        }
        return rowsAffected;
    }

    /**
     * Method queries sql table then updates appointment that user selected.
     *
     * @param appointment
     * @throws SQLException
     */
    public static int updateAppointment(Appointment appointment) throws SQLException {
        int rowsAffect = 0;
        try {
            String sqlStatement = "UPDATE appointments SET Customer_ID = ?, User_ID = ?, Title = ?, " +
                    "Description = ?, Location = ?, Contact_ID = ?, Type = ?, Start = ?, END = ?, " +
                    "Last_Update = ?, Last_Updated_By = ? WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ps.setInt(1, appointment.getCustomerId());
            ps.setInt(2, appointment.getUserId());
            ps.setString(3, appointment.getTitle());
            ps.setString(4, appointment.getDescription());
            ps.setString(5, appointment.getLocation());
            ps.setInt(6, appointment.getContactId());
            ps.setString(7, appointment.getType());
            ps.setTimestamp(8, Timestamp.valueOf(appointment.getStart()));
            ps.setTimestamp(9, Timestamp.valueOf(appointment.getEnd()));
            ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(11, UserDAO.getCurrentUserName());
            ps.setInt(12, appointment.getId());
            int rowsAffected = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("error updating appointment....");
            System.out.println(e.getMessage());
        }

        return rowsAffect;
    }

    /**
     * Method queries sql table then deletes appointment that the user selected.
     *
     * @param appointmentId
     * @throws SQLException
     */
    public static int deleteAppointment(int appointmentId) throws SQLException {
        int rowsAffected = 0;
        try {
            String sqlStatement = "DELETE FROM appointments WHERE Appointment_ID=?";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ps.setInt(1, appointmentId);
            rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting appointment....");
            System.out.println(e.getErrorCode());
        }
        return rowsAffected;
    }

    /**
     * Method gets and sets all default appointments from sql table.
     *
     * @throws SQLException
     */
    public static void setAppointments() throws SQLException {
        setAppointmentTimes();
        allAppointments.clear();
        try {
            String sqlStatement = "SELECT * FROM appointments";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");

                allAppointments.add(new Appointment(id, title, userId, customerId, contactId, description,
                        location, type, start, end));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method creates string observable list of appointment times in 15 min increments.
     */
    public static void setAppointmentTimes() {
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j += 15) {
                if (i < 10) {
                    if (j < 15) {
                        appointmentTimes.add("0" + i + ":" + j + "0");
                    } else {
                        appointmentTimes.add("0" + i + ":" + j);
                    }
                } else {
                    if (j < 15) {
                        appointmentTimes.add(i + ":" + j + "0");
                    } else {
                        appointmentTimes.add(i + ":" + j);
                    }
                }
            }
        }
    }

    /**
     * Method loops through every appointment and checks to see if selected customer still has an appointment or not.
     *
     * @param customer
     * @return true
     */
    public static boolean customerHasAppointment(Customer customer) {
        for (Appointment a : allAppointments) {
            if (a.getCustomerId() == customer.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method checks to see if new appointment will overlap with existing appointments.
     *
     * @return true
     */
    public static boolean appointmentOverlap(LocalDateTime startDate, LocalDateTime endDate, int appointmentId, int customerId) throws SQLException {
        LocalDateTime startDateUTC = TimeUtil.convertTimeDateUTC(startDate);
        LocalDateTime endDateUTC = TimeUtil.convertTimeDateUTC(endDate);
        // a: 8:00 - 10:00
        // b: 9:00 - 9:30
        String sqlStatement = "SELECT * FROM appointments WHERE Customer_ID = " + customerId + " AND '" + startDateUTC + "' >= Start " +
                "AND '" + startDateUTC + "' <= End " +
                "OR Customer_ID = " + customerId + " AND '" + endDateUTC + "' >= Start AND '" + endDateUTC + "' <= End";
        PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);

        int overlappingAppointments = 0;
        ResultSet rs = ps.executeQuery();
        ObservableList<Integer> appointmentIds = FXCollections.observableArrayList();
        while (rs.next()) {
            overlappingAppointments++;
            appointmentIds.add(rs.getInt("Appointment_ID"));
        }
        //System.out.println(overlappingAppointments);

        return overlappingAppointments > 0 & !appointmentIds.contains(appointmentId);
    }

    /**
     * Method checks to see if new appointment aligns with business hours in EST.
     * hours 8:00 a.m. to 10:00 p.m. EST
     *
     * @return true
     */
    public static boolean appointmentOutsideBusinessHours(LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime startDateEST = TimeUtil.convertTimeDateEST(startDate);
        LocalDateTime endDateEST = TimeUtil.convertTimeDateEST(endDate);
        LocalTime startTimeEST = startDateEST.toLocalTime();
        LocalTime endTimeEST = endDateEST.toLocalTime();

        return startTimeEST.isBefore(TimeUtil.getBusinessHoursStart()) || startTimeEST.isAfter(TimeUtil.getBusinessHoursEnd())
                || endTimeEST.isBefore(TimeUtil.getBusinessHoursStart()) || endTimeEST.isAfter(TimeUtil.getBusinessHoursEnd());
    }

    /**
     * Method alters the auto increment of the appointment id.
     * This is so that the new auto-generated appointment id is the value of the max appointment id + 1.
     *
     * @throws SQLException
     */
    public static void alterAutoIncrementAppointmentId() throws SQLException {
        try {
            String sqlStatement = "ALTER TABLE appointments AUTO_INCREMENT = ?";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ps.setInt(1, Collections.max(getAllAppointmentIds(), null));
            int rowsAffected = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error altering appointment auto increment....");
            e.printStackTrace();
        }
    }

    /**
     * Method loops through every appointment and returns list of appointments that have the same contact id.
     * Contains lambda #1 that replaces for loop to loop through every appointment.
     * Then checks to see if contact ids match using if statement.
     *
     * @param contactId
     * @return appointments
     */
    public static ObservableList<Appointment> getAppointmentsBasedOnContactId(int contactId) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        // lambda #1
        getAllAppointments().forEach(a -> {
            if (a.getContactId() == contactId) {
                appointments.add(a);
            }
        });

        /*
        original (not using lambda)
        for (Appointment a : getAllAppointments()) {
            if (a.getContactId() == contactId) {
                appointments.add(a);
            }
        }
         */

        return appointments;
    }

    /**
     * Method queries sql table then retrieves the total amount of appointments per type.
     *
     * @param appointment
     * @return totalAppointments
     * @throws SQLException
     */
    public static int getTotalAppointmentsBasedOnType(Appointment appointment) throws SQLException {
        int totalAppointments = 0;
        String sqlStatement = "SELECT * FROM appointments WHERE Type = ?";
        PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
        ps.setString(1, appointment.getType());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            totalAppointments++;
        }
        return totalAppointments;
    }

    /**
     * Method queries sql table then retrieves the total amount of appointments per month.
     *
     * @param appointment
     * @return totalMonths
     * @throws SQLException
     */
    public static int getTotalAppointmentsBasedOnMonth(Appointment appointment) throws SQLException {
        int totalMonths = 0;
        String sqlStatement = "SELECT * FROM appointments WHERE MONTH(Start) = ?";
        PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
        ps.setInt(1, appointment.getStart().getMonth().getValue());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            totalMonths++;
        }
        return totalMonths;
    }

    public static ObservableList<Appointment> getAppointmentsIn15Mins() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        // time zones
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime userTimeZone = ZonedDateTime.now(UserDAO.getZoneId());
        ZonedDateTime nowUTC = userTimeZone.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime nowPlus15UTC = nowUTC.plusMinutes(15);

        // sql statement
        String sqlStatement = "SELECT * FROM appointments WHERE Start BETWEEN '" + nowUTC + "' AND '" + nowPlus15UTC + "'";
        PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            for (Appointment a : getAllAppointments()) {
                if (rs.getInt("Appointment_ID") == a.getId()) {
                    appointments.add(a);
                }
            }
        }
        return appointments;
    }


    /**
     * Method returns observable list of all appointment ids.
     * Contains lambda #2 that replaces for loop to get every appointment.
     * Then adds each appointment id to list of all appointment ids.
     *
     * @return allAppointmentIds
     */
    public static ObservableList<Integer> getAllAppointmentIds() {
        ObservableList<Integer> allAppointmentIds = FXCollections.observableArrayList();

        // lambda #2
        getAllAppointments().forEach(a -> allAppointmentIds.add(a.getId()));

        /*
        original (not using lambda)
        for (Appointment a : getAllAppointments()) {
            allAppointmentIds.add(a.getId());
        }

         */
        return allAppointmentIds;
    }

    public static ObservableList<String> getAppointmentTimes() {
        return appointmentTimes;
    }

    /**
     * Returns observable list of all appointments
     *
     * @return allAppointments
     */
    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    /**
     * Method returns list of all appointments from this current week.
     *
     * @return appointments
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointmentsThisWeek() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        // LocalDateTime this monday set to 00:00
        LocalDateTime thisWeeksMonday = LocalDateTime.now().with(DayOfWeek.MONDAY).with(LocalTime.MIN);
        LocalDateTime thisWeeksSunday = LocalDateTime.now().with(DayOfWeek.SUNDAY);
        // LocalDateTime next monday set to 00:00
        LocalDateTime nextMonday = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).with(LocalTime.MIN);

        // looping though every appointment between this monday and next monday at 00:00 so
        // all of this sunday is included.
        String sqlStatement = "SELECT * FROM appointments WHERE Start BETWEEN '" + thisWeeksMonday +
                "' AND '" + nextMonday + "'";
        PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            for (Appointment a : getAllAppointments()) {
                if (rs.getInt("Appointment_ID") == a.getId()) {
                    appointments.add(a);
                }
            }
        }
        return appointments;
    }

    /**
     * Method returns list of all appointments from this current month.
     *
     * @return appointments
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointmentsThisMonth() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        LocalDate initial = LocalDate.now();
        LocalDateTime thisMonthStartDay = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime thisMonthEndDay = LocalDateTime.now().withDayOfMonth(initial.getMonth().length(initial.isLeapYear()));
        String sqlStatement = "SELECT * FROM appointments WHERE Start BETWEEN '" + thisMonthStartDay +
                "' AND '" + thisMonthEndDay + "'";
        PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            for (Appointment a : getAllAppointments()) {
                if (rs.getInt("Appointment_ID") == a.getId()) {
                    appointments.add(a);
                }
            }
        }
        return appointments;
    }
}
