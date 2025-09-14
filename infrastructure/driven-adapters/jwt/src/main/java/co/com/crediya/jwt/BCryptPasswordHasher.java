package co.com.crediya.jwt;

import co.com.crediya.model.utils.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BCryptPasswordHasher implements PasswordHasher {
    
    private final BCryptPasswordEncoder passwordEncoder;
    
    public BCryptPasswordHasher() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    @Override
    public Boolean matches(String password, String hashPassword) {
        return passwordEncoder.matches(password, hashPassword);
    }
    
    @Override
    public Mono<String> hash(String password) {
        return Mono.fromCallable(() -> passwordEncoder.encode(password));
    }
}
