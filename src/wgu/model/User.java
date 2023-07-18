package wgu.model;

/**
 * Class for User object.
 */
public class User {
    private int userId;
    private String userName;
    private String password;

    /**
     * Constructor for User.
     */
    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Returns user id.
     *
     * @return userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Returns user name.
     *
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets username.
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets user password.
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
