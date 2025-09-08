package co.com.crediya.usecase.role;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import reactor.core.publisher.Mono;

public class RoleUseCase {
    private final RoleRepository roleRepository;

    public RoleUseCase(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Mono<Role> findById(Integer id) {
        return roleRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Role con id " + id + " no encontrado")));
    }

    public Mono<Boolean> validateRoleExists(Integer id) {
        return roleRepository.existsById(id);
    }
}