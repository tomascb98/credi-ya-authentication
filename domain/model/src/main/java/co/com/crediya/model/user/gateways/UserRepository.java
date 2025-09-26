package co.com.crediya.model.user.gateways;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface UserRepository {
    Mono<User> saveUser(User user);
    Mono<User> findUserByEmail(String email);
    Mono<Boolean> findUserByDocumentNumber(String documentNumber);
    Flux<User> findUsersByDocumentNumber(Set<String> documentNumbers);
}