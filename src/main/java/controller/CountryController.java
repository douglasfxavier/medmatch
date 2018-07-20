package controller;

import java.util.ArrayList;
import model.Country;;

public class CountryController {
	
	public Country findCountry(String countryName, ArrayList<Country> countryList) {		
		
		for(Country c: countryList) {
			if (c.getName() .equals(countryName))		
				return c;
		}
		
		return null;
	}
}
