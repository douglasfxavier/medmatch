package controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

import model.ActiveIngredient;
import model.Drug;
import model.Manufacturer;
import model.MockDrug;
import util.SparqlQuery;

public class DrugSearch {
	
	public static Map<Double,MockDrug>compareDrugByBrand(String originDrugBand,
														 String originCountry,
														 Model targetModel) {
		
		String ontologyIRI = "http://medmatch.global/ontology/pharmacology";
		OntologyManager ontologyManager = new OntologyManager("pharmacology.owl", ontologyIRI);
		Property hasCompound = ontologyManager.findProperty("hasCompound");
		Property hasManufacturer = ontologyManager.findProperty("hasManufacturer");
		Property name = ontologyManager.findProperty("name");
		Property brandProp = ontologyManager.findProperty("brand");
		Property strengthValue = ontologyManager.findProperty("strength");
		
		
		Map<Double,MockDrug> finalMetrics = new HashMap<Double,MockDrug>();		

		ResultSet originQueryIterator = SparqlQuery.queryIngredientsOfOneDrug(originDrugBand, originCountry);
		Map<Integer,String> originIngredients = new HashMap<Integer,String>();
		
		//Iterate the query solution for the origin ingredients and store the ingredient names in one map  
		while(originQueryIterator.hasNext()) {
			int originIndex = originQueryIterator.getRowNumber() + 1;
			String ingredientName = originQueryIterator.next().get("ingredientName").toString(); 
			originIngredients.put(originIndex, ingredientName);
		}
		

		ResIterator targetDrugs = targetModel.listResourcesWithProperty(hasCompound);
		
		//Comparison between the ingredients from both drugs		
		while(targetDrugs.hasNext()) {
			Resource targetDrug = targetDrugs.next();
			Statement brandResource = targetModel.getProperty(targetDrug,brandProp);
			
			//In case there is a drug resource with no brand name, go to next
			if (brandResource == null)
				continue;
			
			String brandName = brandResource.getObject().toString();
			String targetStrength = targetDrug.getProperty(strengthValue).getObject().toString();
			Resource targetFormulation = targetDrug.listProperties(hasCompound).next().getObject().asResource();
			StmtIterator targetIngredientsIterator = targetFormulation.listProperties();
			
			//In case, there are no ingredients for the current drug, got to next
			if (targetIngredientsIterator == null)
				continue;
			
			MockDrug drugObject = new MockDrug();
			drugObject.setDrugURI(targetDrug.getURI());
			drugObject.setBrand(brandName);
			drugObject.setStrength(targetStrength);
			
			while(targetIngredientsIterator.hasNext()) {
				Statement targetIngredientStatemennt = targetIngredientsIterator.next();
				Resource targetIngredient = targetIngredientStatemennt.getObject().asResource(); 
				String targetIngredientName = targetIngredient.getProperty(name).getObject().toString(); 
				drugObject.addIngredient(targetIngredientName);
				
				//Iterate the ingredients of the drugs from the origin country
				for(Map.Entry<Integer, String> ingredient : originIngredients.entrySet()) {
					int originPosition = ingredient.getKey();
					String originIngredientName = ingredient.getValue();
					
					double metric = StringComparison.levenshteinDistance(originIngredientName, targetIngredientName);
					metric = metric + (originPosition-1)/100;
					int numberOfOriginIngredients = originIngredients.size();
					double mean = metric/numberOfOriginIngredients; 
					drugObject.setMetric(mean);
					finalMetrics.put(mean,drugObject);

				}

			}
		}		
		return finalMetrics;
	}

}
