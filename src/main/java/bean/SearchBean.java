package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
public class SearchBean{
	private ArrayList<Country> countryList;
	private String originCountry;
	private String targetCountry;
	private String drugBrand;
	private Model targetModel;
	private Map<Double, MockDrug> matchedDrugsMap;	
	
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
	
	public ArrayList<MockDrug> getMatchedDrugs() {
		ArrayList<MockDrug> drugList = new ArrayList<>();
		if (matchedDrugsMap != null){
			int index = 0;
			for(Map.Entry<Double, MockDrug> entry : matchedDrugsMap.entrySet()) {
				drugList.add(index++,entry.getValue());
			}
		}
		return drugList;
	}
	
	public Map<Double, MockDrug> getMatchedDrugsMap() {
		return matchedDrugsMap;
	}
	public void setMatchedDrugsMap(Map<Double, MockDrug> matchedDrugsMap) {
		this.matchedDrugsMap = matchedDrugsMap;
	}

	public String search() throws Exception  {
		try {
			
			FusekiConnector fusekiConnector = new FusekiConnector();
									
			//Dumping target country model
			HashMap<String, String> targetCountryServices = fusekiConnector.getDatasetDetailsByCountry(targetCountry);
			String targetDumpService = targetCountryServices.get("dumpService");
			this.targetModel = fusekiConnector.dumpData(targetDumpService);
			
			this.matchedDrugsMap = DrugSearch.compareDrugByBrand(drugBrand,originCountry,targetModel);
			Map<Double,MockDrug> sortedMatchedDrugs = new TreeMap<Double,MockDrug>(matchedDrugsMap);
			
			for(Map.Entry<Double,MockDrug> entry : sortedMatchedDrugs.entrySet()) {
				MockDrug drug = entry.getValue();
				Double  metric = entry.getKey();

				System.out.println(String.format("Metric: %s    Brand: %s      URI: %s",
						metric, drug.getBrand(), drug.getDrugURI()));
			}		
			return null;		
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
