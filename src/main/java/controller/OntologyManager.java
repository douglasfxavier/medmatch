package controller;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;

public class OntologyManager {
	private final OntModel ontologyModel;
	private final String ontologyIRI;
	private final String ontologyNamespace;	
	private final OntDocumentManager documentManager;

	public OntologyManager(String ontologyFile, String ontologyURI) {
		super();		
		this.ontologyModel = ModelFactory.createOntologyModel();
		this.ontologyIRI = ontologyURI;
		this.ontologyNamespace = ontologyURI + "#";
		this.documentManager = ontologyModel.getDocumentManager();
		this.documentManager.addAltEntry(this.ontologyIRI, "file:" + ontologyFile);
		this.ontologyModel.read(ontologyURI);
	}

	public OntClass findClass(String className){
		try {
			ExtendedIterator<OntClass> ontClassIterator = this.ontologyModel.listClasses();
			while(ontClassIterator.hasNext()){
				OntClass ontClass = ontClassIterator.next();
				if (ontClass.getLocalName().equals(className))
					return ontClass;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public OntProperty findProperty(String propertyName){
		try {	
			ExtendedIterator<OntProperty> ontClassIterator = this.ontologyModel.listAllOntProperties();
			while (ontClassIterator.hasNext()){
				OntProperty ontProperty = ontClassIterator.next();
				if (ontProperty.getLocalName().equals(propertyName))
					return ontProperty;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public OntModel getOntologyModel() {
		return ontologyModel; 
	}

	public String getOntologyIRI() {
		return ontologyIRI;
	}

	public String getOntologyNamespace() {
		return ontologyNamespace;
	}

	public OntDocumentManager getDocumentManager() {
		return documentManager;
	}
		
}
