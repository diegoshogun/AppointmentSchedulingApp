package wgu.model;

import wgu.dao.AppointmentDAO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Class for Appointment object.
 */
public class Appointment {
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    private int id;
    private String title;
    private int userId;
    private int customerId;
    private int contactId;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int totalAppointmentsByType;
    private int totalAppointmentsByMonth;
    private String month;

    /**
     * Constructor for appointment class.
     */
    public Appointment(int id, String title, int userId, int customerId, int contactId,
                       String description, String location, String type, LocalDateTime start, LocalDateTime end) throws SQLException {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.customerId = customerId;
        this.contactId = contactId;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        totalAppointmentsByType = AppointmentDAO.getTotalAppointmentsBasedOnType(this);
        totalAppointmentsByMonth = AppointmentDAO.getTotalAppointmentsBasedOnMonth(this);
        month = String.valueOf(this.start.getMonth());

    }

    /**
     * returns appointment id.
     *
     * @return id;
     */
    public int getId() {
        return id;
    }

    /**
     * sets appointment id.
     *
     * @param id;
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns appointment title.
     *
     * @return title;
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets appointment title.
     *
     * @param title;
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * returns appointment user id.
     *
     * @return userId;
     */
    public int getUserId() {
        return userId;
    }

    /**
     * sets appointment user id.
     *
     * @param userId;
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * returns appointment customer id.
     *
     * @return customerId;
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * sets appointment customer id.
     *
     * @param customerId;
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * returns appointment contact id.
     *
     * @return contactId;
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * sets appointment contact id.
     *
     * @param contactId;
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * returns appointment description.
     *
     * @return description;
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets appointment description.
     *
     * @param description;
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * returns appointment location.
     *
     * @return location;
     */
    public String getLocation() {
        return location;
    }

    /**
     * sets appointment location.
     *
     * @param location;
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * returns appointment type.
     *
     * @return type;
     */
    public String getType() {
        return type;
    }

    /**
     * sets appointment type.
     *
     * @param type;
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * returns appointment start time.
     *
     * @return start;
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * sets appointment start time.
     *
     * @param start;
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * returns appointment end time.
     *
     * @return end;
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * sets appointment end time.
     *
     * @param end;
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * returns totalAppointmentsByType.
     *
     * @return totalAppointmentsByType
     */
    public int getTotalAppointmentsByType() {
        return totalAppointmentsByType;
    }

    /**
     * returns totalAppointmentsByMonth.
     *
     * @return totalAppointmentsByMonth
     */
    public int getTotalAppointmentsByMonth() {
        return totalAppointmentsByMonth;
    }

    /**
     * returns appointment month.
     *
     * @return month
     */
    public String getMonth() {
        return month;
    }
}
