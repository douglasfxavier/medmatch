package controller;

import java.util.ArrayList;

import model.Drug;;

public class DrugController {

	public Drug findDrugByCode(String drugCode, ArrayList<Drug> drugsList) {
		
		for(Drug d: drugsList) {
			if (d.getDrugCode().equals(drugCode))
				return d;
		}
		
		return null;
	}

}
