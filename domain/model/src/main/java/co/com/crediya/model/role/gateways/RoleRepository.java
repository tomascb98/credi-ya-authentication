package co.com.crediya.model.role.gateways;

import co.com.crediya.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> saveRole(Role role);
    Mono<Role> findRoleById(Integer id);
    Mono<Role> findRoleByName(String name);
    Mono<Role> updateRole(Role role);
}
