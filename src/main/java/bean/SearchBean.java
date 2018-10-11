package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
public class SearchBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Country> countryList;
	private String originCountry;
	private String targetCountry;
	private String drugBrand;
	private Model targetModel;
	private Set<Entry<Double, MockDrug>> matchedDrugs;
	
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
	
	public Set<Entry<Double, MockDrug>> getMatchedDrugs() {
		return matchedDrugs;
	}
	public void setMatchedDrugs(Set<Entry<Double, MockDrug>> matchedDrugs) {
		this.matchedDrugs = matchedDrugs;
	}
	
//	public List<MockDrug> getMatchedDrugs() {
//		List<MockDrug> drugCollection = null;
//		if (matchedDrugsMap != null && matchedDrugsMap.size() > 0) {
//			drugCollection = new ArrayList<>(matchedDrugsMap.values());
//		}
//		return drugCollection;
//	}
	

	public String search() throws Exception  {
		try {
			
			FusekiConnector fusekiConnector = new FusekiConnector();
									
			//Dumping target country model
			HashMap<String, String> targetCountryServices = fusekiConnector.getDatasetDetailsByCountry(targetCountry);
			String targetDumpService = targetCountryServices.get("dumpService");
			this.targetModel = fusekiConnector.dumpData(targetDumpService);
			
			this.matchedDrugs = DrugSearch.compareDrugByBrand(drugBrand,originCountry,targetModel);
			
			for(Map.Entry<Double,MockDrug> entry : matchedDrugs) {
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
