package util;

import java.util.ArrayList;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;

import model.Country;



public class WikiDataCountry {
	ArrayList<Country> allCountries = new ArrayList<Country>();

    public ArrayList<Country> getCountries() {
        try {
            String endpoint = "https://query.wikidata.org/sparql";

            String querySelect = "SELECT DISTINCT ?countryLabel \n" +
                    "                (?country as ?uri)\n" +
                    "                ?numericCode\n" +
                    "                ?alphaCode\n" +
                    "                ?language\n" +
                    "                ?web_code\n" +
                    "                WHERE { \n" +
                    "                       ?country wdt:P31 wd:Q3624078. \n" +
                    "                       ?country wdt:P299 ?numericCode. \n" +
                    "                       ?country wdt:P298 ?alphaCode.\n" +
                    "                       SERVICE wikibase:label { bd:serviceParam wikibase:language \"en\" . } \n" +
                    "                       FILTER(NOT EXISTS { ?country wdt:P31 wd:Q3024240. }) \n" +
                    "                       FILTER(NOT EXISTS { ?country wdt:P31 wd:Q28171280. }) \n" +
                    "                } \n" +
                    "               ORDER BY ?countryLabel";
            	Query query = QueryFactory.create();
            	query.setPrefix("wdt", "http://www.wikidata.org/prop/direct/");
            	query.setPrefix("wd", "http://www.wikidata.org/entity/");
            	query.setPrefix("wikibase", "http://wikiba.se/ontology#");
            	query.setPrefix("bd", "http://www.bigdata.com/rdf#");
            	QueryFactory.parse(query, querySelect, "", Syntax.syntaxSPARQL_11);
            	QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);
            	ResultSet result = qexec.execSelect();

            	while(result.hasNext()) {
            		QuerySolution row = result.next();
            		String uri = row.get("uri").toString();
            		String numericCode = row.get("numericCode").toString();
            		String countryName = row.get("countryLabel").toString().split("@")[0];
            		String alphaCode = row.get("alphaCode").toString();
            		
            		if (uri != null && numericCode != null && countryName != null) {
	            		Country newCountry = new Country(uri,Integer.parseInt(numericCode),countryName,alphaCode);
	            		this.allCountries.add(newCountry);
            		}
            	}
             
        }catch(Exception eex) {
            System.out.println(eex);
            eex.printStackTrace();
        }
		return allCountries;
    }
    
}
