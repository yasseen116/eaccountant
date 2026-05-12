package com.artal.eaccountant;

import com.artal.eaccountant.catalog.ProductCategory;
import com.artal.eaccountant.catalog.ProductCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
}

