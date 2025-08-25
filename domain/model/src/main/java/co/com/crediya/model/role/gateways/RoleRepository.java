package co.com.crediya.model.role.gateways;

import co.com.crediya.model.role.Role;

public interface RoleRepository {
    Role saveRole(Role role);
    Role findRoleById(Long id);
    Role findRoleByName(String name);
    Role updateRole(Role role);
}
