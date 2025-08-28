package co.com.crediya.usecase.user.helper;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;

import java.awt.dnd.InvalidDnDOperationException;
import java.math.BigDecimal;

public class UserValidatorHelper{

    public static void UserFieldsValidation(User user) throws IllegalStateException{
        if(user.getEmail() == null || user.getEmail().trim().isEmpty()
        || user.getName() == null || user.getName().trim().isEmpty()
        || user.getSalaryBase() == null){
            throw new IllegalStateException("El nombre, email y/o salario base está vacio.");
        }
    }

    public static void BusinessRulesUserValidation(User user, UserRepository repository) throws IllegalStateException{
        Mono<User> userAlreadyExists = repository.findUserByEmail(user.getEmail());
        if(userAlreadyExists != null) throw new InvalidDnDOperationException("El email ya existe en el sistema.");

        String emailRegex = "^(?!\\.)(?!.*\\.\\.)[a-zA-Z0-9_%+-]+(\\.[a-zA-Z0-9_%+-]+)*@([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
        if(!user.getEmail().trim().matches(emailRegex)) throw new IllegalArgumentException("El campo email no tiene un formato valido.");

        if(user.getSalaryBase().compareTo(BigDecimal.ZERO) < 0 || user.getSalaryBase().compareTo(BigDecimal.valueOf(1500000.0)) > 0) throw new IllegalStateException("El salario base no puede ser negativo o superar 1500000");
    }
}
