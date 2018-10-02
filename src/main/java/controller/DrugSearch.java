package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import util.FusekiConnector;

public class DrugSearch {
	
	public static Model getDrugsModelByCountry(String countryURI) {
		FusekiConnector fusekiConnector = new FusekiConnector();
		HashMap<String, String> countryServices = fusekiConnector.getDatasetDetailsByCountry(countryURI);
		String datasetDumpService = countryServices.get("dumpService");
		Model drugsModel = fusekiConnector.dumpData(datasetDumpService);
		
		return drugsModel;
	}	
	
	public static ArrayList<String> getDrugBrandsByCountry(String countryURI, OntologyManager ontologyManager){
		ArrayList<String> list = new ArrayList<>();
		Model drugsModel = getDrugsModelByCountry(countryURI);
		Property brandPro = ontologyManager.findProperty("brand");
		
		ResIterator drugsIterator = drugsModel.listResourcesWithProperty(brandPro);
		
		while(drugsIterator.hasNext()) {
			Resource drug = drugsIterator.next();
			
			if (drug.getProperty(brandPro) == null) {
				continue;
			}else {
				String brandNade = drug.getProperty(brandPro).getObject().toString();
				list.add(brandNade);
			}
		}	
		
		return list;
	}
	
	public static Map<Float,Resource> compareDrugBybrand(String brandDrugA, 
												Model modelDrugB, 
												OntologyManager ontologyManager) {
		OntProperty hasCompound = ontologyManager.findProperty("hasCompound");
		OntProperty brand = ontologyManager.findProperty("brand");
		OntProperty name = ontologyManager.findProperty("name");
		ResIterator drugsIterator = modelDrugB.listResourcesWithProperty(hasCompound);
		//NodeIterator compounds = rdfModel.listObjectsOfProperty(hasCompound);
		Map<Float,Resource> metricsMap = new HashMap<Float, Resource>();		
		Map<Float,Resource> treeMap = new TreeMap<>();
		
		while(drugsIterator.hasNext()) {
			Resource drug = drugsIterator.next();
			
			if (drug.getProperty(brand) == null) {
				continue;
			}
			
			Resource compound = drug.getPropertyResourceValue(hasCompound);
			StmtIterator drugIngredients = compound.listProperties();
			
			while(drugIngredients.hasNext()) {
				Statement ingredientStatement	= drugIngredients.next();
				String ingredientURI = ingredientStatement.getObject().toString();
				Resource ingredientResource = modelDrugB.getResource(ingredientURI);
				String ingredientNameDrugB = ingredientResource.getProperty(name).getObject().toString();
				
				Float metric = StringComparison.euclideanDistance(brandDrugA, ingredientNameDrugB);
				
				if (metric >=0 && metric <=3) {
					metricsMap.put(metric,drug);
				}
				
				treeMap = new TreeMap<>(metricsMap);
			}
		}
		return treeMap;
	}

}
