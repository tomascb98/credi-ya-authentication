package co.com.crediya.model.user.gateways;

import co.com.crediya.model.user.User;

import java.util.List;

public interface UserRepository {
    User saveUser(User user);
    User findUserById(Long id);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(Long id);
}
