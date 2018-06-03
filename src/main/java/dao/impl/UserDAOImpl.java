package dao.impl;

import dao.UserDAO;
import jdbc.JDBC;
import model.Role;
import model.User;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class);
    
    private static final String GET_ALL_USERS_WITHOUT_ADMIN_QUERY = "SELECT * FROM users WHERE role <> 'admin'";
    private static final String CHECK_USER_QUERY = "SELECT * FROM Users WHERE login = ? AND password = ?";

    @Override
    public List<User> getAllUsersWithoutAdmin() {
        List<User> users = new ArrayList<>();
        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(GET_ALL_USERS_WITHOUT_ADMIN_QUERY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(createUserFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return users;
    }

    @Override
    public User getUser(String login, String password) {
        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(CHECK_USER_QUERY)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createUserFromResultSet(resultSet);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }


    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getInt("idUser"),
                resultSet.getString("login"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
    }

}
