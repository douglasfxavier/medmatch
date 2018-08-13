package controller;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;


public class OntologyManager {
	private final OntModel ontologyModel;
	private final String ontologyURI;
	private final String ontologyNamespace;
	private final String ontologyFile;	
	private final OntDocumentManager documentManager;
	
	

	public OntologyManager() {
		super();
		this.ontologyModel = ModelFactory.createOntologyModel();
		this.ontologyURI = "http://students.ecs.soton.ac.uk/dfxs1n17/pharmacology";
		this.ontologyNamespace = ontologyURI + "/";
		this.ontologyFile ="file:/resources/ontology/pharmacology.owl";
		this.documentManager = ontologyModel.getDocumentManager();
		this.documentManager.addAltEntry(this.ontologyURI, this.ontologyFile);
		this.ontologyModel.read(ontologyURI);
	}

	public static OntClass findClass(String nomeClasse, OntModel ontModel){
		for (Iterator<OntClass> iterator = ontModel.listClasses(); iterator.hasNext();){
			OntClass ontClass = iterator.next();
			if (ontClass.getLocalName().equals(nomeClasse))
				return ontClass;
		}
		return null;
	}
	
	public static OntProperty findProperty(String nomePropriedade,OntModel ontModel){
		for (Iterator<OntProperty> iterator = ontModel.listAllOntProperties(); iterator.hasNext();){
			OntProperty ontProperty = iterator.next();
			if (ontProperty.getLocalName().equals(nomePropriedade))
				return ontProperty;
		}
		return null;
	}
	
	public ArrayList<String> getClassesNames(OntModel ontModel){
		ArrayList<String> classesNames = new ArrayList<String>();
		ExtendedIterator<OntClass> classes = ontModel.listClasses();
		while (classes.hasNext()) {
			classesNames.add(classes.next().getLocalName());
		}
		
		return classesNames;
	}

	public OntModel getOntologyModel() {
		return ontologyModel;
	}

	public String getOntologyURI() {
		return ontologyURI;
	}

	public String getOntologyNamespace() {
		return ontologyNamespace;
	}

	public String getOntologyFile() {
		return ontologyFile;
	}

	public OntDocumentManager getDocumentManager() {
		return documentManager;
	}
		
}
