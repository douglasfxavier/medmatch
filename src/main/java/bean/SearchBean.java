package bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ValueChangeEvent;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import controller.DrugSearch;
import controller.OntologyManager;
import util.FusekiConnector;

@ManagedBean (name = "searchBean")
@RequestScoped
public class SearchBean {
	private Map<String,String> registeredCountries;
	private String selectedOriginCountry;
	private String selectedTargetCountry;
	//private ArrayList<String> drugNamesByCountry;
	private String selectedbrand;
	private OntologyManager ontologyManager;
	
	//private String activeIngredientInput;
	@PostConstruct 
	public void Init() {
		String ontologyIRI = "http://medmatch.global/ontology/pharmacology";
		this.ontologyManager = new OntologyManager("pharmacology.owl", ontologyIRI);
	}
	
	public String getSelectedOriginCountry() {
		return selectedOriginCountry;
	}
	
	public void setSelectedOriginCountry(String newValue) {
		this.selectedOriginCountry = newValue;
	}
	
	public String getSelectedTargetCountry() {
		return selectedTargetCountry;
	}
	
	public void setSelectedTargetCountry(String selectedTargetCountry) {
		this.selectedTargetCountry = selectedTargetCountry;
	}

	public String getSelectedbrand() {
		return selectedbrand;
	}

	public void setSelectedbrand(String newValue) {
		this.selectedbrand = newValue;
	}
	
	public ArrayList<String> drugBrandsByCountry(String newValue) {
		return DrugSearch.getDrugBrandsByCountry(selectedOriginCountry, this.ontologyManager);
	}

/*	public void setDrugNamesByCountry(ArrayList<String> newValue) {
		return newValue;
	}*/
	
	public Map<String, String> getRegisteredCountries() {
		FusekiConnector fc = new FusekiConnector();
		this.registeredCountries = fc.getRegisteredCountries();
		return this.registeredCountries;
	}

	public ArrayList<String> onCountrySelected(ValueChangeEvent event) {
		if (selectedOriginCountry !=null && !selectedOriginCountry.equals("")) {			 
			return DrugSearch.getDrugBrandsByCountry(selectedOriginCountry, this.ontologyManager);
		}else {
			return new ArrayList<>();
		}
	}

	public String search() throws IOException {
		
		FusekiConnector fusekiConnector = new FusekiConnector();
	
		HashMap<String, String> originCountryServices = fusekiConnector.getDatasetDetailsByCountry(selectedOriginCountry);
		String originCountryDumpService = originCountryServices.get("dumpService");
		Model originCountryDrugsModel = fusekiConnector.dumpData(originCountryDumpService);
				
		HashMap<String, String> targetCountryServices = fusekiConnector.getDatasetDetailsByCountry(selectedTargetCountry);
		String targetCountryDumpService = targetCountryServices.get("dumpService");
		Model targetCountryDrugsModel = fusekiConnector.dumpData(targetCountryDumpService);
				
		
		Property brandProp = this.ontologyManager.findProperty("brand");
		Map<Float,Resource> drugsMap = DrugSearch.compareDrugBybrand(selectedbrand, targetCountryDrugsModel, ontologyManager); 
				
		for(Map.Entry<Float,Resource> entry : drugsMap.entrySet()) {
			Float metric = entry.getKey();			
			Resource drug = entry.getValue();			
			//System.out.println(String.format("%s   %s", metric, drugURI));
			String drugbrand = drug.getPropertyResourceValue(brandProp).toString();
			System.out.println(String.format("%s   %s", metric, drugbrand));
		}
		
		return null;
	}
	

}
