package bean;

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
	

}
