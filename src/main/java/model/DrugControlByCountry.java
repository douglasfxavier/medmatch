package model;

import java.util.Date;

public class DrugControlByCountry {
	private ActiveIngredient compound;
	private Boolean allowed;
	private Date lastUpdate;
	
	public DrugControlByCountry(ActiveIngredient compound, Boolean allowed, Date lastUpdate) {
		super();
		this.compound = compound;
		this.allowed = allowed;
		this.lastUpdate = lastUpdate;
	}

	public ActiveIngredient getCompound() {
		return compound;
	}

	public void setCompound(ActiveIngredient compound) {
		this.compound = compound;
	}

	public Boolean getAllowed() {
		return allowed;
	}

	public void setAllowed(Boolean allowed) {
		this.allowed = allowed;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
		
}
