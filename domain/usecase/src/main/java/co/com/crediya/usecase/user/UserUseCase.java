package co.com.crediya.usecase.user;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.usecase.user.helper.UserValidatorHelper;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class UserUseCase{
    private final UserRepository userRepository;

    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> saveUser(User user) {
        return UserValidatorHelper.validateAndSaveUser(user, userRepository);
    }
}