package com.savelife.blood_donation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import com.savelife.blood_donation.entity.UserRole;
import com.savelife.blood_donation.repository.DonorProfileRepository;
import com.savelife.blood_donation.repository.UserRepository;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:savelife;DB_CLOSE_DELAY=-1",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.datasource.driver-class-name=org.h2.Driver"
})
@AutoConfigureMockMvc
class BloodDonationApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository users;

	@Autowired
	private DonorProfileRepository profiles;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoads() {
	}

	@Test
	void publicPagesRenderAndAdminPageRequiresAuthentication() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/register"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/requests"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/admin"))
				.andExpect(status().is3xxRedirection());
	}

	@Test
	void registrationCreatesDonorWithEncodedPasswordAndProfile() throws Exception {
		mockMvc.perform(post("/register")
				.with(csrf())
				.param("fullName", "Test Donor")
				.param("email", "test-donor@example.test")
				.param("password", "a-long-test-password"))
				.andExpect(status().is3xxRedirection());

		var donor = users.findByEmailIgnoreCase("test-donor@example.test").orElseThrow();
		assertThat(donor.getRole()).isEqualTo(UserRole.DONOR);
		assertThat(donor.getPassword()).isNotEqualTo("a-long-test-password");
		assertThat(passwordEncoder.matches("a-long-test-password", donor.getPassword())).isTrue();
		assertThat(profiles.findByUserId(donor.getId())).isPresent();

		var login = mockMvc.perform(formLogin()
				.user("test-donor@example.test")
				.password("a-long-test-password"))
				.andExpect(status().is3xxRedirection())
				.andReturn();
		MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);
		assertThat(session).isNotNull();

		mockMvc.perform(get("/dashboard").session(session))
				.andExpect(status().isOk());
		mockMvc.perform(get("/profile").session(session))
				.andExpect(status().isOk());
		mockMvc.perform(post("/requests")
				.session(session)
				.with(csrf())
				.param("bloodGroup", "O_POSITIVE")
				.param("units", "2")
				.param("hospitalName", "City Hospital")
				.param("city", "Example City")
				.param("contactPhone", "+1 555 555 5555")
				.param("message", "Urgent request"))
				.andExpect(status().is3xxRedirection());
		mockMvc.perform(get("/admin").with(user("admin").roles("ADMIN")))
				.andExpect(status().isOk());
	}

	@Test
	void registrationRejectsPasswordsThatExceedBcryptByteLimit() throws Exception {
		String longUtf8Password = "\u20AC".repeat(25);
		mockMvc.perform(post("/register")
				.with(csrf())
				.param("fullName", "Invalid Password Donor")
				.param("email", "long-password@example.test")
				.param("password", longUtf8Password))
				.andExpect(status().isOk());

		assertThat(users.existsByEmailIgnoreCase("long-password@example.test")).isFalse();
	}
}
