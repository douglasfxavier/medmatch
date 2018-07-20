package controller;

import java.util.ArrayList;

import model.Drug;;

public class DrugController {

	public Drug findCategory(String drugName, ArrayList<Drug> drugsList) {
		
		for(Drug d: drugsList) {
			if (d.getBrandName().equals(drugName))
				return d;
		}
		
		return null;
	}

}
