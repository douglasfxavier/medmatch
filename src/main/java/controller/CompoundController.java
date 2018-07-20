package controller;

import java.util.ArrayList;
import model.Compound;

public class CompoundController {
	
	public Compound findCompound(String compoundName, ArrayList<Compound> compoundList) {		
		
		for(Compound c: compoundList) {
			if (c.getName() .equals(compoundName))		
				return c;
		}
		
		return null;
	}
}
