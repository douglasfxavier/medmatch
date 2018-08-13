package bean;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import controller.CountryController;
import model.Country;


@ManagedBean (name = "countryListBean")
@SessionScoped
public class CountryListBean {
	CountryController countryController = new CountryController();
	ArrayList<Country> allCountries = new ArrayList<Country>();

	@PostConstruct
	public void Init() {
		this.allCountries = this.countryController.getAllContries();
	}


	public ArrayList<Country> getAllCountries() {
		return allCountries;
	}

	public Country getCountry(int numericCode) {
		Country country = this.countryController.
				findCountry(numericCode, this.allCountries);
		
		return country;
	}	
}
