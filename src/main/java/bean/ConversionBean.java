package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

@ManagedBean (name = "conversionBean")
public class ConversionBean {
	@ManagedProperty (value = "#{matchingBean}")
	private MatchingBean matchingBean;
	//Fields send by post from the view matching.xhtml
	private String CategorySelect;
	private String CompoundNameSelect;
	private String DrugCodeSelect;
	private String DrugNameSelect;
	private String ManufacturerIDSelect;
	private String ManufacturerNameSelect;
	private String StrengthSelect;
	
	public MatchingBean getMatchingBean() {
		return matchingBean;
	}

	public void setMatchingBean(MatchingBean matchingBean) {
		this.matchingBean = matchingBean;
	}
	
	public String getCategorySelect() {
		return CategorySelect;
	}

	public void setCategorySelect(String categorySelect) {
		CategorySelect = categorySelect;
	}

	public String getCompoundNameSelect() {
		return CompoundNameSelect;
	}

	public void setCompoundNameSelect(String compoundNameSelect) {
		CompoundNameSelect = compoundNameSelect;
	}

	public String getDrugCodeSelect() {
		return DrugCodeSelect;
	}

	public void setDrugCodeSelect(String drugCodeSelect) {
		DrugCodeSelect = drugCodeSelect;
	}

	public String getDrugNameSelect() {
		return DrugNameSelect;
	}

	public void setDrugNameSelect(String drugNameSelect) {
		DrugNameSelect = drugNameSelect;
	}

	public String getManufacturerIDSelect() {
		return ManufacturerIDSelect;
	}

	public void setManufacturerIDSelect(String manufacturerIDSelect) {
		ManufacturerIDSelect = manufacturerIDSelect;
	}

	public String getManufacturerNameSelect() {
		return ManufacturerNameSelect;
	}

	public void setManufacturerNameSelect(String manufacturerNameSelect) {
		ManufacturerNameSelect = manufacturerNameSelect;
	}

	public String getStrengthSelect() {
		return StrengthSelect;
	}

	public void setStrengthSelect(String strengthSelect) {
		StrengthSelect = strengthSelect;
	}

	public String convert() {
		
		return "conversion?faces-redirect=true";
	}

}
