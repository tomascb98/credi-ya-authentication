package co.com.crediya.model.user.gateways;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository {
    Mono<User> saveUser(User user);
    Mono<User> findUserByEmail(String email);
    Mono<User> findUserByDocumentNumber(String documentNumber);
}