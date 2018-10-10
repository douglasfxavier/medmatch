package util;

import java.util.HashMap;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

public class SparqlQuery {
		
		public static ResultSet queryIngredientsOfOneDrug(String originDrugBand, 
									String originCountry) {

			FusekiConnector fusekiConnector = new FusekiConnector();
			
			//Query the model looking for the drug buy the brand (selectedBrand)
			//And retrieve the ingredients of its compound
	    	String queryString =
	    					"PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
	    					"PREFIX  pharm: <http://medmatch.global/ontology/pharmacology#>\n" + 
	    					"PREFIX  lifesci: <https://health-lifesci.schema.org/>\n" +
	    					"SELECT ?drugURI ?brand ?seq ?ingredientName\n" + 
	    					"WHERE {\n" + 
	    					"  			?drugURI rdf:type lifesci:Drug ;\n" + 
	    					"       			       pharm:brand ?brand ;\n" + 
	    					"   			  pharm:hasCompound ?compound .\n" + 
	    					"   			 ?compound ?seq ?ingredient .\n" + 
	    					"  		      ?ingredient pharm:name ?ingredientName .\n" + 
	    					String.format("FILTER (regex(?brand,\"%s\",\"i\"))\n",originDrugBand) + 
	    					"}"; 
	    				
			HashMap<String, String> originCountryServices = fusekiConnector.getDatasetDetailsByCountry(originCountry);
			String originSparqlService = originCountryServices.get("sparqlService");

	    	Query query = QueryFactory.create(queryString) ;    	
	    	QueryExecution qexec = QueryExecutionFactory.sparqlService(originSparqlService, query);
	    	ResultSet originDrugsIterator = qexec.execSelect();
					
	    	return originDrugsIterator;
    	
		}

		public static ResultSet queryAllDrugsWithIngredients(Model targetModel) {
		
			//Query the model looking for all the drug buy from the model that 
			//has a brand, compound with seq and ingredients
			//Retrieve the ingredients of its compound
			String queryString =
				"PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
				"PREFIX  pharm: <http://medmatch.global/ontology/pharmacology#>\n" + 
				"PREFIX  lifesci: <https://health-lifesci.schema.org/>\n" +
				"SELECT DISTINCT ?drugURI\n" + 
				"WHERE {\n" + 
				"  			?drugURI rdf:type lifesci:Drug ;\n" + 
				"       			       pharm:brand ?brand ;\n" + 
				"   			  pharm:hasCompound ?compound .\n" + 
				"   			 ?compound ?seq ?ingredient .\n" + 
				"}"; 
					
			Query query = QueryFactory.create(queryString) ;    	
			QueryExecution qexec = QueryExecutionFactory.create(query,targetModel);
			ResultSet targetDrugsIterator = qexec.execSelect();
					
			return targetDrugsIterator;

		}
		
		
}
