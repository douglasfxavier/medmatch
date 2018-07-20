package model;

import java.util.ArrayList;

public class Compound {
	private int id;
	private String name;
	private ArrayList<Ingredient> ingredients;
	
	public Compound(int id, String name, ArrayList<Ingredient> ingredients) {
		super();
		this.id = id;
		this.name = name;
		this.ingredients = ingredients;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	@Override
	public String toString() {
		return "Compound [id=" + id + ", name=" + name + ", ingredients=" + ingredients + "]";
	}
	
		
}
