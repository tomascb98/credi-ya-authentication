package co.com.crediya.usecase.user;


import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.usecase.user.helper.UserValidatorHelper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class UserUseCase{
    private final UserRepository userRepository;

    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> saveUser(User user) {
        UserValidatorHelper.UserFieldsValidation(user);
        UserValidatorHelper.BusinessRulesUserValidation(user, userRepository);
        return userRepository.saveUser(user);
    }

    public Mono<User> findUserById(UUID id) {
        return userRepository.findUserById(id);
    }

    public Flux<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public Mono<User>  updateUser(User user) {
        return userRepository.updateUser(user);
    }

    public void deleteUser(UUID id) {
        userRepository.deleteUser(id);
    }
}
