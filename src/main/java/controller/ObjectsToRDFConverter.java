package controller;

import java.io.FileNotFoundException;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import model.Compound;
import model.Drug;
import model.Manufacturer;
import model.Category;

public class ObjectsToRDFConverter {
	private final Model rdfModel;			
	private final OntModel ontologyModel;
	private final String ontologyURI;
	private final String ontologyNamespace;
	private final String ontologyFile;	
	private final OntDocumentManager documentManager;
	private final String URL;
	
	public ObjectsToRDFConverter() {
		this.URL = "http://wwww.anvisa.gov.br/";
		this.rdfModel = ModelFactory.createDefaultModel();
		this.ontologyModel = ModelFactory.createOntologyModel();
		this.ontologyURI = "http://students.ecs.soton.ac.uk/dfxs1n17/pharmacology";
		this.ontologyNamespace = ontologyURI + "/";
		this.ontologyFile ="file:./src/main/resources/ontology/pharmacology.owl";
		this.documentManager = ontologyModel.getDocumentManager();
		this.documentManager.addAltEntry(this.ontologyURI, this.ontologyFile);
		this.ontologyModel.read(ontologyURI);
		this.rdfModel.setNsPrefix("pharm", ontologyNamespace);
		this.rdfModel.setNsPrefix("foaf", "http://xmlns.com/foaf/0.1/");
		this.rdfModel.setNsPrefix("dcterms", "http://purl.org/dc/terms/");
	
	}
	
	public Model convertDataToRDF(String path) throws FileNotFoundException {
        
        CSVDataReader csvDataReader = new CSVDataReader();
        csvDataReader.loadData(path);
        OntClass drugClass = OntologyManager.findClass("Drug", this.ontologyModel);
        OntClass manufacturerClass = OntologyManager.findClass("Manufacturer",this.ontologyModel);
        OntClass compoundClass = OntologyManager.findClass("Compound", this.ontologyModel);
        OntClass categoryClass = OntologyManager.findClass("Category", this.ontologyModel);
        String drugURL = this.URL + "drug/";
        String compoundURL = this.URL + "compound/";
        String manufactureURL = this.URL + "manufacturer/";
        String categoryURL = this.URL + "category/";
        
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
        	String instanceURI = drugURL + d.getDrugCode(); 
        	Resource drug = rdfModel.createResource(instanceURI,drugClass);
        	OntProperty drugName = OntologyManager.findProperty("name", this.ontologyModel);
        	drug.addProperty(drugName, d.getBrandName());
        	OntProperty hasStrength = OntologyManager.findProperty("hasStrength", this.ontologyModel);
        	drug.addProperty(hasStrength, d.getStrength());
        	OntProperty manufacturedBy = OntologyManager.findProperty("manufacturedBy", this.ontologyModel);
        	Resource manufacturer = rdfModel.getResource(manufactureURL + d.getManufacturer().getId());
        	drug.addProperty(manufacturedBy,manufacturer);
        	OntProperty hasCompound = OntologyManager.findProperty("hasCompound", this.ontologyModel);
        	Resource compound = rdfModel.getResource(compoundURL + d.getCompound().getId());
        	drug.addProperty(hasCompound, compound);
        	OntProperty isFromCategory = OntologyManager.findProperty("isFromCategory", this.ontologyModel);
        	Resource category = rdfModel.getResource(categoryURL  + d.getCategory().getId());
        	drug.addProperty(isFromCategory, category);
        	
        }
      
        
		return rdfModel;
	}
    
	

}
