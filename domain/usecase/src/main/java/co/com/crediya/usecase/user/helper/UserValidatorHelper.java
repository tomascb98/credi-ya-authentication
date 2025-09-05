package co.com.crediya.usecase.user.helper;

import co.com.crediya.model.exceptions.BusinessRuleException;
import co.com.crediya.model.exceptions.ValidationException;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class UserValidatorHelper {

    public static Mono<User> validateAndSaveUser(User user, UserRepository repository) {
        return validateRequiredFields(user)
            .flatMap(validUser -> validateEmailUniqueness(validUser, repository))
            .flatMap(validUser -> validateBusinessRules(validUser))
            .flatMap(validUser -> repository.saveUser(validUser));
    }

    private static Mono<User> validateRequiredFields(User user) {
        return Mono.fromCallable(() -> {
            if (user.getName() == null || user.getName().trim().isEmpty() ||
                user.getLastName() == null || user.getLastName().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty() ||
                user.getSalaryBase() == null) {
                throw new ValidationException("Los campos nombres, apellidos, correo_electronico y salario_base son obligatorios.");
            }
            return user;
        });
    }

    private static Mono<User> validateEmailUniqueness(User user, UserRepository repository) {
        return repository.findUserByEmail(user.getEmail())
            .hasElement()
            .flatMap(exists -> {
                if (exists) {
                    return Mono.error(new BusinessRuleException("El correo electrónico ya está registrado."));
                }
                return Mono.just(user);
            });
    }

    private static Mono<User> validateBusinessRules(User user) {
        return Mono.fromCallable(() -> {
            String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
            if (!user.getEmail().matches(emailRegex)) {
                throw new ValidationException("El formato del correo electrónico no es válido.");
            }
            
            if (user.getSalaryBase().compareTo(BigDecimal.ZERO) < 0 || 
                user.getSalaryBase().compareTo(BigDecimal.valueOf(15000000)) > 0) {
                throw new ValidationException("El salario base debe estar entre 0 y 15,000,000.");
            }
            
            return user;
        });
    }
}