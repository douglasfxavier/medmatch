package controller;

import java.io.FileNotFoundException;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
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
        OntClass ingredientClass = ontologyManager.findClass("Ingredient");
        OntClass drugClass_Class = ontologyManager.findClass("DrugClass");        
        OntProperty nameProp = ontologyManager.findProperty("name");
        OntProperty brandProp = ontologyManager.findProperty("brand");
        OntProperty hasCompound = ontologyManager.findProperty("hasCompound");
        OntProperty ofDrugClass = ontologyManager.findProperty("ofDrugClass");
		OntProperty strength = ontologyManager.findProperty("strength");            	
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
        	String queryString = 
        			String.format("PREFIX pharm: <%s>\n",ontologyManager.getOntologyNamespace()) + 
        						  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" + 
        						  "ASK" + 
        						  "WHERE {\n" + 
        						  "  ?ingredient rdf:type pharm:Ingredient;\n" + 
        							 String.format("pharm:name \"%s\" .\n",ai.getName()) + 
        						  "}\n"; 
        				
        	Query query = QueryFactory.create(queryString) ;
        	QueryExecution qexec = QueryExecutionFactory.create(query,rdfModel);
        	boolean result = qexec.execAsk();
        	qexec.close();

        	//if no ingredient with the same name of ai instance was found in the model,
        	//create a new ingredient based on ai instance
        	if (!result) {
        		String instanceURI = String.format("%singredient/%s",namespaceIRI,ai.getId());
        		Resource ingredient = rdfModel.createResource(instanceURI,ingredientClass);        	
        		ingredient.addProperty(nameProp, ai.getName());
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
        		if (drug.getPropertyResourceValue(strength) == null)
        			drug.addProperty(strength, d.getStrength());
        	}
        	
        	if (d.getManufacturer() != null) {
        		if (drug.getPropertyResourceValue(hasManufacturerProp) == null) {
        			instanceURI = String.format("%smanufacturer/%s",namespaceIRI,d.getManufacturer().getId());
        			Resource manufacturer = rdfModel.getResource(instanceURI);
        			drug.addProperty(hasManufacturerProp,manufacturer);
        		}
        	}
        	
        	if (d.getActiveIngredients() != null && d.getActiveIngredients().size() > 0) {
        		
        		if (drug.getPropertyResourceValue(hasCompound) == null) {
	        		instanceURI = String.format("%scompound/%s", namespaceIRI,d.getDrugCode());
        			Seq ingredientsSeq = rdfModel.getSeq(instanceURI);
	        		
	        		for (ActiveIngredient ingredientObject : d.getActiveIngredients()) {        			
	                	instanceURI = String.format("%singredient/%s", namespaceIRI,ingredientObject.getId());
	        			Resource ingredientResource = rdfModel.getResource(instanceURI);                	
	                	ingredientsSeq.add(ingredientResource);                	
	        		}
	        		
	        		drug.addProperty(hasCompound, ingredientsSeq);
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
