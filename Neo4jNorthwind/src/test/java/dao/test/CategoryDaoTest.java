package dao.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import dao.CategoryDao;
import entity.Category;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryDaoTest {
	
	
	private CategoryDao categoryDao;

	@BeforeAll
	void setup() {
		categoryDao = new CategoryDao();
	}
	
	@Test
	void testFindOne() {
		Category category = categoryDao.findOne("1");
		System.out.println(category);
		assertNotNull(category);
		assertEquals("Beverages", category.getName());
		assertEquals(true, category.getDescription().contains("coffees"));
	}
	
	@Test
	void testFindOneNull() {
		Category category = categoryDao.findOne("A1");
		assertEquals(null, category);
	}
	
	
	@AfterAll
	void tearDown() {
		categoryDao.close();
		categoryDao = null; // help gc - garbage collector
	}
	
}
