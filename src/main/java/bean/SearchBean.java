package bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.apache.jena.rdf.model.Model;

import controller.DrugSearch;
import model.MockDrug;
import util.FusekiConnector;
import util.SparqlQuery;


@SuppressWarnings("serial")
@ManagedBean (name = "searchBean")
@ViewScoped
public class SearchBean implements Serializable{
	private String originCountry;
	private String targetCountry;
	private String currentTargetCountry;
	private String drugBrand;
	private Model targetModel;
	private List<String> brandList;
	private Set<Entry<MockDrug,Double>> matchedDrugs;
	private Map<String,String> registeredCountries;
	
	@PostConstruct 
	public void Init() {
		this.registeredCountries = SparqlQuery.getRegisteredCountries();
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
	
	public void onOriginCountryChange(ValueChangeEvent event){
			//String contryURI = event.getNewValue().toString();
			//this.brandList = DrugSearch.getOriginDrugBrands(contryURI);

	}
	
	public void onTargetCountryChange(ValueChangeEvent event){
		this.currentTargetCountry = event.getNewValue().toString();
	
	}
	
	public List<String> getBrandList() {
		return brandList;
	}
	
	public void setBrandList(List<String> brandList) {
		this.brandList = brandList;
	}
	
	public Map<String, String> getRegisteredCountries() {
		return registeredCountries;
	}

	public void setRegisteredCountries(Map<String, String> registeredCountries) {
		this.registeredCountries = registeredCountries;
	}

	public String search() throws Exception  {
		try {
			this.matchedDrugs = null;
			FusekiConnector fusekiConnector = new FusekiConnector();
			
			//Dumping target country model
			if(this.currentTargetCountry != this.targetCountry) {
				HashMap<String, String> targetCountryServices = fusekiConnector.getDatasetDetailsByCountry(this.targetCountry);
				String targetDumpService = targetCountryServices.get("dumpService");
				this.targetModel = fusekiConnector.dumpData(targetDumpService);
				
				String drugURI = SparqlQuery.queryURIByBrand(drugBrand, originCountry);
				this.matchedDrugs = DrugSearch.compareDrugByBrand(drugURI,originCountry,targetModel);
				
			}
			
			return null;		
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
