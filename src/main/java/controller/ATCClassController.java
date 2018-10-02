package controller;
import java.util.ArrayList;
import model.ATCClass;;


public class ATCClassController {

	public static ATCClass findATCClassByCode(String atcCode, ArrayList<ATCClass> atcClassList) {		
		for(ATCClass c: atcClassList) {
			if (c.getCode().equals(atcCode))		
				return c;
		}
		return null;
	}
	
	public static ATCClass findATCClassByName(String atcClassName, ArrayList<ATCClass> atcClassList) {		
		for(ATCClass c: atcClassList) {
			if (c.getName().toLowerCase().equals(atcClassName.toLowerCase()))		
				return c;
		}
		return null;
	}
}
