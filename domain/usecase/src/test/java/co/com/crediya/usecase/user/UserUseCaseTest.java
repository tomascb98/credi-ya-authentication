package co.com.crediya.usecase.user;

import co.com.crediya.model.exceptions.BusinessRuleException;
import co.com.crediya.model.exceptions.ValidationException;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private UserUseCase userUseCase;

    private User validUser;
    private User userWithEmptyFields;
    private User userWithInvalidEmail;
    private User userWithInvalidSalary;
    private User existingUser;

    @BeforeEach
    void setUp() {
        userUseCase = new UserUseCase(userRepository);

        Role role = Role.builder()
            .id(1)
            .name("USER")
            .description("Rol de usuario")
            .build();

        validUser = User.builder()
            .id(UUID.randomUUID())
            .name("Juan")
            .lastName("Pérez")
            .birthDate(LocalDate.of(1990, 1, 1))
            .address("Calle 123 #45-67")
            .phoneNumber("3001234567")
            .email("juan.perez@example.com")
            .salaryBase(BigDecimal.valueOf(2500000))
            .password("password123")
            .documentNumber("12345678")
            .role(role)
            .build();

        userWithEmptyFields = User.builder()
            .id(UUID.randomUUID())
            .name("")
            .lastName("")
            .email("")
            .salaryBase(null)
            .build();

        userWithInvalidEmail = User.builder()
            .id(UUID.randomUUID())
            .name("Juan")
            .lastName("Pérez")
            .email("invalid-email")
            .salaryBase(BigDecimal.valueOf(2500000))
            .build();

        userWithInvalidSalary = User.builder()
            .id(UUID.randomUUID())
            .name("Juan")
            .lastName("Pérez")
            .email("juan.perez@example.com")
            .salaryBase(BigDecimal.valueOf(20000000))
            .build();

        existingUser = User.builder()
            .id(UUID.randomUUID())
            .name("María")
            .lastName("García")
            .email("juan.perez@example.com")
            .salaryBase(BigDecimal.valueOf(3000000))
            .build();
    }

    @Test
    @DisplayName("Debería guardar usuario exitosamente cuando todos los datos son válidos")
    void shouldSaveUserSuccessfully() {
        // Arrange
        when(userRepository.findUserByEmail(validUser.getEmail()))
            .thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class)))
            .thenReturn(Mono.just(validUser));

        // Act
        Mono<User> result = userUseCase.saveUser(validUser);

        // Assert
        StepVerifier.create(result)
            .expectNext(validUser)
            .verifyComplete();

        verify(userRepository).findUserByEmail(validUser.getEmail());
        verify(userRepository).saveUser(any(User.class));
    }

    @Test
    @DisplayName("Debería fallar cuando faltan campos requeridos")
    void shouldFailWhenRequiredFieldsAreMissing() {
        // Act
        Mono<User> result = userUseCase.saveUser(userWithEmptyFields);

        // Assert
        StepVerifier.create(result)
            .expectError(ValidationException.class)
            .verify();
    }

    @Test
    @DisplayName("Debería fallar cuando el email ya está registrado")
    void shouldFailWhenEmailAlreadyExists() {
        // Arrange
        when(userRepository.findUserByEmail(validUser.getEmail()))
            .thenReturn(Mono.just(existingUser));

        // Act
        Mono<User> result = userUseCase.saveUser(validUser);

        // Assert
        StepVerifier.create(result)
            .expectError(BusinessRuleException.class)
            .verify();

        verify(userRepository).findUserByEmail(validUser.getEmail());
    }

    @Test
    @DisplayName("Debería fallar cuando el formato del email no es válido")
    void shouldFailWhenEmailFormatIsInvalid() {
        // Act
        Mono<User> result = userUseCase.saveUser(userWithInvalidEmail);

        // Assert
        StepVerifier.create(result)
            .expectError(ValidationException.class)
            .verify();
    }

    @Test
    @DisplayName("Debería fallar cuando el salario base está fuera del rango válido")
    void shouldFailWhenSalaryBaseIsOutOfRange() {
        // Act
        Mono<User> result = userUseCase.saveUser(userWithInvalidSalary);

        // Assert
        StepVerifier.create(result)
            .expectError(ValidationException.class)
            .verify();
    }

    @Test
    @DisplayName("Debería validar usuario exitosamente cuando existe")
    void shouldValidateUserSuccessfullyWhenExists() {
        // Arrange
        String documentNumber = "12345678";
        when(userRepository.findUserByDocumentNumber(documentNumber))
            .thenReturn(Mono.just(validUser));

        // Act
        Mono<Boolean> result = userUseCase.validateUser(documentNumber);

        // Assert
        StepVerifier.create(result)
            .expectNext(true)
            .verifyComplete();

        verify(userRepository).findUserByDocumentNumber(documentNumber);
    }

    @Test
    @DisplayName("Debería validar usuario como falso cuando no existe")
    void shouldValidateUserAsFalseWhenNotExists() {
        // Arrange
        String documentNumber = "87654321";
        when(userRepository.findUserByDocumentNumber(documentNumber))
            .thenReturn(Mono.empty());

        // Act
        Mono<Boolean> result = userUseCase.validateUser(documentNumber);

        // Assert
        StepVerifier.create(result)
            .expectNext(false)
            .verifyComplete();

        verify(userRepository).findUserByDocumentNumber(documentNumber);
    }

    @Test
    @DisplayName("Debería manejar salario base en el límite inferior")
    void shouldHandleSalaryBaseAtLowerLimit() {
        // Arrange
        User userWithMinSalary = validUser.toBuilder()
            .salaryBase(BigDecimal.ZERO)
            .build();

        when(userRepository.findUserByEmail(userWithMinSalary.getEmail()))
            .thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class)))
            .thenReturn(Mono.just(userWithMinSalary));

        // Act
        Mono<User> result = userUseCase.saveUser(userWithMinSalary);

        // Assert
        StepVerifier.create(result)
            .expectNext(userWithMinSalary)
            .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar salario base en el límite superior")
    void shouldHandleSalaryBaseAtUpperLimit() {
        // Arrange
        User userWithMaxSalary = validUser.toBuilder()
            .salaryBase(BigDecimal.valueOf(15000000))
            .build();

        when(userRepository.findUserByEmail(userWithMaxSalary.getEmail()))
            .thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class)))
            .thenReturn(Mono.just(userWithMaxSalary));

        // Act
        Mono<User> result = userUseCase.saveUser(userWithMaxSalary);

        // Assert
        StepVerifier.create(result)
            .expectNext(userWithMaxSalary)
            .verifyComplete();
    }
}
