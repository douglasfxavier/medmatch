package model;

public class Ingredient {
	private int id;
	private String ingredientName;
	
	public Ingredient(int id, String ingredientName) {
		super();
		this.id = id;
		this.ingredientName = ingredientName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIngredientName() {
		return ingredientName;
	}

	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}
	
	
}
