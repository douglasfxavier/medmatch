package model;

public class Country {
	private int id;
	private String name;
	private String abbreviation;
	
	public Country(int id, String name, String abbreviation) {
		super();
		this.id = id;
		this.name = name;
		this.abbreviation= abbreviation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + "]";
	}
	
	
}
