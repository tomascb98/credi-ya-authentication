package co.com.crediya.model.user.gateways;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository {
    Mono<User> saveUser(User user);
    Mono<User> findUserByEmail(String email);
    Mono<User> findUserById(UUID id);
    Flux<User> getAllUsers();
    Mono<User> updateUser(User user);
    void deleteUser(UUID id);
}
