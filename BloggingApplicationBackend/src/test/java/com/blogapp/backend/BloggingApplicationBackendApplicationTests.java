package com.blogapp.backend;

import com.blogapp.backend.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BloggingApplicationBackendApplicationTests {

	@Autowired
	UserController userController;

	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
	}

}
