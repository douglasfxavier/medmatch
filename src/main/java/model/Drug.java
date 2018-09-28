package model;

import java.util.ArrayList;

public class Drug {
	private String drugCode;
	private String brandName;
	private String descritpion;
	private String strength;
	private Manufacturer manufacturer;
	private Country country;
	private ArrayList<ActiveIngredient> activeIngredients = new ArrayList<ActiveIngredient>();
	private DrugClass drugClass;

	public Drug() {
	
	}
	
	public Drug(String drugCode, String brandName, String descritpion, String strength, Manufacturer manufacturer,
			Country country, DrugClass drugClass) {
		super();
		this.drugCode = drugCode;
		this.brandName = brandName;
		this.descritpion = descritpion;
		this.strength = strength;
		this.manufacturer = manufacturer;
		this.country = country;
		this.drugClass = drugClass;
	}
	public String getDrugCode() {
		return drugCode;
	}
	
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	
	public String getBrandName() {
		return brandName;
	}
	
	public void setBrandName(String brandName) {
		this.brandName = brandName;
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
	
	public Manufacturer getManufacturer() {
		return manufacturer;
	}
	
	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	public Country getCountry() {
		return country;
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}
	
	
	public ArrayList<ActiveIngredient> getActiveIngredients() {
		return activeIngredients;
	}

	public void setActiveIngredients(ArrayList<ActiveIngredient> activeIngredients) {
		this.activeIngredients = activeIngredients;		
	}

	public DrugClass getDrugClass() {
		return drugClass;
	}
	
	public void setDrugClass(DrugClass drugClass) {
		this.drugClass = drugClass;
	}

	@Override
	public String toString() {
		return "Drug [drugCode=" + drugCode + ", brandName=" + brandName + ", descritpion=" + descritpion
				+ ", strength=" + strength + ", manufacturer=" + manufacturer + ", country=" + country
				+ ", activeIngredients=" + activeIngredients + ", drugClass=" + drugClass + "]";
	}
	
}
