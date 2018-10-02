package bean;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import controller.CountryController;
import model.Country;


@ManagedBean (name = "countryListBean")
@ApplicationScoped
public class CountryListBean {
	ArrayList<Country> allCountries = new ArrayList<>();
	ArrayList<String> countryNames = new ArrayList<>();

	@PostConstruct
	public void Init() {
		this.allCountries = CountryController.getAllContries();
	}
	
	public void setAllCountries(ArrayList<Country> allCountries) {
		this.allCountries = allCountries;
	}

	public void setCountryNames(ArrayList<String> countryNames) {
		this.countryNames = countryNames;
	}

	public ArrayList<Country> getAllCountries() {
		return allCountries;
	}

	public Country getCountry(int numericCode) {
		Country country = CountryController.
				findCountry(numericCode, this.allCountries);
		
		return country;
	}

}
