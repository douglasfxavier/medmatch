package bean;

import java.io.FileNotFoundException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.jena.fuseki.embedded.FusekiServer;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.fuseki.async.*;

import controller.ObjectsToRDFConverter;
import controller.OntologyManager;
import controller.RDFManager;
import model.Country;

@ManagedBean (name = "conversionBean")
@SessionScoped
public class ConversionBean {
	@ManagedProperty (value = "#{matchingBean}")
	private MatchingBean matchingBean;
	@ManagedProperty (value = "#{ontologyBean}")
	private OntologyBean ontologyBean;
	@ManagedProperty (value = "#{csvDataReaderBean}")
	private CSVDataReaderBean csvDataReaderBean;
	private Model rdfModel;
	private String rdfString;
	//Fields send by post from the view matching.xhtml
	private String categoryIndex;
	private String compoundNameIndex;
	private String drugCodeIndex;
	private String drugNameIndex;
	private String manufacturerIDIndex;
	private String manufacturerNameIndex;
	private String strengthIndex;

	public String getCategoryIndex() {
		return categoryIndex;
	}

	public void setCategoryIndex(String categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	public String getCompoundNameIndex() {
		return compoundNameIndex;
	}

	public void setCompoundNameIndex(String compoundNameIndex) {
		this.compoundNameIndex = compoundNameIndex;
	}

	public String getDrugCodeIndex() {
		return drugCodeIndex;
	}

	public void setDrugCodeIndex(String drugCodeIndex) {
		this.drugCodeIndex = drugCodeIndex;
	}

	public String getDrugNameIndex() {
		return drugNameIndex;
	}

	public void setDrugNameIndex(String drugNameIndex) {
		this.drugNameIndex = drugNameIndex;
	}

	public String getManufacturerIDIndex() {
		return manufacturerIDIndex;
	}

	public void setManufacturerIDIndex(String manufacturerIDIndex) {
		this.manufacturerIDIndex = manufacturerIDIndex;
	}

	public String getManufacturerNameIndex() {
		return manufacturerNameIndex;
	}

	public void setManufacturerNameIndex(String manufacturerNameIndex) {
		this.manufacturerNameIndex = manufacturerNameIndex;
	}

	public String getStrengthIndex() {
		return strengthIndex;
	}

	public void setStrengthIndex(String strengthIndex) {
		this.strengthIndex = strengthIndex;
	}

	public Model getRdfModel() {
		return rdfModel;
	}

	public void setRdfModel(Model rdfModel) {
		this.rdfModel = rdfModel;
	}

	public String getRdfString() {
		return rdfString;
	}

	public void setRdfString(String rdfString) {
		this.rdfString = rdfString;
	}
	

	public MatchingBean getMatchingBean() {
		return matchingBean;
	}

	public void setMatchingBean(MatchingBean matchingBean) {
		this.matchingBean = matchingBean;
	}

	public OntologyBean getOntologyBean() {
		return ontologyBean;
	}

	public void setOntologyBean(OntologyBean ontologyBean) {
		this.ontologyBean = ontologyBean;
	}

	public CSVDataReaderBean getCsvDataReaderBean() {
		return this.csvDataReaderBean;
	}

	public void setCsvDataReaderBean(CSVDataReaderBean csvDataReaderBean) {
		this.csvDataReaderBean = csvDataReaderBean;
	}

	public String convert() throws NumberFormatException, FileNotFoundException, ArrayIndexOutOfBoundsException {
		try {
			Country country = this.matchingBean.getSelectedCountry();
			String base = this.matchingBean.getUploadBean().getDatasetURL();
			OntologyManager ontologyManager = this.matchingBean.getOntologyBean().getOntologyManager();
			RDFManager manager = new RDFManager();
			

	
			this.csvDataReaderBean.getCsvDataReader().loadData(country, 
					   drugCodeIndex, 
					   drugNameIndex, 
					   compoundNameIndex,
					   categoryIndex, 
					   manufacturerIDIndex, 
					   manufacturerNameIndex, 
					   strengthIndex);
	
			ObjectsToRDFConverter rdfConverter = new ObjectsToRDFConverter(base, ontologyManager);
			
			this.rdfModel = rdfConverter.convertDataToRDF(this.csvDataReaderBean.getCsvDataReader());
			
			FacesContext fc= FacesContext.getCurrentInstance();
			
			String path = fc.getExternalContext().getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.ttl",country.getCountryName().replace('\n', '_')));
			manager.saveFile(rdfModel, path, Lang.TURTLE,base);		
		
			path = fc.getExternalContext().getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.rdf",country.getCountryName().replace('\n', '_')));
			manager.saveFile(rdfModel, path, Lang.RDFXML,base);			

			DatasetGraph graph = DatasetFactory.create(rdfModel).asDatasetGraph();
			FusekiServer server = FusekiServer.create()
				    .add(String.format("/%s", country.getCountryName().replace('\n', '_')),graph)
				    .build();
			server.start();		
			//server.stop();
			
			
			return "conversion?faces-redirect=true";
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
