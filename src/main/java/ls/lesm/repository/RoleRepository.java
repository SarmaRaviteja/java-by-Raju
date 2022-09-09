package ls.lesm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ls.lesm.model.Role;
import ls.lesm.payload.request.RoleRequest;

public interface RoleRepository extends JpaRepository<Role, Integer> {



	Role findByRoleName(String roleName);

	Role findByRoleId(Integer roleId);

	void deleteByRoleName(String roleName);

	void save(RoleRequest role);


}
