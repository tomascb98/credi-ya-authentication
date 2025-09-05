package co.com.crediya.model.role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    private Integer testId;
    private String testName;
    private String testDescription;
    private Date testCreatedAt;
    private Date testUpdatedAt;

    @BeforeEach
    void setUp() {
        testId = 1;
        testName = "ADMIN";
        testDescription = "Rol de administrador del sistema";
        testCreatedAt = new Date();
        testUpdatedAt = new Date();
    }

    @Test
    @DisplayName("Debería crear Role con builder")
    void shouldCreateRoleWithBuilder() {
        // Act
        Role role = Role.builder()
            .id(testId)
            .name(testName)
            .description(testDescription)
            .createdAt(testCreatedAt)
            .updatedAt(testUpdatedAt)
            .build();

        // Assert
        assertThat(role).isNotNull();
        assertThat(role.getId()).isEqualTo(testId);
        assertThat(role.getName()).isEqualTo(testName);
        assertThat(role.getDescription()).isEqualTo(testDescription);
        assertThat(role.getCreatedAt()).isEqualTo(testCreatedAt);
        assertThat(role.getUpdatedAt()).isEqualTo(testUpdatedAt);
    }

    @Test
    @DisplayName("Debería crear Role con constructor por defecto")
    void shouldCreateRoleWithDefaultConstructor() {
        // Act
        Role role = new Role();

        // Assert
        assertThat(role).isNotNull();
        assertThat(role.getId()).isNull();
        assertThat(role.getName()).isNull();
        assertThat(role.getDescription()).isNull();
        assertThat(role.getCreatedAt()).isNull();
        assertThat(role.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Debería crear Role con constructor con parámetros")
    void shouldCreateRoleWithParameterizedConstructor() {
        // Act
        Role role = new Role(testId, testName, testDescription, testCreatedAt, testUpdatedAt);

        // Assert
        assertThat(role).isNotNull();
        assertThat(role.getId()).isEqualTo(testId);
        assertThat(role.getName()).isEqualTo(testName);
        assertThat(role.getDescription()).isEqualTo(testDescription);
        assertThat(role.getCreatedAt()).isEqualTo(testCreatedAt);
        assertThat(role.getUpdatedAt()).isEqualTo(testUpdatedAt);
    }

    @Test
    @DisplayName("Debería usar toBuilder correctamente")
    void shouldUseToBuilderCorrectly() {
        // Arrange
        Role original = Role.builder()
            .id(testId)
            .name(testName)
            .description(testDescription)
            .createdAt(testCreatedAt)
            .updatedAt(testUpdatedAt)
            .build();

        // Act
        Role copy = original.toBuilder()
            .name("USER")
            .description("Rol de usuario")
            .build();

        // Assert
        assertThat(copy).isNotSameAs(original);
        assertThat(copy.getId()).isEqualTo(original.getId());
        assertThat(copy.getName()).isEqualTo("USER");
        assertThat(copy.getDescription()).isEqualTo("Rol de usuario");
        assertThat(copy.getCreatedAt()).isEqualTo(original.getCreatedAt());
        assertThat(copy.getUpdatedAt()).isEqualTo(original.getUpdatedAt());
    }

    @Test
    @DisplayName("Debería manejar valores null correctamente")
    void shouldHandleNullValuesCorrectly() {
        // Act
        Role role = Role.builder()
            .id(null)
            .name(null)
            .description(null)
            .createdAt(null)
            .updatedAt(null)
            .build();

        // Assert
        assertThat(role).isNotNull();
        assertThat(role.getId()).isNull();
        assertThat(role.getName()).isNull();
        assertThat(role.getDescription()).isNull();
        assertThat(role.getCreatedAt()).isNull();
        assertThat(role.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Debería ser igual a otra instancia con los mismos valores")
    void shouldBeEqualToAnotherInstanceWithSameValues() {
        // Arrange
        Role role1 = Role.builder()
            .id(testId)
            .name(testName)
            .description(testDescription)
            .createdAt(testCreatedAt)
            .updatedAt(testUpdatedAt)
            .build();

        Role role2 = Role.builder()
            .id(testId)
            .name(testName)
            .description(testDescription)
            .createdAt(testCreatedAt)
            .updatedAt(testUpdatedAt)
            .build();

        // Assert
        assertThat(role1).isEqualTo(role2);
        assertThat(role1.hashCode()).isEqualTo(role2.hashCode());
    }

    @Test
    @DisplayName("Debería tener representación de string correcta")
    void shouldHaveCorrectStringRepresentation() {
        // Arrange
        Role role = Role.builder()
            .id(testId)
            .name(testName)
            .description(testDescription)
            .createdAt(testCreatedAt)
            .updatedAt(testUpdatedAt)
            .build();

        // Act
        String stringRepresentation = role.toString();

        // Assert
        assertThat(stringRepresentation).isNotNull();
        assertThat(stringRepresentation).contains(testId.toString());
        assertThat(stringRepresentation).contains(testName);
        assertThat(stringRepresentation).contains(testDescription);
    }
}
