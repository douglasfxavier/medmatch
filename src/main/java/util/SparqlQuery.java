package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.query.ResultSetRewindable;
import org.apache.jena.rdf.model.Model;

public class SparqlQuery {
		
		public static Boolean askActiveIngredienExist(String activeIngedientName,Model model) {
			String queryString = 
        			"PREFIX pharm: <http://medmatch.global/ontology/pharmacology#>\n" + 
        			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
        			"ASK" + 
        			"WHERE {\n" + 
        			"  ?activeIngredient rdf:type pharm:ActiveIngredient;\n" + 
        			String.format("pharm:name \"%s\" .\n",activeIngedientName) + 
        			"}\n";
			Query query = QueryFactory.create(queryString) ;
        	QueryExecution qexec = QueryExecutionFactory.create(query,model);
        	boolean result = qexec.execAsk();
        	
        	qexec.close();
        	
			return result; 
		}
	
		public static String queryURIByBrand(String drugBrand, String countrURI) {

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
						"       			       pharm:brand ?brand .\n" + 
						String.format("FILTER (regex(?brand,\"%s\",\"i\"))\n",drugBrand) + 
						"} LIMIT 1"; 
					
			HashMap<String, String> originCountryServices = fusekiConnector.getDatasetDetailsByCountry(countrURI);
			String originSparqlService = originCountryServices.get("sparqlService");
			
			Query query = QueryFactory.create(queryString) ;    	
			QueryExecution qexec = QueryExecutionFactory.sparqlService(originSparqlService, query);
			ResultSet resultSet = qexec.execSelect();
			String drugURI = resultSet.next().get("drugURI").toString();
			qexec.close();
			
			return drugURI;

	}

		public static ResultSet queryIngredientsOfOneDrug(String originDrugURI, 
									String originCountry) {

			FusekiConnector fusekiConnector = new FusekiConnector();
			
			//Query the model looking for the drug buy the brand (selectedBrand)
			//And retrieve the ingredients of its compound
	    	String queryString =
	    					"PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
	    					"PREFIX  pharm: <http://medmatch.global/ontology/pharmacology#>\n" + 
	    					"PREFIX  lifesci: <https://health-lifesci.schema.org/>\n" +
	    					"SELECT ?drugURI ?brand ?seq ?activeIngredientName\n" + 
	    					"WHERE {\n" + 
	    					"  			?drugURI rdf:type lifesci:Drug ;\n" + 
	    					"       			       pharm:brand ?brand ;\n" + 
	    					"   			  pharm:hasFormulation ?formulation.\n" + 
	    					"   			 ?formulation ?seq ?activeIngredient .\n" + 
	    					"  		      ?activeIngredient pharm:name ?activeIngredientName .\n" + 
	    					String.format("FILTER (?drugURI = <%s>)\n",originDrugURI) + 
	    					"}"; 
	    				
			HashMap<String, String> originCountryServices = fusekiConnector.getDatasetDetailsByCountry(originCountry);
			String originSparqlService = originCountryServices.get("sparqlService");

	    	Query query = QueryFactory.create(queryString) ;    	
	    	QueryExecution qexec = QueryExecutionFactory.sparqlService(originSparqlService, query);
	    	ResultSet resultSet = qexec.execSelect();
	    	ResultSetRewindable originDrugsIterator = ResultSetFactory.copyResults(resultSet);
	    	
	    	qexec.close();
	    	return originDrugsIterator;
    	
		}
		
		public static ResultSet queryDrugsbyBrand(String originDrugBand, 
				String originCountryURI) {

			FusekiConnector fusekiConnector = new FusekiConnector();
			
			//Query the model looking for the drug buy the brand (selectedBrand)
			//And retrieve the ingredients of its compound
			String queryString =
					"PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
					"PREFIX  pharm: <http://medmatch.global/ontology/pharmacology#>\n" + 
					"PREFIX  lifesci: <https://health-lifesci.schema.org/>\n" +
					"SELECT ?drugURI ?brand" + 
					"WHERE {\n" + 
					"  			?drugURI rdf:type lifesci:Drug ;\n" + 
					"       			       pharm:brand ?brand .\n" + 
					String.format("FILTER (regex(?brand,\"%s\",\"i\"))\n",originDrugBand) + 
					"}"; 
				
			HashMap<String, String> originCountryServices = fusekiConnector.getDatasetDetailsByCountry(originCountryURI);
			String originSparqlService = originCountryServices.get("sparqlService");
			
			Query query = QueryFactory.create(queryString) ;    	
			QueryExecution qexec = QueryExecutionFactory.sparqlService(originSparqlService, query);
			ResultSet resultSet = qexec.execSelect();
			ResultSetRewindable originDrugsIterator = ResultSetFactory.copyResults(resultSet);
			
			qexec.close();
			
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
				"   			  pharm:hasFormulation ?formulation .\n" + 
				"   			 ?formulation ?seq ?activeIngredient .\n" + 
				"}"; 
					
			Query query = QueryFactory.create(queryString) ;    	
			QueryExecution qexec = QueryExecutionFactory.create(query,targetModel);
			ResultSet resultSet = qexec.execSelect();
			ResultSetRewindable targetDrugsIterator = ResultSetFactory.copyResults(resultSet);
			qexec.close();
			
			return targetDrugsIterator;

		}
		
		public static List<String> getDrugBrandsByCountry(String countryURI){
			
			FusekiConnector fusekiConnector = new FusekiConnector();
			
			//Query the model looking for all drug brands from the country
			String queryString = 
"					PREFIX  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>  \r\n" + 
"					PREFIX  pharm: <http://medmatch.global/ontology/pharmacology#>  \r\n" + 
"					PREFIX  lifesci: <https://health-lifesci.schema.org/> \r\n" + 
"					SELECT DISTINCT ?brand  \r\n" + 
"					WHERE {  \r\n" + 
"					  			?drugURI rdf:type lifesci:Drug ;  \r\n" + 
"					       			       pharm:brand ?brand .  \r\n" + 
"					} ";
				
			HashMap<String, String> countryServices = fusekiConnector.getDatasetDetailsByCountry(countryURI);
			String countrySparqlService = countryServices.get("sparqlService");
			
			Query query = QueryFactory.create(queryString) ;    	
			QueryExecution qexec = QueryExecutionFactory.sparqlService(countrySparqlService, query);
			ResultSet drugsIterator = qexec.execSelect();
			
			List<String> drugsList = new ArrayList<String>();
			
			while (drugsIterator.hasNext()) {
				QuerySolution drug = drugsIterator.next();
				String brand = drug.getLiteral("brand").getString(); 
				drugsList.add(brand);
			}
			qexec.close();
			return drugsList;
			
		}
}
