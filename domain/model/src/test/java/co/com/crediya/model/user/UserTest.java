package co.com.crediya.model.user;

import co.com.crediya.model.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private UUID testId;
    private String testName;
    private String testLastName;
    private LocalDate testBirthDate;
    private String testAddress;
    private String testPhoneNumber;
    private String testEmail;
    private BigDecimal testSalaryBase;
    private String testPassword;
    private String testDocumentNumber;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testName = "Juan";
        testLastName = "Pérez";
        testBirthDate = LocalDate.of(1990, 1, 1);
        testAddress = "Calle 123 #45-67";
        testPhoneNumber = "3001234567";
        testEmail = "juan.perez@example.com";
        testSalaryBase = BigDecimal.valueOf(2500000);
        testPassword = "password123";
        testDocumentNumber = "12345678";
        testRole = Role.builder()
            .id(1)
            .name("USER")
            .description("Rol de usuario")
            .build();
    }

    @Test
    @DisplayName("Debería crear User con builder")
    void shouldCreateUserWithBuilder() {
        // Act
        User user = User.builder()
            .id(testId)
            .name(testName)
            .lastName(testLastName)
            .birthDate(testBirthDate)
            .address(testAddress)
            .phoneNumber(testPhoneNumber)
            .email(testEmail)
            .salaryBase(testSalaryBase)
            .password(testPassword)
            .documentNumber(testDocumentNumber)
            .role(testRole)
            .build();

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(testId);
        assertThat(user.getName()).isEqualTo(testName);
        assertThat(user.getLastName()).isEqualTo(testLastName);
        assertThat(user.getBirthDate()).isEqualTo(testBirthDate);
        assertThat(user.getAddress()).isEqualTo(testAddress);
        assertThat(user.getPhoneNumber()).isEqualTo(testPhoneNumber);
        assertThat(user.getEmail()).isEqualTo(testEmail);
        assertThat(user.getSalaryBase()).isEqualTo(testSalaryBase);
        assertThat(user.getPassword()).isEqualTo(testPassword);
        assertThat(user.getDocumentNumber()).isEqualTo(testDocumentNumber);
        assertThat(user.getRole()).isEqualTo(testRole);
    }

    @Test
    @DisplayName("Debería crear User con constructor por defecto")
    void shouldCreateUserWithDefaultConstructor() {
        // Act
        User user = new User();

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isNull();
        assertThat(user.getLastName()).isNull();
        assertThat(user.getBirthDate()).isNull();
        assertThat(user.getAddress()).isNull();
        assertThat(user.getPhoneNumber()).isNull();
        assertThat(user.getEmail()).isNull();
        assertThat(user.getSalaryBase()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getDocumentNumber()).isNull();
        assertThat(user.getRole()).isNull();
    }

    @Test
    @DisplayName("Debería crear User con constructor con parámetros")
    void shouldCreateUserWithParameterizedConstructor() {
        // Act
        User user = new User(testId, testName, testLastName, testBirthDate, testAddress,
            testPhoneNumber, testEmail, testSalaryBase, testPassword, testDocumentNumber, testRole);

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(testId);
        assertThat(user.getName()).isEqualTo(testName);
        assertThat(user.getLastName()).isEqualTo(testLastName);
        assertThat(user.getBirthDate()).isEqualTo(testBirthDate);
        assertThat(user.getAddress()).isEqualTo(testAddress);
        assertThat(user.getPhoneNumber()).isEqualTo(testPhoneNumber);
        assertThat(user.getEmail()).isEqualTo(testEmail);
        assertThat(user.getSalaryBase()).isEqualTo(testSalaryBase);
        assertThat(user.getPassword()).isEqualTo(testPassword);
        assertThat(user.getDocumentNumber()).isEqualTo(testDocumentNumber);
        assertThat(user.getRole()).isEqualTo(testRole);
    }

    @Test
    @DisplayName("Debería usar toBuilder correctamente")
    void shouldUseToBuilderCorrectly() {
        // Arrange
        User original = User.builder()
            .id(testId)
            .name(testName)
            .lastName(testLastName)
            .birthDate(testBirthDate)
            .address(testAddress)
            .phoneNumber(testPhoneNumber)
            .email(testEmail)
            .salaryBase(testSalaryBase)
            .password(testPassword)
            .documentNumber(testDocumentNumber)
            .role(testRole)
            .build();

        // Act
        User copy = original.toBuilder()
            .name("María")
            .lastName("García")
            .email("maria.garcia@example.com")
            .build();

        // Assert
        assertThat(copy).isNotSameAs(original);
        assertThat(copy.getId()).isEqualTo(original.getId());
        assertThat(copy.getName()).isEqualTo("María");
        assertThat(copy.getLastName()).isEqualTo("García");
        assertThat(copy.getEmail()).isEqualTo("maria.garcia@example.com");
        assertThat(copy.getBirthDate()).isEqualTo(original.getBirthDate());
        assertThat(copy.getAddress()).isEqualTo(original.getAddress());
        assertThat(copy.getPhoneNumber()).isEqualTo(original.getPhoneNumber());
        assertThat(copy.getSalaryBase()).isEqualTo(original.getSalaryBase());
        assertThat(copy.getPassword()).isEqualTo(original.getPassword());
        assertThat(copy.getDocumentNumber()).isEqualTo(original.getDocumentNumber());
        assertThat(copy.getRole()).isEqualTo(original.getRole());
    }

    @Test
    @DisplayName("Debería manejar valores null correctamente")
    void shouldHandleNullValuesCorrectly() {
        // Act
        User user = User.builder()
            .id(null)
            .name(null)
            .lastName(null)
            .birthDate(null)
            .address(null)
            .phoneNumber(null)
            .email(null)
            .salaryBase(null)
            .password(null)
            .documentNumber(null)
            .role(null)
            .build();

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isNull();
        assertThat(user.getLastName()).isNull();
        assertThat(user.getBirthDate()).isNull();
        assertThat(user.getAddress()).isNull();
        assertThat(user.getPhoneNumber()).isNull();
        assertThat(user.getEmail()).isNull();
        assertThat(user.getSalaryBase()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getDocumentNumber()).isNull();
        assertThat(user.getRole()).isNull();
    }

    @Test
    @DisplayName("Debería ser igual a otra instancia con los mismos valores")
    void shouldBeEqualToAnotherInstanceWithSameValues() {
        // Arrange
        User user1 = User.builder()
            .id(testId)
            .name(testName)
            .lastName(testLastName)
            .birthDate(testBirthDate)
            .address(testAddress)
            .phoneNumber(testPhoneNumber)
            .email(testEmail)
            .salaryBase(testSalaryBase)
            .password(testPassword)
            .documentNumber(testDocumentNumber)
            .role(testRole)
            .build();

        User user2 = User.builder()
            .id(testId)
            .name(testName)
            .lastName(testLastName)
            .birthDate(testBirthDate)
            .address(testAddress)
            .phoneNumber(testPhoneNumber)
            .email(testEmail)
            .salaryBase(testSalaryBase)
            .password(testPassword)
            .documentNumber(testDocumentNumber)
            .role(testRole)
            .build();

        // Assert
        assertThat(user1).isEqualTo(user2);
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
    }

    @Test
    @DisplayName("Debería tener representación de string correcta")
    void shouldHaveCorrectStringRepresentation() {
        // Arrange
        User user = User.builder()
            .id(testId)
            .name(testName)
            .lastName(testLastName)
            .birthDate(testBirthDate)
            .address(testAddress)
            .phoneNumber(testPhoneNumber)
            .email(testEmail)
            .salaryBase(testSalaryBase)
            .password(testPassword)
            .documentNumber(testDocumentNumber)
            .role(testRole)
            .build();

        // Act
        String stringRepresentation = user.toString();

        // Assert
        assertThat(stringRepresentation).isNotNull();
        assertThat(stringRepresentation).contains(testId.toString());
        assertThat(stringRepresentation).contains(testName);
        assertThat(stringRepresentation).contains(testLastName);
        assertThat(stringRepresentation).contains(testEmail);
        assertThat(stringRepresentation).contains(testDocumentNumber);
    }
}
