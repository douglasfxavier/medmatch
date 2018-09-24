package model;

import java.util.ArrayList;

public class ActiveIngredient {
	private int id;
	private String name;
	
	public ActiveIngredient(int id, String name) {
		super();
		this.id = id;
		this.name = name;		
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

	@Override
	public String toString() {
		return "ActiveIngredient [id=" + id + ", name=" + name + "]";
	}
		
}
