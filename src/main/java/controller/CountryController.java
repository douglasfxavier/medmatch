package controller;

import java.util.ArrayList;
import model.Country;
import util.WikiDataCountry;;

public class CountryController {
	
	public static ArrayList<Country> getAllContries() {
		ArrayList<Country> allCountries;
		WikiDataCountry wdc = new WikiDataCountry();
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
	

}
