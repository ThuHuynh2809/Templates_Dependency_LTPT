package util;

import java.util.Map;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.types.Node;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AppUtils<T> {


	private static final Gson GSON = new Gson();

	public static Driver getDriver() {
		return GraphDatabase.driver("neo4j://localhost:7687", 
				AuthTokens.basic("neo4j", "12345678"));
	}

	public static <T> T nodeToPOJO(Node node, Class<T> clazz) { //Generic method
		
		Map<String, Object> properties = node.asMap(); // HashMap<String, Object>
//		System.out.println(properties);
		
		String json = GSON.toJson(properties);
//		System.out.println(json);
		
		T obj = GSON.fromJson(json, clazz);
		
		return obj;
	}

	public static <T> Map<String, Object> getProperties(T t) {
		
		String json = GSON.toJson(t);
//		System.out.println(json);
		
//		Map<String, Object> map = GSON.fromJson(json, Map.class);
		Map<String, Object> map = GSON.fromJson(json,  new TypeToken<Map<String, Object>>(){});
//		System.out.println(map);
		
		return map;
	}

}
