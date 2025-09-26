package co.com.crediya.usecase.user.helper;

import co.com.crediya.model.exceptions.BusinessRuleException;
import co.com.crediya.model.exceptions.ValidationException;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public class UserValidatorHelper {

    public static Mono<User> validateUser(User user, UserRepository repository) {
        return validateRequiredFields(user)
            .flatMap(validUser -> validateEmailUniqueness(validUser, repository))
            .flatMap(validUser -> validateBusinessRules(validUser));
    }

    public static Mono<String> validateStringField(String field, String fieldName){
        if(field == null || field.trim().isEmpty()){
            return Mono.error(new ValidationException("El campo : " + fieldName + "no puede estar vácio."));
        }
        return Mono.just(field);
    }

    private static Mono<User> validateRequiredFields(User user) {
        return validateStringField(user.getName(), "nombre")
                .flatMap(name -> validateStringField(user.getLastName(), "apellido"))
                .flatMap(lastName -> validateStringField(user.getEmail(), "correo electrónico"))
                .flatMap(email -> validateStringField(user.getPassword(), "contraseña"))
                .flatMap(password -> validateSalaryBase(user.getSalaryBase()))
                .map(salary -> user);
    }

    // Método adicional para BigDecimal
    private static Mono<BigDecimal> validateSalaryBase(BigDecimal salary) {
        if (salary == null) {
            return Mono.error(new ValidationException("El campo salario base no puede estar vacío."));
        }
        return Mono.just(salary);
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