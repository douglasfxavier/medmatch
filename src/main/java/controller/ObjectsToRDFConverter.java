package controller;

import java.io.FileNotFoundException;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import controller.OntologyManager;
import model.Compound;
import model.Drug;
import model.Manufacturer;
import model.Category;

public class ObjectsToRDFConverter {
	private final Model rdfModel;
	private final OntModel ontologyModel;
	private final String datasetURL;

	
	public ObjectsToRDFConverter(String datasetURL, OntologyManager ontologyManager) {
		this.datasetURL = datasetURL;
		this.ontologyModel = ontologyManager.getOntologyModel();
		this.rdfModel = ModelFactory.createDefaultModel();
		this.rdfModel.setNsPrefix("pharm", ontologyManager.getOntologyNamespace());
		this.rdfModel.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
		this.rdfModel.setNsPrefix("dcterms", "http://purl.org/dc/terms/");	
	}
	
	
	
	public Model convertDataToRDF(CSVDataReader csvDataReader) throws FileNotFoundException {
        OntClass drugClass = OntologyManager.findClass("Drug", this.ontologyModel);
        OntClass manufacturerClass = OntologyManager.findClass("Manufacturer",this.ontologyModel);
        OntClass compoundClass = OntologyManager.findClass("Compound", this.ontologyModel);
        OntClass categoryClass = OntologyManager.findClass("Category", this.ontologyModel);
/*        String drugURL = this.datasetURL + "drug/";
        String compoundURL = this.datasetURL + "compound/";
        String manufactureURL = this.datasetURL + "manufacturer/";
        String categoryURL = this.datasetURL + "category/";*/
 
        String drugURL = "drug/";
        String compoundURL = "compound/";
        String manufactureURL = "manufacturer/";
        String categoryURL = "category/";
        
        //Converting objects from Manufacturer class to resources
        for(Manufacturer m: csvDataReader.manufacturerList) {
        	String instanceURI = manufactureURL + m.getId();
        	Resource manufacturer = rdfModel.createResource(instanceURI,manufacturerClass);
        	OntProperty manufacturerName = OntologyManager.findProperty("name", this.ontologyModel);
        	manufacturer.addProperty(manufacturerName, m.getName());
        }
        
        //Converting objects from Compound class to resources
        for(Compound c: csvDataReader.compoundList) {
        	String instanceURI = compoundURL + c.getId();
        	Resource compound = rdfModel.createResource(instanceURI,compoundClass);
        	OntProperty compoundName = OntologyManager.findProperty("name", this.ontologyModel);
        	compound.addProperty(compoundName, c.getName());
        }      
        
        //Converting objects from Category class to resources
        for(Category c: csvDataReader.categoryList) {
        	String instanceURI = categoryURL + c.getId();
        	Resource category = rdfModel.createResource(instanceURI,categoryClass);
        	OntProperty categoryName = OntologyManager.findProperty("name", this.ontologyModel);
        	category.addProperty(categoryName, c.getName());
        }      
        
        //Converting objects from Drug class to resources and creating relationships between resources
        for(Drug d: csvDataReader.drugsList) {        	
        	
        	if (d.getDrugCode() == null) {
        		return null;
        	}

    		String instanceURI = drugURL + d.getDrugCode(); 
    		Resource drug = rdfModel.createResource(instanceURI,drugClass);

        	if (d.getBrandName() != null) {
        		OntProperty drugName = OntologyManager.findProperty("name", this.ontologyModel);
            	drug.addProperty(drugName, d.getBrandName());
        	}
        	
        	if (d.getStrength() != null){
        		OntProperty hasStrength = OntologyManager.findProperty("hasStrength", this.ontologyModel);
            	drug.addProperty(hasStrength, d.getStrength());
        	}
        	
        	if (d.getManufacturer() != null) {
        		OntProperty manufacturedBy = OntologyManager.findProperty("manufacturedBy", this.ontologyModel);
            	Resource manufacturer = rdfModel.getResource(manufactureURL + d.getManufacturer().getId());
            	drug.addProperty(manufacturedBy,manufacturer);
        	}
        	
        	if (d.getCompound() != null) {
        		OntProperty hasCompound = OntologyManager.findProperty("hasCompound", this.ontologyModel);
            	Resource compound = rdfModel.getResource(compoundURL + d.getCompound().getId());
            	drug.addProperty(hasCompound, compound);
        	}
        	
        	if (d.getCategory() != null) {
            	OntProperty isFromCategory = OntologyManager.findProperty("isFromCategory", this.ontologyModel);
            	Resource category = rdfModel.getResource(categoryURL  + d.getCategory().getId());
            	drug.addProperty(isFromCategory, category);
        	} 
        }
 
		return rdfModel;
	}
   	

}
