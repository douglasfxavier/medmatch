package controller;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
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
import org.apache.jena.util.iterator.ExtendedIterator;

import com.github.andrewoma.dexx.collection.HashMap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;

import model.Country;
import util.CSVDelimiter;
import util.CountryList;
import util.Functions;
import util.FusekiConnector;

public class App 
{
    
	
	public static void main( String[] args ) throws FileNotFoundException
    {
			int n = 5;
			for(int i = 1; i<=n;i++) {
				System.out.println(Functions.weightByPosition(n,i));
			}

        	
//
//    	String ontologyIRI = "http://medmatch.global/ontology/pharmacology";
//    	OntologyManager om = new OntologyManager("pharmacology.owl", ontologyIRI);
//    	try {
//    		ExtendedIterator<OntClass> ontClassIterator = om.getOntologyModel().listClasses();
//			while(ontClassIterator.hasNext()){
//				OntClass ontClass = ontClassIterator.next();
//				if (ontClass.getLocalName() == null)
//					continue;					
//				System.out.println(ontClass);
//				
//			}
//			
//			ExtendedIterator<OntProperty> ontPropIterator = om.getOntologyModel().listAllOntProperties();
//			while(ontPropIterator.hasNext()){
//				OntProperty ontPro = ontPropIterator.next();		
//				if (ontPro.getLocalName() == null)
//					continue;
//				System.out.println(ontPro);
//				
//			}
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//    	/*
//    	String datasetEndpoint = String.format("http://localhost:3030/%s","Brazil");
//		String datasetDumpDataService = String.format("%s/data",datasetEndpoint);
//		String datasetSparqlService = String.format("%s/upload",datasetEndpoint);
//		String countryURI = "http://wikidata/country/Brazil"; 
//		String sparqlString = 
//				  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
//				  "PREFIX void: <http://rdfs.org/ns/void#>\n" +
//				  "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +		  
//				  "INSERT DATA {\n" + 
//				  	String.format("<%s> rdf:type void:Dataset;\n", datasetEndpoint) +
//				  	String.format("void:dataDump <%s>;\n", datasetDumpDataService) +
//				  	String.format("void:sparqlEndpoint <%s>;\n", datasetSparqlService) +
//				  	String.format("wdt:P17 <%s> .\n", countryURI) + 
//				  "}";
//		
//		System.out.println(sparqlString);
//    	
//		String str1 = "ACEPONATO DE METILPREDNISOLONA";
//		String str2 = "ACEPONATO DA METILPREDNISOLONA";
//		System.out.println(str1 + " vs. " + str2);
//    	System.out.println(StringMatching.calculateEuclideanDistance(str1,str2));
//    	
//		str1 = "METHYLPREDNISOLONE ACETATE";
//		str2 = "METHYLPREDNISOLONE ACETATE";
//		System.out.println(str1 + " vs. " + str2);
//    	System.out.println(StringMatching.euclideanDistance(str1,str2));
//    	
//		str1 = "ACEPONATO DE METILPREDNISOLONA";
//		str2 = "METHYLPREDNISOLONE ACETATE";
//		System.out.println(str1 + " vs. " + str2);
//    	System.out.println(StringMatching.euclideanDistance(str1,str2));
//    	
//		str1 = "ACEPONATO DE METILPREDNISOLONA";
//		str2 = "Hydrocortisone aceponate";
//		System.out.println(str1 + " vs. " + str2);
//    	System.out.println(StringMatching.euclideanDistance(str1,str2));
//    	
//		str1 = "ACEPONATO DE METILPREDNISOLONA";
//		str2 = "METHYLPREDNISOLONE";
//		System.out.println(str1 + " vs. " + str2);
//    	System.out.println(StringMatching.euclideanDistance(str1,str2));
//    	
//		str1 = "ACEPONATO DE METILPREDNISOLONA";
//		str2 = "METHYLPREDNISOLONE (METHYLPREDNISOLONE SODIUM SUCCINATE)"; 
//		System.out.println(str1 + " vs. " + str2);
//    	System.out.println(StringMatching.calculateEuclideanDistance(str1,str2));
//    	
//		str1 = "ACEPONATO DE METILPREDNISOLONA";
//		str2 = "ACEPONATO ABCDEFGHIJL METILPREDNISOLONA "; 
//		System.out.println(str1 + " vs. " + str2);
//    	System.out.println(StringMatching.euclideanDistance(str1,str2));
//    	
//    	str1 = "ACEPONATO DE METILPREDNISOLONA";
//		str2 = "ABCDEFGHIJL PREDNISOLONE "; 
//		System.out.println(str1 + " vs. " + str2);
//    	System.out.println(StringMatching.euclideanDistance(str1,str2));*/
    	
    	
/*    	String sparqlString = "PREFIX dc: <http://purl.org/dc/elements/1.1/>" + 
		   					  "INSERT DATA" + 
		   					  "{ GRAPH <http://example/g2> {" + 
		   					  "<http://example/book1> dc:title \"A new book\";" + 
		   					  "dc:creator \"A.N.Other\" } ." + 
		   					 "}";
    	
		FusekiConnector fc = new FusekiConnector();
		fc.update("http://localhost:3030/test/update", sparqlString);*/

			

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
