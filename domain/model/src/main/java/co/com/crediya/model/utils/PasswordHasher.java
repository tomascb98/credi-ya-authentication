package co.com.crediya.model.utils;

import reactor.core.publisher.Mono;

public interface PasswordHasher {
    Boolean matches (String password, String hashPassword);
    Mono<String> hash (String password);
}
