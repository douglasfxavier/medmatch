package model;

public class ATCClass {
	private String code;
	private String name;
	private String description;
	
	public ATCClass(String atcCode, String name, String description) {
		super();
		this.code = atcCode;
		this.name = name;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
		
}
