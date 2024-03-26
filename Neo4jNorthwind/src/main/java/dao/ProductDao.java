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

public class ProductDao {
	private Driver driver;
	
	public ProductDao() {
		driver = AppUtils.getDriver();
	}
	
	public void addProduct(Product product) {
		String query = "CREATE (n:Product {productID: $productID, productName: $productName, unitPrice: $unitPrice, quantityPerUnit: $quantityPerUnit})";
		Map<String, Object> pars = AppUtils.getProperties(product);

		try (Session session = driver.session()) {
			session.executeWrite(tx -> {
				return tx.run(query, pars).consume();
			});
		}
	}
	/**
	 * Get list of products, limit to n
	 * @param n - number of products to get
	 * @return void
	 * String query = "MATCH (n:Product) RETURN n LIMIT $n";
     */
	 public void listProducts(int n) {
		 String query = "MATCH (n:Product) RETURN n LIMIT $n";
		 Map<String, Object> pars = Map.of("n", n);
		 
		 try (Session session = driver.session()) {
             session.executeRead(tx -> {
                 Result result = tx.run(query, pars);
                 while (result.hasNext()) {
                     Record record = result.next();
                     Node node = record.get("n").asNode();
                     System.out.println("Product Id: " + node.get("productID").asString());
                     System.out.println("Product Name: " + node.get("productName").asString());
                     System.out.println("Unit Price: " + node.get("unitPrice").asDouble());
                     System.out.println("Quantity Per Unit: " + node.get("quantityPerUnit").asString());
                 }
                 return null;
                 });
             }
	 }
	
	public List<Product> listProductsByCategory(String categoryName) {
		String query = "MATCH  (p:Product)-[r:PART_OF]->(c:Category) where c.categoryName= $name RETURN p" ;
		Map<String, Object> pars = Map.of("name", categoryName);
		
		try (Session session = driver.session()) {

			return session.executeRead(tx -> {
				Result result = tx.run(query, pars);
				return result.stream()
						.map(Record -> Record.get("p").asNode())
						.map(node -> AppUtils.nodeToPOJO(node, Product.class))
						.toList();
			});
		}
		
	}
	
	
	public Product findOne(String id) {
		
		String query = "MATCH (n:Product) WHERE n.productID= $id RETURN n";
		Map<String, Object> pars = Map.of("id", id);
		
		try(Session session = driver.session()){
			
			return session.executeRead(tx -> {
				Result result = tx.run(query, pars);
				
				if(!result.hasNext())
					return null;
				
			 	Record record = result.next();
				Node node = record.get("n").asNode();
				
				
				return AppUtils.nodeToPOJO(node, Product.class);
			});
		}
	}
	
	public void findMaxUnitPrice() {
		String query = "MATCH (n:Product) RETURN max(n.unitPrice) as maxUnitPrice";

		try (Session session = driver.session()) {
			session.executeRead(tx -> {
				Result result = tx.run(query);
				Record record = result.next();
				System.out.println("Max Unit Price: " + record.get("maxUnitPrice").asDouble());
				return null;
			});
		}
	}
	public void getNumberProductsByName() {
		String query = "MATCH (n:Product) RETURN n.productName as productName, count(n) as count";

		try (Session session = driver.session()) {
			session.executeRead(tx -> {
				Result result = tx.run(query);
				while (result.hasNext()) {
					Record record = result.next();
					System.out.println("Product Name: " + record.get("productName").asString());
					System.out.println("Count: " + record.get("count").asInt());
				}
				return null;
			});
		}
	}

	public void sumUnitPrice(String id) {
		String query = "MATCH (n:Product) where n.productID= $id RETURN sum(n.unitPrice) as sumUnitPrice";
		Map<String, Object> pars = Map.of("id", id);

		try (Session session = driver.session()) {
			session.executeRead(tx -> {
				Result result = tx.run(query, pars);
				Record record = result.next();
				System.out.println("Sum Unit Price: " + record.get("sumUnitPrice").asDouble());
				return null;
			});
		}
	}
	
	public void close() {
		driver.close();
	}
	
	public static void main(String[] args) {
		ProductDao productDao = new ProductDao();
//		Product product = new Product("1", "Anh", 30.0, "10 boxes x 20 bags");
		Category category = new Category("C01", "Laptop2", "Laptop category");
		Product product = new Product("2", "Binh", 20.0, "10 boxes x 20 bags", category);
//		productDao.addProduct(product);
		productDao.listProducts(10);
//  	productDao.findMaxUnitPrice();
//		productDao.getNumberProductsByName();
//		productDao.sumUnitPrice("1");
		List<Product> products = productDao.listProductsByCategory("Laptop2");
		System.out.println(products.size());
		products.forEach(System.out::println);
		productDao.close();
	}
	
}
