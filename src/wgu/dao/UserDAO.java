package wgu.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu.utils.JDBC;
import wgu.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;


/**
 * Database related class for User.
 */
public class UserDAO extends User {

    private static ObservableList<User> allUsers = FXCollections.observableArrayList();
    private static int currentUserId;
    private static String currentUserName;

    /**
     * UserDAO constructor.
     *
     * @param userId
     * @param userName
     * @param password
     */
    public UserDAO(int userId, String userName, String password) {
        super(userId, userName, password);
    }


    /**
     * Method authenticates user username and password
     *
     * @throws SQLException
     */
    public static boolean authenticateUser(String userName, String password) throws SQLException {
        try {
            String sqlStatement = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ps.setString(1, userName);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("User found!");
                currentUserId = rs.getInt("User_ID");
                currentUserName = rs.getString("User_Name");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method runs query statement to get all users.
     * It then adds them to allUsers list.
     *
     * @throws SQLException
     */
    public static void setUsers() throws SQLException {
        try {
            String sqlStatement = "SELECT * FROM users"; // selects all users from user table
            PreparedStatement ps = JDBC.CONNECTION.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {  // loops through all users in table then adds it to allUsers list
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                User newUser = new User(userId, userName, password);
                allUsers.add(newUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns observable list of all users
     *
     * @return allUsers
     */
    public static ObservableList<User> getAllUsers() {
        return allUsers;
    }

    /**
     * Returns current user id.
     *
     * @return currentUserId
     */
    public static int getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Returns current user name.
     *
     * @return currentUserName
     */
    public static String getCurrentUserName() {
        return currentUserName;
    }

    /**
     * Returns users system default zone id
     *
     * @return ZoneId
     */
    public static ZoneId getZoneId() {
        return ZoneId.systemDefault();
    }

}
