package controller;

import java.util.ArrayList;
import model.Country;
import util.WikiData;;

public class CountryController {
	
	public static ArrayList<Country> getAllContries() {
		ArrayList<Country> allCountries;
		WikiData wdc = new WikiData();
		allCountries = wdc.getCountries();
		
		return allCountries;
	}

	public static Country findCountry(int numericCode, ArrayList<Country> countryList) {		
		
		for(Country c: countryList) {
			if (c.getNumericCode() == numericCode)		
				return c;
		}
		
		return null;
	}
	
	public static Country findCountryByURI(String uri, ArrayList<Country> countryList) {		
		
		for(Country c: countryList) {
			if (c.getUri().equals(uri))		
				return c;
		}
		
		return null;
	}

	

}
