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
import model.Manufacturer;
import util.FusekiConnector;
import model.Category;
import model.Country;

public class ObjectsToRDFConverter {
	//private final Model rdfModel;
	private final OntologyManager ontologyManager;
	private final String namespaceIRI;
		
	public ObjectsToRDFConverter(String baseURI, OntologyManager ontologyManager) {
		this.namespaceIRI = baseURI;
		this.ontologyManager = ontologyManager;
	}
	
	public Model convertDataToRDF(CSVDataReader csvDataReader, Country country) throws FileNotFoundException {
        OntClass drugClass = ontologyManager.findClass("Drug");
        OntClass manufacturerClass = ontologyManager.findClass("Manufacturer");
        OntClass ingredientClass = ontologyManager.findClass("Ingredient");
        OntClass categoryClass = ontologyManager.findClass("Category");        
        OntProperty nameProperty = ontologyManager.findProperty("name");
        OntProperty brandName = ontologyManager.findProperty("brandName");
        OntProperty hasCompound = ontologyManager.findProperty("hasCompound");
        OntProperty isFromCategory = ontologyManager.findProperty("isFromCategory");
		OntProperty hasStrength = ontologyManager.findProperty("hasStrength");            	
		OntProperty manufacturedBy = ontologyManager.findProperty("manufacturedBy");    	
		
		Model rdfModel = ModelFactory.createDefaultModel();
		
		//Trying to get the whole graph from the country, if exist 
		FusekiConnector fusekiConnector = new FusekiConnector();
		String countryDumpService = fusekiConnector.getDatasetDetailsByCountry(country).get("dumpService");
		rdfModel = fusekiConnector.dumpData(countryDumpService);
		rdfModel.setNsPrefixes(ontologyManager.getOntologyModel().getNsPrefixMap());		
		
		//Converting objects from Manufacturer class to resources
        for(Manufacturer m: csvDataReader.manufacturerList) {
        	String instanceURI = String.format("%smanufacturer/%s",namespaceIRI, m.getId());
        	//Look for the instance in the Model. If does not exist, instantiate it
        	if (rdfModel.getResource(instanceURI) == null) {
        		Resource manufacturer = rdfModel.createResource(instanceURI,manufacturerClass);        	
        		manufacturer.addProperty(nameProperty, m.getName());        	
        	}
        }
        
        //Converting objects from Active Ingredient class to resources
        for(ActiveIngredient ai: csvDataReader.activeIngredientsList) {
        	
        	//Query the model for any ingredient with the same name of ai instance
        	String queryString = 
        			String.format("PREFIX pharm: <%s>\n",ontologyManager.getOntologyIRI()) + 
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
        		ingredient.addProperty(nameProperty, ai.getName());
        	}
        	
        }      
        
        //Converting objects from Category class to resources
        for(Category c: csvDataReader.categoryList) {
        	String instanceURI = String.format("%scategory/%s",namespaceIRI,c.getId());
        	Resource category = rdfModel.createResource(instanceURI,categoryClass);        	
        	category.addProperty(nameProperty, c.getName());
        }      
        
        //Converting objects from Drug class to resources and creating relationships between resources
        for(Drug d: csvDataReader.drugsList) {        	
        	
        	if (d.getDrugCode() == null) {
        		return null;
        	}
        	
    		String instanceURI = String.format("%sdrug/%s", namespaceIRI,d.getDrugCode());
    		
    		//Obtain the drug from the model, if it already exists
    		//If it does not exist, it is created as result of the method getResource
    		Resource drug = rdfModel.getResource(instanceURI);
   			drug = rdfModel.createResource(instanceURI,drugClass);

        	if (d.getBrandName() != null) {        		
            	drug.addProperty(brandName, d.getBrandName());
        	}
        	
        	if (d.getStrength() != null){
        		drug.addProperty(hasStrength, d.getStrength());
        	}
        	
        	if (d.getManufacturer() != null) {
        		Resource manufacturer = rdfModel.getResource(String.format("%smanufacturer/%s", namespaceIRI,d.getManufacturer().getId()));
            	drug.addProperty(manufacturedBy,manufacturer);
        	}
        	
        	if (d.getActiveIngredients() != null && d.getActiveIngredients().size() > 0) {
        		
        		Seq ingredientsSeq = rdfModel.createSeq();
        		for (ActiveIngredient ingredientObject : d.getActiveIngredients()) {        			
                	Resource ingredientResource = rdfModel.getResource(String.format("%singredient/%s", namespaceIRI,ingredientObject.getId()));                	
                	ingredientsSeq.add(ingredientResource);                	
        		}
        		
        		drug.addProperty(hasCompound, ingredientsSeq);
        		
        	}
        	
        	if (d.getCategory() != null) {            	
            	Resource category = rdfModel.getResource(String.format("%scategory/%s",namespaceIRI,d.getCategory().getId()));
            	drug.addProperty(isFromCategory, category);
        	}
        }
 
		return rdfModel;
	}
   	

}
