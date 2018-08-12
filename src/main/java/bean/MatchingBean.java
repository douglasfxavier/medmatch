package bean;

import java.util.ArrayList;
import java.util.HashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import controller.CountryController;
import model.Country;

@ManagedBean ( name = "matchingBean")
@SessionScoped
public class MatchingBean {
	private ArrayList<Country> allCountries = new ArrayList<Country>();
	private String selectedCountryCode;
	private Country selectedCountry;
	private String url;
	//file
	private HashMap<String,String> mathings;
	
	public MatchingBean() {
		super();		
		CountryController countryController = new CountryController();
		this.allCountries = countryController.getAllContries();

	}

	public Country getSelectedCountry() {
		CountryController countryController = new CountryController();
		Country country = countryController.
				findCountry(Integer.parseInt(this.selectedCountryCode), allCountries);
		this.selectedCountry = country;
		
		return selectedCountry;
	}
	
	
	
	public String getSelectedCountryCode() {
		return selectedCountryCode;
	}

	public void setSelectedCountryCode(String selectedCountryCode) {
		this.selectedCountryCode = selectedCountryCode;
	}


	public void setAllCountries(ArrayList<Country> allCountries) {
		this.allCountries = allCountries;
	}



	public ArrayList<Country> getAllCountries() {
		return allCountries;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HashMap<String, String> getMathings() {
		return mathings;
	}

	public void setMathings(HashMap<String, String> mathings) {
		this.mathings = mathings;
	}
	
	public String loadMetadata() {
		return "matching?faces-redirect=true";

	}

}
