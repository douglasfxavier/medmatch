package model;

import java.util.ArrayList;

public class MockDrug {
	private Double metric;
	private String drugURI;
	private String brand;
	private String descritpion;
	private String strength;
	private String manufacturer;
	private String country;
	private ArrayList<String> activeIngredients = new ArrayList<String>();
	
	public Double getMetric() {
		return metric;
	}
	public void setMetric(Double metric) {
		this.metric = metric;
	}
	public String getDrugURI() {
		return drugURI;
	}
	public void setDrugURI(String drugURI) {
		this.drugURI = drugURI;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public String getDescritpion() {
		return descritpion;
	}
	
	public void setDescritpion(String descritpion) {
		this.descritpion = descritpion;
	}
	
	public String getStrength() {
		return strength;
	}
	
	public void setStrength(String strength) {
		this.strength = strength;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}
	
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public ArrayList<String> getActiveIngredients() {
		return activeIngredients;
	}
	
	public void setActiveIngredients(ArrayList<String> activeIngredients) {
		this.activeIngredients = activeIngredients;
	}
	
	public void addIngredient (String ingredient) {
		this.activeIngredients.add(ingredient);
	}

}
