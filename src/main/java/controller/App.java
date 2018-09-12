package controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;

import model.Country;
import util.CSVDelimiter;
import util.CountryList;
import util.WikiDataCountry;

public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
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
