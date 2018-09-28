package controller;
import java.util.ArrayList;
import model.DrugClass;;


public class DrugClassController {
	
	public static DrugClass findDrugClass(String drugClassName, ArrayList<DrugClass> drugClassList) {		
		for(DrugClass c: drugClassList) {
			if (c.getName() .equals(drugClassName))		
				return c;
		}
		return null;
	}
}
