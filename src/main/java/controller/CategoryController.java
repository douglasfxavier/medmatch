package controller;
import java.util.ArrayList;
import model.Category;;


public class CategoryController {
	public Category findCategory(String categoryName, ArrayList<Category> categoryList) {		
		for(Category c: categoryList) {
			if (c.getName() .equals(categoryName))		
				return c;
		}
		return null;
	}
}
