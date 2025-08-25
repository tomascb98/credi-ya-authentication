package co.com.crediya.usecase.role;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
public class RoleUseCase implements RoleRepository {

    private final RoleRepository repository;
    @Override
    public Role saveRole(Role role) {
        return repository.saveRole(role);
    }

    @Override
    public Role findRoleById(Long id) {
        return repository.findRoleById(id);
    }

    @Override
    public Role findRoleByName(String name) {
        return findRoleByName(name);
    }

    @Override
    public Role updateRole(Role role) {
        return updateRole(role);
    }
}
