package bean;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import controller.OntologyManager;

@ManagedBean (name = "ontologyBean")
@ApplicationScoped
public class OntologyBean {
	OntologyManager ontologyManager;
	
	@PostConstruct
	public void Init() {
		FacesContext fc= FacesContext.getCurrentInstance();
		String path = fc.getExternalContext().getRealPath("WEB-INF\\classes\\ontology\\pharmacology.owl");
		this.ontologyManager = new OntologyManager(path);
	}
	
	public OntologyManager getOntologyManager() {

		return this.ontologyManager;
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
