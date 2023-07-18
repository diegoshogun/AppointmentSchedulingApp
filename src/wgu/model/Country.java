package wgu.model;

import java.sql.SQLException;

/***
 * Class for country object.
 *
 */

public class Country {
    private int id; // country id
    private String name; // country name

    /**
     * Country class constructor.
     */
    public Country(int id, String name) throws SQLException {
        this.id = id;
        this.name = name;
    }

    /**
     * returns country id.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * sets country id.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns country name.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * sets country name.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
