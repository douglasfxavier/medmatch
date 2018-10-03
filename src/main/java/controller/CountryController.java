package controller;

import java.util.ArrayList;
import model.Country;
import util.WikiData;;

public class CountryController {
	ArrayList<Country> countryList;
	
	
	public CountryController() {
		WikiData wdc = new WikiData();
		this.countryList = wdc.getCountries(); 
	}

	public ArrayList<Country> getCountryList() {
				
		return countryList;
	}

	public Country findCountry(int numericCode) {		
		
		for(Country c: this.countryList) {
			if (c.getNumericCode() == numericCode)		
				return c;
		}
		
		return null;
	}
	
	public Country findCountryByURI(String uri) {		
		
		for(Country c: this.countryList) {
			if (c.getUri().equals(uri))		
				return c;
		}
		
		return null;
	}

	

}
