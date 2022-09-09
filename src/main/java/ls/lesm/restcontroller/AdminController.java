package ls.lesm.restcontroller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ls.lesm.model.MasterEmployeeDetails;
import ls.lesm.model.Role;
import ls.lesm.model.User;
import ls.lesm.model.UserRole;
import ls.lesm.payload.request.RoleRequest;
import ls.lesm.repository.MasterEmployeeDetailsRepository;
import ls.lesm.repository.RoleRepository;
import ls.lesm.repository.UserRepository;
import ls.lesm.service.impl.AdminServiceImpl;
import ls.lesm.service.impl.UserServiceImpl;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
	
	
	private static final Logger LOG=LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRopository;
	
	@Autowired
	private AdminServiceImpl adminServiceImpl;
	
	@Autowired
	private MasterEmployeeDetailsRepository masterEmployeeDetailsRepository;
	
	//create User
	//UMER
	@PostMapping("/sign-up")
	public User createUser(@RequestBody User user,
			               @RequestParam String roleName) throws Exception {
		LOG.info("Enterd into createUser Method");

		MasterEmployeeDetails employee=this.masterEmployeeDetailsRepository.findByLancesoft(user.getUsername());
		user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
		user.setUsername(user.getUsername().toUpperCase());
		user.setEmail(employee.getEmail());
		user.setPhoneNo(employee.getPhoneNo());
		user.setFirstName(employee.getFirstName());
		user.setLastName(employee.getLastName());
		LOG.debug("Encrypted password");
		Set<UserRole> userRoleSet=new HashSet<>();

		Role role=new Role();         //default role "User"
		
		role.setRoleName(roleName);
		Role role1=this.roleRopository.findByRoleName(roleName);
		role.setRoleId(role1.getRoleId());

		user.setRoleName(role.getRoleName());

		UserRole userRole=new UserRole();
		userRole.setRole(role);
		userRole.setUser(user);
		LOG.debug("Assigned Default role to user to USER");
		userRoleSet.add(userRole);
		return this.userService.createUser(user, userRoleSet);
	}
	
	//UMER
	@PostMapping("/create-roles")
	public ResponseEntity<?> createRoles(@RequestBody Role role) {
	
		this.adminServiceImpl.createNewRole( role);
		return new ResponseEntity<Role>(HttpStatus.ACCEPTED);
		}
	//UMER
	@DeleteMapping("/delete-roles/{roleName}")
	public ResponseEntity<?> deleteRole(@PathVariable String roleName, Role role){
		this.adminServiceImpl.deleteRoles(roleName);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	//UMER
	@GetMapping("/all-roles")
	public ResponseEntity<List<Role>> allRoles(){
		List<Role> allroles= this.roleRopository.findAll();
		return new ResponseEntity<List<Role>>(allroles, HttpStatus.ACCEPTED);
		
	}

}
