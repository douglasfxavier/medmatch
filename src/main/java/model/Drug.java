package model;

public class Drug {
	private String drugCode;
	private String brandName;
	private String descritpion;
	private String strength;
	private Manufacturer manufacturer;
	private Country country;
	private Compound compound;
	private Category category;

	public Drug() {
	
	}
	
	public Drug(String drugCode, String brandName, String descritpion, String strength, Manufacturer manufacturer,
			Country country, Compound compound, Category category) {
		super();
		this.drugCode = drugCode;
		this.brandName = brandName;
		this.descritpion = descritpion;
		this.strength = strength;
		this.manufacturer = manufacturer;
		this.country = country;
		this.compound = compound;
		this.category = category;
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
	
	public Compound getCompound() {
		return compound;
	}
	
	public void setCompound(Compound compound) {
		this.compound = compound;
	}
	
	
	
	public Category getCategory() {
		return category;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return "Drug [drugCode=" + drugCode + ", brandName=" + brandName + ", descritpion=" + descritpion
				+ ", strength=" + strength + ", manufacturer=" + manufacturer + ", country=" + country + ", compound="
				+ compound + "]";
	}
	
	

}
