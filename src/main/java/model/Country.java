package model;

public class Country {
	private String uri;
	private int numericCode; 
	private String countryName;
	private String alphaCode;

	
	
	public Country(int numericCode, String countryName,  String alphaCode) {
		super();
		this.numericCode = numericCode;
		this.countryName = countryName;
		this.alphaCode= alphaCode;
	}
	
	public Country(String uri, int numericCode, String countryName, String alphaCode) {
		super();
		this.uri = uri;
		this.numericCode = numericCode;
		this.countryName = countryName;
		this.alphaCode = alphaCode;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public int getNumericCode() {
		return numericCode;
	}

	public void setNumericCode(int numericCode) {
		this.numericCode = numericCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getAlphaCode() {
		return alphaCode;
	}

	public void setAlphaCode(String alphaCode) {
		this.alphaCode = alphaCode;
	}

	@Override
	public String toString() {
		return "Country [uri=" + uri + ", numericCode=" + numericCode + ", countryName=" + countryName + ", alphaCode="
				+ alphaCode + "]";
	}
	
}
