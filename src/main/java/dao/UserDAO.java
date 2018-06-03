package dao;

import model.User;

import java.util.List;

public interface UserDAO {

    List<User> getAllUsersWithoutAdmin();

    User getUser(String login, String password);
}
