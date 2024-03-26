package entity;

import com.google.gson.annotations.SerializedName;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
//@AllArgsConstructor
@ToString
public class Product {
	@SerializedName	("productID")
	private String id;
	@SerializedName	("productName")
	private String name;
	private double unitPrice;
	private String quantityPerUnit;
	

	public Product(String id, String name, double unitPrice, String quantityPerUnit, Category category) {
		super();
		this.id = id;
		this.name = name;
		this.unitPrice = unitPrice;
		this.quantityPerUnit = quantityPerUnit;
		this.category = category;
	}


	public Product() {
		super();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getUnitPrice() {
		return unitPrice;
	}


	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}


	public String getQuantityPerUnit() {
		return quantityPerUnit;
	}


	public void setQuantityPerUnit(String quantityPerUnit) {
		this.quantityPerUnit = quantityPerUnit;
	}


	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


	@ToString.Exclude
	private Category category;

}
