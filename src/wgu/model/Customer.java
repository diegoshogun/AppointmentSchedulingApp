package wgu.model;

/**
 * Class for Customer Object.
 */
public class Customer {
    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int divisionId;

    /**
     * Constructor for Customer.
     */
    public Customer(int id, String name, String address, String postalCode, String phoneNumber, int divisionId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.divisionId = divisionId;
    }

    /**
     * returns customer id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * sets customer id.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns customer name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * sets customer name.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns customer address.
     *
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * sets customer address.
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * returns customer postal code.
     *
     * @return postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * sets customer postal code.
     *
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * returns customer phone number.
     *
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * sets customer phone number.
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * returns customer division id.
     *
     * @return divisionId
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * sets customer division id.
     *
     * @param divisionId
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}
