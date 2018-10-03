package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.shared.PrefixMapping;

import util.FusekiConnector;

public class DrugSearch {
	
	public static Model getDrugsModelByCountry(String countryURI) {
		FusekiConnector fusekiConnector = new FusekiConnector();
		HashMap<String, String> countryServices = fusekiConnector.getDatasetDetailsByCountry(countryURI);
		String datasetDumpService = countryServices.get("dumpService");
		Model drugsModel = fusekiConnector.dumpData(datasetDumpService);
		
		return drugsModel;
	}	
	
/*	public static ArrayList<String> getDrugBrandsByCountry(String countryURI, OntologyManager ontologyManager){
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
	}*/
	
	public static Map<Float,Resource> compareDrugByBrand(String selectedBrand,
														 Model modelDrugA,
														 Model modelDrugB, 
														 OntologyManager ontologyManager) {
		OntProperty hasCompound = ontologyManager.findProperty("hasCompound");
		OntProperty brand = ontologyManager.findProperty("brand");
		OntProperty name = ontologyManager.findProperty("name");
		ResIterator modelDrugBIterator = modelDrugB.listResourcesWithProperty(hasCompound);
		System.out.println("Chegou aqui. Drug Search Linha 65");
    	//Query the model looking for the drug buy the brand (selectedBrand)
		//And retrieve the ingredients of it compound
    	String queryString = 
    			String.format(
    					"SELECT ?drugURI ?brand ?seq ?ingredientName\n" + 
    					"WHERE {\n" + 
    					"  			?drugURI rdf:type lifesci:Drug ;\n" + 
    					"       			       pharm:brand ?brand ;\n" + 
    					"   			  pharm:hasCompound ?compound .\n" + 
    					"   			 ?compound ?seq ?ingredient .\n" + 
    					"  		      ?ingredient pharm:name ?ingredientName .\n" + 
    					"\n"+ 
    					String.format("FILTER (?brand = \"%s\")\n",selectedBrand) + 
    					"} "); 
    				
    	Query query = QueryFactory.create(queryString) ; 
    	query.setPrefixMapping((PrefixMapping) modelDrugA.getNsPrefixMap()); 
    	QueryExecution qexec = QueryExecutionFactory.create(query,modelDrugA);
    	ResultSet modelDrugAIterator = qexec.execSelect();
    	qexec.close();
    	
		//NodeIterator compounds = rdfModel.listObjectsOfProperty(hasCompound);
		Map<Float,Resource> metricsMap = new HashMap<Float, Resource>();		
		Map<Float,Resource> treeMap = new TreeMap<>();
		
		//Iterate the statements of Drug B
		System.out.println("Ingredient A            Ingredient B        Metric");
		while(modelDrugBIterator.hasNext()) {
			Resource drug = modelDrugBIterator.next();
			
			if (drug.getProperty(brand) == null) {
				continue;
			}
			
			Resource compound = drug.getPropertyResourceValue(hasCompound);
			StmtIterator drugIngredients = compound.listProperties();
			
			//Interate the ingredients of Drug B
			while(drugIngredients.hasNext()) {
				Statement ingredientStatement	= drugIngredients.next();
				String ingredientURI = ingredientStatement.getObject().toString();
				Resource ingredientResource = modelDrugB.getResource(ingredientURI);
				String ingredientNameDrugB = ingredientResource.getProperty(name).getObject().toString();
				
				//Iterate ingrediente of Drug A
				while (modelDrugAIterator.hasNext()) {
					QuerySolution row = modelDrugAIterator.next();
					String ingredientNameDrugA = row.get("ingredientName").toString();
					
					Float metric = StringComparison.euclideanDistance(ingredientNameDrugA, ingredientNameDrugB);
					
					System.out.println(String.format("%s             %s           %s",
							ingredientNameDrugA,ingredientNameDrugB, metric));
					
					if (metric >=0 && metric <=3) {
						metricsMap.put(metric,drug);
					}
				}
				
				treeMap = new TreeMap<>(metricsMap);
			}
		}
		return treeMap;
	}

}
