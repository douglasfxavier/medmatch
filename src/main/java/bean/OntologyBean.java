package bean;

import java.util.ArrayList;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import controller.OntologyManager;

@ManagedBean (name = "ontologyBean")
@ApplicationScoped
public class OntologyBean {
	
	public OntologyManager getOntologyManager(String fileName, String ontologyURI) {
		FacesContext fc= FacesContext.getCurrentInstance();
		String path = fc.getExternalContext().getRealPath("WEB-INF\\classes\\ontology\\"+ fileName);		
		OntologyManager ontologyManager = new OntologyManager(path, ontologyURI);
		return ontologyManager;
	}
	
	public ArrayList<String> getOntologyTerms(){
		ArrayList<String> list = new ArrayList<String>();
		list.add("Drug_Code");
		list.add("Drug_Name");
		list.add("Compound");
		list.add("Category");
		list.add("Manufacturer_ID");
		list.add("Manufacturer_Name");
		list.add("Strength");
		return list;
	}
}
