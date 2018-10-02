package controller;

import java.util.ArrayList;

import model.Manufacturer;;

public class ManufacturerController {
	
	
	public Manufacturer findManufacturer(String manufacturerName, ArrayList<Manufacturer> manufacturerList) {
		
		for(Manufacturer m: manufacturerList) {
			if (m.getName().toLowerCase().equals(manufacturerName.toLowerCase()))
				return m;
		}
		
		return null;
	}

}
