package controller;

import java.io.FileNotFoundException;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Seq;

import controller.OntologyManager;

import model.ActiveIngredient;
import model.Drug;
import model.ATCClass;
import model.Manufacturer;
import model.Country;

import util.FusekiConnector;
import util.SparqlQuery;



public class ObjectsToRDFConverter {
	private final OntologyManager ontologyManager;
	private final String namespaceIRI;
		
	public ObjectsToRDFConverter(String namespaceIRI, OntologyManager ontologyManager) {
		this.namespaceIRI = String.format("http://%s",namespaceIRI);
		this.ontologyManager = ontologyManager;
	}
	
	public Model convertDataToRDF(CSVDataReader csvDataReader, Country country) throws FileNotFoundException {
        OntClass drugClass = ontologyManager.findClass("Drug");
        OntClass manufacturerClass = ontologyManager.findClass("Manufacturer");
        OntClass activeIngredientClass = ontologyManager.findClass("ActiveIngredient");
        OntClass drugClass_Class = ontologyManager.findClass("DrugClass");        
        OntProperty nameProp = ontologyManager.findProperty("name");
        OntProperty brandProp = ontologyManager.findProperty("brand");
        OntProperty hasFormulationPro = ontologyManager.findProperty("hasFormulation");
        OntProperty ofDrugClass = ontologyManager.findProperty("ofDrugClass");
		OntProperty strengthValueProp = ontologyManager.findProperty("strengthValue");            	
		OntProperty hasManufacturerProp = ontologyManager.findProperty("hasManufacturer");    	
		
		Model rdfModel = ModelFactory.createDefaultModel();
		
		//Trying to get the whole graph from the country, if exist 
		FusekiConnector fusekiConnector = new FusekiConnector();
		String countryDumpService = fusekiConnector.getDatasetDetailsByCountry(country.getUri()).get("dumpService");
		rdfModel = fusekiConnector.dumpData(countryDumpService);
		rdfModel.setNsPrefixes(ontologyManager.getOntologyModel().getNsPrefixMap());
		
		//Converting objects from Manufacturer class to resources
        for(Manufacturer m: csvDataReader.manufacturerList) {
        	String instanceURI = String.format("%smanufacturer/%s",namespaceIRI, m.getId());
        	//Look for the instance in the Model. If does not exist, instantiate it
        	Resource manufacturer = rdfModel.createResource(instanceURI,manufacturerClass);        	
        	manufacturer.addProperty(nameProp, m.getName());        	
        }
        //Converting objects from Active Ingredient class to resources
        for(ActiveIngredient ai: csvDataReader.activeIngredientsList) {
        	
        	//Query the model for any ingredient with the same name of ai instance
        	//if no ingredient with the same name of ai instance was found in the model,
        	//create a new ingredient based on ai instance
        	if (SparqlQuery.askActiveIngredienExist(ai.getName(), rdfModel) == false) {
        		String instanceURI = String.format("%sactiveIngredient/%s",namespaceIRI,ai.getId());
        		Resource activeIngredient = rdfModel.createResource(instanceURI,activeIngredientClass);        	
        		activeIngredient.addProperty(nameProp, ai.getName());
        	}
        	
        }      
        
        //Converting objects from Category class to resources
        for(ATCClass c: csvDataReader.atcClassList) {
/*        	WikiData wikiData = new WikiData();
        	ArrayList<ATCClass> allATCCodes = wikiData.getAllATCClasses();    */    	
        	
        	String instanceURI = String.format("%sdrugclass/%s",namespaceIRI,c.getCode());
        	Resource category = rdfModel.createResource(instanceURI,drugClass_Class);        	
        	if (c.getName() != null && c.getName().length() > 0) {
        		if (category.getPropertyResourceValue(nameProp) == null)
        			category.addProperty(nameProp, c.getName());
        	}
        }      
        
        //Converting objects from Drug class to resources and creating relationships between resources
        for(Drug d: csvDataReader.drugsList) {        	
        	
    		String instanceURI = String.format("%sdrug/%s", namespaceIRI,d.getDrugCode());
    		
    		//Obtain the drug from the model, if it already exists
    		//If it does not exist, it is created as result of the method getResource
    		Resource drug = rdfModel.createResource(instanceURI,drugClass);
   			

   			if (d.getBrand() != null) {   
   				if (drug.getPropertyResourceValue(brandProp) == null)
   					drug.addProperty(brandProp, d.getBrand());
        	}
        	
        	if (d.getStrength() != null){
        		if (drug.getPropertyResourceValue(strengthValueProp) == null)
        			drug.addProperty(strengthValueProp, d.getStrength());
        	}
        	
        	if (d.getManufacturer() != null) {
        		if (drug.getPropertyResourceValue(hasManufacturerProp) == null) {
        			instanceURI = String.format("%smanufacturer/%s",namespaceIRI,d.getManufacturer().getId());
        			Resource manufacturer = rdfModel.getResource(instanceURI);
        			drug.addProperty(hasManufacturerProp,manufacturer);
        		}
        	}
        	
        	if (d.getActiveIngredients() != null && d.getActiveIngredients().size() > 0) {
        		
        		if (drug.getPropertyResourceValue(hasFormulationPro) == null) {
	        		instanceURI = String.format("%sformulation/%s", namespaceIRI,d.getDrugCode());
        			Seq activeIngredientsSeq = rdfModel.getSeq(instanceURI);
	        		
	        		for (ActiveIngredient activeIngredientObject : d.getActiveIngredients()) {        			
	                	instanceURI = String.format("%sactiveIngredient/%s", namespaceIRI,activeIngredientObject.getId());
	        			Resource activeIngredientResource = rdfModel.getResource(instanceURI);                	
	                	activeIngredientsSeq.add(activeIngredientResource);                	
	        		}
	        		
	        		drug.addProperty(hasFormulationPro, activeIngredientsSeq);
        		}
        		
        	}
        	
        	if (d.getAtcClass() != null) {     
        		if (drug.getPropertyResourceValue(ofDrugClass) == null) {
        			instanceURI = String.format("%sdrugclass/%s",namespaceIRI,d.getAtcClass().getCode());
        			Resource drugClassResource = rdfModel.getResource(instanceURI);
        			drug.addProperty(ofDrugClass, drugClassResource);
        		}
        	}
        }
 
		return rdfModel;
	}
   	

}
