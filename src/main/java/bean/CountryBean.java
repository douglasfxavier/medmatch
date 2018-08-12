package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean (name = "countryBean")
@SessionScoped
public class CountryBean {
	private String uri;
	private int numericCode; 
	private String countryName;
	private String alphaCode;
	
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
	
}
