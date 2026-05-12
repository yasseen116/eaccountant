package com.artal.eaccountant;

import com.artal.eaccountant.auth.UserAccount;
import com.artal.eaccountant.auth.UserAccountRepository;
import com.artal.eaccountant.catalog.ProductCategory;
import com.artal.eaccountant.catalog.ProductCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EaccountantApplication {

	public static void main(String[] args) {
		SpringApplication.run(EaccountantApplication.class, args);
	}
	// Seeds default categories when the database is empty.
	@Bean
	CommandLineRunner seedcategories(ProductCategoryRepository categoryRepository) {
		return args -> {
			if (categoryRepository.count() == 0) {
				createCategory(categoryRepository, "Money Clip");
				createCategory(categoryRepository, "Cardholder");
				createCategory(categoryRepository, "Stickers");
				createCategory(categoryRepository, "Holo Stickers");
				createCategory(categoryRepository, "Bookmark");
			}
		};
	}
	// Creates and saves one category.
	private void createCategory(ProductCategoryRepository repository, String name) {
		ProductCategory category = new ProductCategory();
		category.setName(name);
		category.setActive(true);
		repository.save(category);
	}

	// Seeds default admin user when no users exist.
	@Bean
	CommandLineRunner seedAdminUser(UserAccountRepository userRepository,
									PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.count() == 0) {
				UserAccount admin = new UserAccount();
				admin.setName("Admin");
				admin.setEmail("admin@eaccountant.local");
				admin.setPassword(passwordEncoder.encode("admin123"));
				admin.setRole("ADMIN");
				admin.setActive(true);

				userRepository.save(admin);

				System.out.println("Default admin user created.");
			}
		};
	}
}

