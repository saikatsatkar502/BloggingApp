package com.blogapp.backend;

import com.blogapp.backend.model.Role;
import com.blogapp.backend.model.User;
import com.blogapp.backend.repo.UserRepository;
import com.blogapp.backend.service.role.RoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class BloggingApplicationBackendApplication implements CommandLineRunner {

	@Autowired private RoleServiceImpl roleService;

	@Autowired private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(BloggingApplicationBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Role> roles = this.roleService.findAllRoles();
		if(roles.isEmpty()){
			Role role = new Role();
			role.setName("ROLE_ADMIN");
			this.roleService.saveRole(role);

			Role role1 = new Role();
			role1.setName("ROLE_USER");
			this.roleService.saveRole(role1);
		}

		if(this.userRepository.count()==0) {
			Role role = this.roleService.findRoleByName("ROLE_ADMIN");

			User user = new User();
			user.setName("Admin");
			user.setEmail("Admin@Admin.com");
			user.setPassword(new BCryptPasswordEncoder().encode("Admin@123"));
			user.setRoles(Set.of(role));
			this.userRepository.save(user);
		}

	}
}
