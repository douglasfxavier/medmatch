package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Set;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import model.MockDrug;
import util.Functions;
import util.MapUtil;
import util.SparqlQuery;

public class DrugSearch {
	
	public static Set<Entry<MockDrug,Double>> compareDrugByBrand(String originDrugURI,
														 String originCountry,
														 Model targetModel) {
		
		OntologyManager ontologyManager = new OntologyManager();
		Property hasFormulationProp = ontologyManager.findProperty("hasFormulation");
		Property nameProp = ontologyManager.findProperty("name");
		Property brandProp = ontologyManager.findProperty("brand");
		Property strengthValueProp = ontologyManager.findProperty("strengthValue");
		
		
		Map<MockDrug,Double> finalMetrics = new HashMap<MockDrug,Double>();		
		
		ResultSet originQueryIterator = SparqlQuery.queryIngredientsOfOneDrug(originDrugURI, originCountry);
		Map<Integer,String> originIngredients = new HashMap<Integer,String>();
		
		//Iterate the query solution for the origin ingredients and store the ingredient names in one map  
		while(originQueryIterator.hasNext()) {
			int originIndex = originQueryIterator.getRowNumber() + 1;
			String ingredientName = originQueryIterator.next().get("activeIngredientName").toString(); 
			originIngredients.put(originIndex, ingredientName);
		}
		
		
		int numberOfOriginIngredients = originIngredients.size();
		double weight = Functions.weightByNumberOfElements(numberOfOriginIngredients);

		ResIterator targetDrugs = targetModel.listResourcesWithProperty(hasFormulationProp);
		
		//Comparison between the ingredients from both drugs		
		while(targetDrugs.hasNext()) {
			Double meanOfDrug = 0.00;
			Resource targetDrug = targetDrugs.next();
			Statement brandResource = targetModel.getProperty(targetDrug,brandProp);
			Map<String,Double> ingredientsByDrug = new HashMap<String,Double>();
			
			
			//In case there is a drug resource with no brand name, go to next
			if (brandResource == null)
				continue;
			
			String targetBrandName = brandResource.getObject().toString();
			String targetStrength = targetDrug.getProperty(strengthValueProp).getObject().toString();
			Resource targetFormulation = targetDrug.listProperties(hasFormulationProp).next().getObject().asResource();
			StmtIterator targetIngredientsIterator = targetFormulation.listProperties();
			
			//In case, there are no ingredients for the current drug, got to next
			if (targetIngredientsIterator == null)
				continue;
			
			MockDrug drugObject = new MockDrug();
			drugObject.setDrugURI(targetDrug.getURI());
			drugObject.setBrand(targetBrandName);
			drugObject.setStrength(targetStrength);
			
			while(targetIngredientsIterator.hasNext()) {
				Statement targetIngredientStatemennt = targetIngredientsIterator.next();
				int targetPosition = targetIngredientStatemennt.getPredicate().getOrdinal();
				
				if (targetPosition > numberOfOriginIngredients)
					break;
				
				Resource targetIngredient = targetIngredientStatemennt.getObject().asResource(); 
				String targetIngredientName = targetIngredient.getProperty(nameProp).getObject().toString(); 
				drugObject.addIngredient(targetIngredientName);
				
				//Iterate the ingredients of the drugs from the origin country
				
				for(Map.Entry<Integer, String> ingredient : originIngredients.entrySet()) {
					int originPosition = ingredient.getKey();
				
					String originIngredientName = ingredient.getValue();
				
					double metric = StringComparison.cosineSimilarity(originIngredientName, targetIngredientName);
					
					metric = metric + (weight * Math.abs(targetPosition-originPosition));
					
					if (ingredientsByDrug.get(targetIngredientName) != null 
							&& metric > ingredientsByDrug.get(targetIngredientName)) 
						continue;
					
					ingredientsByDrug.put(targetIngredientName,metric);
					
				}
			}
			
			for(Map.Entry<String, Double> entry : ingredientsByDrug.entrySet()) {
				meanOfDrug += entry.getValue();
			}
			
			meanOfDrug = meanOfDrug/ingredientsByDrug.size();
		
			if (meanOfDrug> 0.7 && meanOfDrug <= 1) {
				drugObject.setMetric(meanOfDrug);
				finalMetrics.put(drugObject,meanOfDrug);
			}
		}
		
		Map<MockDrug, Double> reversedSortedFinalMetrics = MapUtil.sortByValue(finalMetrics);
		
		return reversedSortedFinalMetrics.entrySet();
	}
	
	public static List<String> getOriginDrugBrands(String originCountryURI){
		List<String> drugsList = SparqlQuery.getDrugBrandsByCountry(originCountryURI);
		return drugsList;		
	}

}
