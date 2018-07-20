package controller;
import java.util.Iterator;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Model;


public class OntologyManager {

	public static OntClass findClass(String nomeClasse,OntModel ontModel){
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
}
