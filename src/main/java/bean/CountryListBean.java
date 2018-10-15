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
	CountryController countryController;
	ArrayList<Country> countryList = new ArrayList<>();
	ArrayList<String> countryNames = new ArrayList<>();

	@PostConstruct
	public void Init() {
		this.countryController = new CountryController();
		this.countryList = this.countryController.getCountryList();
	}
	
	public void setCountryList(ArrayList<Country> countryList) {
		this.countryList = countryList;
	}

	public void setCountryNames(ArrayList<String> countryNames) {
		this.countryNames = countryNames;
	}

	public ArrayList<Country> getCountryList() {
		return countryList;
	}

	public Country getCountry(int numericCode) {
		
		Country country = this.countryController.
				findCountry(numericCode);
		
		return country;
	}

}
