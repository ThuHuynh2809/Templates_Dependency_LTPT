package dao;

import java.util.List;
import java.util.Map;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;

import entity.Category;
import entity.Product;
import util.AppUtils;

public class CategoryDao {
    private Driver driver;

    public CategoryDao() {
        driver = AppUtils.getDriver();
    }

    /**
     * Create a new category
     * 
     * @param category - Category object
     * @return void
     *         String query = "CREATE (n:Category {categoryID: $categoryID, categoryName: $categoryName, description: $description})";
     */

    public void addCategory(Category category) {
        String query = "CREATE (n:Category {categoryID: $categoryID, categoryName: $categoryName, description: $description})";

        Map<String, Object> pars = AppUtils.getProperties(category);

        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                return tx.run(query, pars).consume();
            });
        }
    }

    /**
     * Find one category by id
     * 
     * @param id - Category id
     * @return Category
     *         String query = "MATCH (n:Category) where n.categoryID= $id RETURN n";
     */

    public Category findOne(String categoryID) {
        String query = "MATCH (n:Category) where n.categoryID= $id RETURN n";
        Map<String, Object> pars = Map.of("id", categoryID);

        try (Session session = driver.session()) {
            return session.executeRead(tx -> {
                Result result = tx.run(query, pars);
                while (result.hasNext()) {
                    Record record = result.next();
                    Node node = record.get("n").asNode();
                    System.out.println("Category ID: " + node.get("categoryID").asString());
                    System.out.println("Category Name: " + node.get("categoryName").asString());
                    System.out.println("Description: " + node.get("description").asString());
                }
                return null;
            });
        }
    }

    /**
     * Update category information
     * 
     * @param category Category to be updated
     * @return void
     *         String query =
     * 
     */

    public void updateCategory(Category category) {
        String query = "MATCH (n:Category {categoryID: $categoryID}) "
                + "SET n.categoryName = $categoryName, n.description = $description";

        Map<String, Object> pars = AppUtils.getProperties(category);

        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                return tx.run(query, pars).consume();
            });
        }
    }

    /**
     * Delete category by id
     */

    public void deleteCategory(String categoryID) {
        String query = "MATCH (n:Category {categoryID: $id}) "
                + "DETACH DELETE n";

        Map<String, Object> pars = Map.of("id", categoryID);

        try (Session session = driver.session()) {
            session.executeWrite(tx -> {
                return tx.run(query, pars).consume();
            });
        }
    }

    /**
     * Get list of categories, limit to n
     * 
     * @param n number of categories to get
     * @return void
     *         String query =
     */

    public void listCategories(int n) {
        String query = "MATCH (n:Category) RETURN n LIMIT $n";
        Map<String, Object> pars = Map.of("n", n);

        try (Session session = driver.session()) {
            session.executeRead(tx -> {
                Result result = tx.run(query, pars);
                while (result.hasNext()) {
                    Record record = result.next();
                    Node node = record.get("n").asNode();
                    System.out.println("Category ID: " + node.get("categoryID").asString());
                    System.out.println("Category Name: " + node.get("categoryName").asString());
                    System.out.println("Description: " + node.get("description").asString());
                }
                return null;
            });
        }
    }
    
    public void listCategories() {
        String query = "MATCH (n:Category) RETURN n";
        try (Session session = driver.session()) {
            session.executeRead(tx -> {
                Result result = tx.run(query);
                while (result.hasNext()) {
                    Record record = result.next();
                    Node node = record.get("n").asNode();
                    System.out.println("Category ID: " + node.get("categoryID").asString());
                    System.out.println("Category Name: " + node.get("categoryName").asString());
                    System.out.println("Description: " + node.get("description").asString());
                }
                return null;
            });
        }
    }

	public void findByDescription(String description) {
		String query = "MATCH (n:Category) where n.description= $description RETURN n";
		Map<String, Object> pars = Map.of("description", description);

		try (Session session = driver.session()) {
			session.executeRead(tx -> {
				Result result = tx.run(query, pars);
				while (result.hasNext()) {
					Record record = result.next();
					Node node = record.get("n").asNode();
					System.out.println("Category ID: " + node.get("categoryID").asString());
					System.out.println("Category Name: " + node.get("categoryName").asString());
					System.out.println("Description: " + node.get("description").asString());
				}
				return null;
			});
		}
	}

	
	public List<Product> listProductsByCategory(String categoryName) {
//		String query = "MATCH  (p:Product)-[r:PART_OF]->(c:Category) where c.categoryName= $name RETURN p" ;
//		Map<String, Object> pars = Map.of("name", categoryName);
//		
//		try (Session session = driver.session()) {
//
//			return session.executeRead(tx -> {
//				Result result = tx.run(query, pars);
//				return result.stream()
//						.map(Record -> Record.get("p").asNode())
//						.map(node -> AppUtils.nodeToPOJO(node, Product.class))
//						.toList();
//			});
//		}
		String query = "MATCH  (p:Product)-[r:PART_OF]->(c:Category) where c.categoryName= $name RETURN p" ;
		Map<String, Object> pars = Map.of("name", categoryName);
		try (Session session = driver.session()) {
			return session.executeRead(tx -> {
				Result result = tx.run(query, pars);
				return result.stream().map(Record -> Record.get("p").asNode())
						.map(node -> AppUtils.nodeToPOJO(node, Product.class)).toList();
			});
		}
		
	}
    public void close() {
        driver.close();
    }

    public static void main(String[] args) {
        CategoryDao categoryDao = new CategoryDao();
        Category category = new Category("A2", "Thu", "sdaa");
        Category category1 = new Category("A1", "Thu", "Thu's ");
//        categoryDao.addCategory(category);
//        categoryDao.updateCategory(category1);
//        categoryDao.deleteCategory("A1");
//        categoryDao.findOne("A1");
//        categoryDao.deleteCategory("A124234");
//        categoryDao.findByDescription("sdaa");
        categoryDao.listCategories(10);
      
        categoryDao.close();
        System.out.println("Done!");
    }
}
