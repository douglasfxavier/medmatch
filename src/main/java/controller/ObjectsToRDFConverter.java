package controller;

import java.io.FileNotFoundException;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Seq;

import controller.OntologyManager;
import model.ActiveIngredient;
import model.Drug;
import model.Manufacturer;
import model.Category;

public class ObjectsToRDFConverter {
	private final Model rdfModel;
	private final OntModel ontologyModel;
	private final String baseURI;
		
	public ObjectsToRDFConverter(String baseURI, OntologyManager ontologyManager) {
		this.baseURI = baseURI;
		this.ontologyModel = ontologyManager.getOntologyModel();
		this.rdfModel = ModelFactory.createDefaultModel();
		this.rdfModel.setNsPrefix("pharm", ontologyManager.getOntologyNamespace());
		this.rdfModel.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
		this.rdfModel.setNsPrefix("dcterms", "http://purl.org/dc/terms/");	
	}
	
	public Model convertDataToRDF(CSVDataReader csvDataReader) throws FileNotFoundException {
        OntClass drugClass = OntologyManager.findClass("Drug", this.ontologyModel);
        OntClass manufacturerClass = OntologyManager.findClass("Manufacturer",this.ontologyModel);
        OntClass ingredientClass = OntologyManager.findClass("Ingredient", this.ontologyModel);
        OntClass categoryClass = OntologyManager.findClass("Category", this.ontologyModel);        
        OntProperty nameProperty = OntologyManager.findProperty("name", this.ontologyModel);
        OntProperty brandName = OntologyManager.findProperty("brandName", this.ontologyModel);
        OntProperty hasCompound = OntologyManager.findProperty("hasCompound", this.ontologyModel);
        OntProperty isFromCategory = OntologyManager.findProperty("isFromCategory", this.ontologyModel);
		OntProperty hasStrength = OntologyManager.findProperty("hasStrength", this.ontologyModel);            	
		OntProperty manufacturedBy = OntologyManager.findProperty("manufacturedBy", this.ontologyModel);    	
    	OntProperty containsIngredient = OntologyManager.findProperty("containsIngredient", this.ontologyModel);
        //Converting objects from Manufacturer class to resources
        for(Manufacturer m: csvDataReader.manufacturerList) {
        	String instanceURI = String.format("%smanufacturer/%s",baseURI, m.getId());
        	Resource manufacturer = rdfModel.createResource(instanceURI,manufacturerClass);        	
        	manufacturer.addProperty(nameProperty, m.getName());

        }
        
        //Converting objects from Active Ingredient class to resources
        for(ActiveIngredient ai: csvDataReader.activeIngredientsList) {
        	String instanceURI = String.format("%singredient/%s",baseURI,ai.getId());
        	Resource ingredient = rdfModel.createResource(instanceURI,ingredientClass);        	
        	ingredient.addProperty(nameProperty, ai.getName());
        }      
        
        //Converting objects from Category class to resources
        for(Category c: csvDataReader.categoryList) {
        	String instanceURI = String.format("%scategory/%s",baseURI,c.getId());
        	Resource category = rdfModel.createResource(instanceURI,categoryClass);        	
        	category.addProperty(nameProperty, c.getName());
        	
        }      
        
        //Converting objects from Drug class to resources and creating relationships between resources
        for(Drug d: csvDataReader.drugsList) {        	
        	
        	if (d.getDrugCode() == null) {
        		return null;
        	}

    		String instanceURI = String.format("%sdrug/%s", baseURI,d.getDrugCode()); 
    		Resource drug = rdfModel.createResource(instanceURI,drugClass);

        	if (d.getBrandName() != null) {        		
            	drug.addProperty(brandName, d.getBrandName());
        	}
        	
        	if (d.getStrength() != null){
        		drug.addProperty(hasStrength, d.getStrength());
        	}
        	
        	if (d.getManufacturer() != null) {
        		Resource manufacturer = rdfModel.getResource(String.format("%smanufacturer/%s", baseURI,d.getManufacturer().getId()));
            	drug.addProperty(manufacturedBy,manufacturer);
        	}
        	
        	if (d.getActiveIngredients() != null && d.getActiveIngredients().size() > 0) {
        		
        		Seq ingredientsSeq = rdfModel.createSeq();
        		for (ActiveIngredient ingredientObject : d.getActiveIngredients()) {        			
                	Resource ingredientResource = rdfModel.getResource(String.format("%singredient/%s", baseURI,ingredientObject.getId()));                	
                	ingredientsSeq.add(ingredientResource);
                	//drug.addProperty(containsIngredient, ingredientResource);
        		}
        		drug.addProperty(hasCompound, ingredientsSeq);
        		
        	}
        	
        	if (d.getCategory() != null) {            	
            	Resource category = rdfModel.getResource(String.format("%scategory/%s",baseURI,d.getCategory().getId()));
            	drug.addProperty(isFromCategory, category);
        	}
        }
 
		return rdfModel;
	}
   	

}
