package model;

import java.util.ArrayList;

public class Drug {
	private String drugCode;
	private String brand;
	private String descritpion;
	private String strength;
	private Manufacturer manufacturer;
	private Country country;
	private ArrayList<ActiveIngredient> activeIngredients = new ArrayList<ActiveIngredient>();
	private ATCClass atcClass;

	public Drug() {
	
	}
	
	public Drug(String drugCode, String brand, String descritpion, String strength, Manufacturer manufacturer,
			Country country, ATCClass atcClass) {
		super();
		this.drugCode = drugCode;
		this.brand = brand;
		this.descritpion = descritpion;
		this.strength = strength;
		this.manufacturer = manufacturer;
		this.country = country;
		this.atcClass = atcClass;
	}

	public String getDrugCode() {
		return drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
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

	public Manufacturer getManufacturer() {
		return manufacturer;
	}
	public void addIngredient(ActiveIngredient ingredient) {
		this.activeIngredients.add(ingredient);
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

	public ATCClass getAtcClass() {
		return atcClass;
	}

	public void setAtcClass(ATCClass atcClass) {
		this.atcClass = atcClass;
	}

	@Override
	public String toString() {
		return "Drug [drugCode=" + drugCode + ", brand=" + brand + ", descritpion=" + descritpion + ", strength="
				+ strength + ", manufacturer=" + manufacturer + ", country=" + country + ", activeIngredients="
				+ activeIngredients + ", atcClass=" + atcClass + "]";
	}
}
