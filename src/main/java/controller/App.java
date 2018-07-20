package controller;

import java.io.FileNotFoundException;

import org.apache.jena.rdf.model.Model;

public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
    	String path = "./resources/data/brazilian-dataset-anvisa.tsv";

    	ObjectsToRDFConverter converter = new ObjectsToRDFConverter();
    	RDFManager manager = new RDFManager();
    	
    	Model rdfModel = converter.convertDataToRDF(path);

    	//RDF/XML syntax
    	//manager.systemOutput(rdfModel, "RDF/XML");
    	//manager.saveFile(rdfModel, "./resources/data/drugs.rdf", "rdf/xml");
    	
    	//Turtle syntax
    	manager.systemOutput(rdfModel, "turtle");
    	manager.saveFile(rdfModel, "./resources/data/drugs.rdf", "turtle");
    	
    	
    	//JSON-LD syntax
    	//manager.systemOutput(rdfModel, "JSON-LD");
    	//manager.saveFile(rdfModel, "./resources/data/drugs.rdf", "JSON-LD");
    	
    }
}
