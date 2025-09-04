package co.com.crediya.usecase.user.helper;

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
class UserValidatorHelperTest {

    @Mock
    private UserRepository userRepository;

    private User validUser;
    private User userWithEmptyFields;
    private User userWithInvalidEmail;
    private User userWithInvalidSalary;
    private User existingUser;

    @BeforeEach
    void setUp() {
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
    @DisplayName("Debería validar y guardar usuario exitosamente")
    void shouldValidateAndSaveUserSuccessfully() {
        // Arrange
        when(userRepository.findUserByEmail(validUser.getEmail()))
            .thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class)))
            .thenReturn(Mono.just(validUser));

        // Act
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(validUser, userRepository);

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
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(userWithEmptyFields, userRepository);

        // Assert
        StepVerifier.create(result)
            .expectError(ValidationException.class)
            .verify();
    }

    @Test
    @DisplayName("Debería fallar cuando el nombre está vacío")
    void shouldFailWhenNameIsEmpty() {
        // Arrange
        User userWithEmptyName = validUser.toBuilder()
            .name("")
            .build();

        // Act
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(userWithEmptyName, userRepository);

        // Assert
        StepVerifier.create(result)
            .expectError(ValidationException.class)
            .verify();
    }

    @Test
    @DisplayName("Debería fallar cuando el apellido está vacío")
    void shouldFailWhenLastNameIsEmpty() {
        // Arrange
        User userWithEmptyLastName = validUser.toBuilder()
            .lastName("")
            .build();

        // Act
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(userWithEmptyLastName, userRepository);

        // Assert
        StepVerifier.create(result)
            .expectError(ValidationException.class)
            .verify();
    }

    @Test
    @DisplayName("Debería fallar cuando el email está vacío")
    void shouldFailWhenEmailIsEmpty() {
        // Arrange
        User userWithEmptyEmail = validUser.toBuilder()
            .email("")
            .build();

        // Act
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(userWithEmptyEmail, userRepository);

        // Assert
        StepVerifier.create(result)
            .expectError(ValidationException.class)
            .verify();
    }

    @Test
    @DisplayName("Debería fallar cuando el salario base es null")
    void shouldFailWhenSalaryBaseIsNull() {
        // Arrange
        User userWithNullSalary = validUser.toBuilder()
            .salaryBase(null)
            .build();

        // Act
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(userWithNullSalary, userRepository);

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
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(validUser, userRepository);

        // Assert
        StepVerifier.create(result)
            .expectError(BusinessRuleException.class)
            .verify();

        verify(userRepository).findUserByEmail(validUser.getEmail());
    }

    @Test
    @DisplayName("Debería fallar cuando el formato del email no es válido")
    void shouldFailWhenEmailFormatIsInvalid() {
        // Arrange
        when(userRepository.findUserByEmail(userWithInvalidEmail.getEmail()))
            .thenReturn(Mono.empty());

        // Act
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(userWithInvalidEmail, userRepository);

        // Assert
        StepVerifier.create(result)
            .expectError(ValidationException.class)
            .verify();
    }

    @Test
    @DisplayName("Debería fallar cuando el salario base es negativo")
    void shouldFailWhenSalaryBaseIsNegative() {
        // Arrange
        User userWithNegativeSalary = validUser.toBuilder()
            .salaryBase(BigDecimal.valueOf(-1000000))
            .build();

        when(userRepository.findUserByEmail(userWithNegativeSalary.getEmail()))
            .thenReturn(Mono.empty());

        // Act
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(userWithNegativeSalary, userRepository);

        // Assert
        StepVerifier.create(result)
            .expectError(ValidationException.class)
            .verify();
    }

    @Test
    @DisplayName("Debería fallar cuando el salario base excede el límite superior")
    void shouldFailWhenSalaryBaseExceedsUpperLimit() {
        // Arrange
        when(userRepository.findUserByEmail(userWithInvalidSalary.getEmail()))
            .thenReturn(Mono.empty());

        // Act
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(userWithInvalidSalary, userRepository);

        // Assert
        StepVerifier.create(result)
            .expectError(ValidationException.class)
            .verify();
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
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(userWithMinSalary, userRepository);

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
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(userWithMaxSalary, userRepository);

        // Assert
        StepVerifier.create(result)
            .expectNext(userWithMaxSalary)
            .verifyComplete();
    }

    @Test
    @DisplayName("Debería validar email con formato válido")
    void shouldValidateEmailWithValidFormat() {
        // Arrange
        User userWithValidEmail = validUser.toBuilder()
            .email("test.email+tag@domain.co.uk")
            .build();

        when(userRepository.findUserByEmail(userWithValidEmail.getEmail()))
            .thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class)))
            .thenReturn(Mono.just(userWithValidEmail));

        // Act
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(userWithValidEmail, userRepository);

        // Assert
        StepVerifier.create(result)
            .expectNext(userWithValidEmail)
            .verifyComplete();
    }

    @Test
    @DisplayName("Debería manejar campos con espacios en blanco")
    void shouldHandleFieldsWithWhitespace() {
        // Arrange
        User userWithWhitespace = validUser.toBuilder()
            .name("  Juan  ")
            .lastName("  Pérez  ")
            .email("juan.perez@example.com") // Email sin espacios para que pase la validación
            .build();

        // El código llama al repositorio con el email original
        when(userRepository.findUserByEmail(userWithWhitespace.getEmail()))
            .thenReturn(Mono.empty());
        when(userRepository.saveUser(any(User.class)))
            .thenReturn(Mono.just(userWithWhitespace));

        // Act
        Mono<User> result = UserValidatorHelper.validateAndSaveUser(userWithWhitespace, userRepository);

        // Assert
        StepVerifier.create(result)
            .expectNext(userWithWhitespace)
            .verifyComplete();
    }
}
