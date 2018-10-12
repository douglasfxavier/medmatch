package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.jena.rdf.model.Model;

import controller.CountryController;
import controller.DrugSearch;
import model.Country;
import model.MockDrug;
import util.FusekiConnector;


@ManagedBean (name = "searchBean")
@ViewScoped
public class SearchBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Country> countryList;
	private String originCountry;
	private String targetCountry;
	private String drugBrand;
	private Model targetModel;
	private Set<Entry<MockDrug,Double>> matchedDrugs;
	//private LinkedList<MockDrug> drugList = new LinkedList<MockDrug>();
	
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

	public Set<Entry<MockDrug,Double>> getMatchedDrugs() {
		return matchedDrugs;
	}
	public void setMatchedDrugs(Set<Entry<MockDrug,Double>> matchedDrugs) {
		this.matchedDrugs = matchedDrugs;
	}
	public String search() throws Exception  {
		try {
			
			FusekiConnector fusekiConnector = new FusekiConnector();
									
			//Dumping target country model
			HashMap<String, String> targetCountryServices = fusekiConnector.getDatasetDetailsByCountry(targetCountry);
			String targetDumpService = targetCountryServices.get("dumpService");
			this.targetModel = fusekiConnector.dumpData(targetDumpService);
			
			this.matchedDrugs = DrugSearch.compareDrugByBrand(drugBrand,originCountry,targetModel);
			
//			for(Map.Entry<MockDrug,Double> entry: matchedDrugs) {
//				MockDrug drug = entry.getKey();
//				Double metric = entry.getValue();
//				System.out.println(String.format("Metric: %s    Brand: %s      URI: %s",
//						metric, drug.getBrand(), drug.getDrugURI()));
//			}		
			return null;		
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
