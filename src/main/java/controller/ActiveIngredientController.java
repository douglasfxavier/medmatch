package controller;

import java.util.ArrayList;
import model.ActiveIngredient;

public class ActiveIngredientController {
	
	public ActiveIngredient findCompound(String activeIngredientName, ArrayList<ActiveIngredient> activeIngedientsList) {		
		
		for(ActiveIngredient c: activeIngedientsList) {
			if (c.getName() .equals(activeIngredientName))		
				return c;
		}
		
		return null;
	}
}
