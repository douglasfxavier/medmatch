package controller;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.engine.binding.Binding;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sparql.modify.UpdateEngine;
import org.apache.jena.sparql.modify.UpdateEngineFactory;
import org.apache.jena.sparql.util.Context;
import org.apache.jena.util.FileManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import model.Country;
import util.CSVDelimiter;
import util.CountryList;
import util.FusekiConnector;

public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
    	String sparqlString = "PREFIX dc: <http://purl.org/dc/elements/1.1/>" + 
		   					  "INSERT DATA" + 
		   					  "{ GRAPH <http://example/g2> {" + 
		   					  "<http://example/book1> dc:title \"A new book\";" + 
		   					  "dc:creator \"A.N.Other\" } ." + 
		   					 "}";
    	
		FusekiConnector fc = new FusekiConnector();
		fc.update("http://localhost:3030/test/update", sparqlString);

			

    	//System.out.println("TAB: " + CSVDelimiter.valueOf("TAB").getDelimiter());
    	
    	
 /*   	CountriesListBean countriesListBean = new CountriesListBean();
    	ArrayList<Country> lista = new ArrayList<Country>();
    	
    	lista = countriesListBean.getAllCountries();
    	
    	for (Country c: lista) {
    		System.out.println(c.toString());
    	}*/
    	
/*    	String path = "./src/main/resources/data/brazilian-dataset-anvisa.tsv";

    	ObjectsToRDFConverter converter = new ObjectsToRDFConverter();
    	RDFManager manager = new RDFManager();
    	
    	Model rdfModel = converter.convertDataToRDF(path);

    	//RDF/XML syntax
    	//manager.systemOutput(rdfModel, "RDF/XML");
    	//manager.saveFile(rdfModel, "./resources/data/drugs.rdf", "rdf/xml");
    	
    	//Turtle syntax
    	manager.systemOutput(rdfModel, "turtle");
    	manager.saveFile(rdfModel, "./src/main/resources/data/drugs.rdf", "turtle");
    	
    	
    	//JSON-LD syntax
    	//manager.systemOutput(rdfModel, "JSON-LD");
    	//manager.saveFile(rdfModel, "./resources/data/drugs.rdf", "JSON-LD");
*/    	
    }
}
