package bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.swing.plaf.synth.SynthSeparatorUI;

import controller.CountryController;
import model.Country;
import util.FusekiConnector;

@ManagedBean (name = "searchBean")
@SessionScoped
public class SearchBean {
	private ArrayList<Country> countryList;
	private String originCountry;
	private String targetCountry;
	private String drugBrand;
	
	public ArrayList<Country> getCountryList() {
		if (countryList == null) {
			CountryController countryController = new CountryController();
			this.countryList = countryController.getCountryList();
		}
		return this.countryList;
	}
	public void setCountryList(ArrayList<Country> countryList) {
		this.countryList = countryList;
	}
	public String getOriginCountry() {
		return originCountry;
	}
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	public String getTargetCountry() {
		return targetCountry;
	}
	public void setTargetCountry(String targetCountry) {
		this.targetCountry = targetCountry;
	}
	public String getDrugBrand() {
		return drugBrand;
	}
	public void setDrugBrand(String drugBrand) {
		this.drugBrand = drugBrand;
	}
	
	public String search() {

		return null;
	}
}
