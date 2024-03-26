package dao.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import dao.ProductDao;

@TestInstance(Lifecycle.PER_CLASS)
public class ProductDaoTest {
	private ProductDao productDao;
	
	@BeforeAll
	void setup() {
		productDao = new ProductDao();
	}
	
	
	@AfterAll
	void teardown() {
		productDao.close();
		productDao = null;
	}

}
