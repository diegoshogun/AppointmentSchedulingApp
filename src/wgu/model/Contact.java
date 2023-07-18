package wgu.model;

/**
 * Contact class for contact object.
 */
public class Contact {
    private int id;
    private String name;
    private String email;

    /**
     * Contact class constructor.
     */
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * returns contact id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * sets contact id.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns contact name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * sets contact name.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns contact email.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets contact email.
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
