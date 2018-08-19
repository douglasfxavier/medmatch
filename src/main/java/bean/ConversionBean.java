package bean;

import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.jena.rdf.model.Model;

import controller.CSVDataReader;
import controller.ObjectsToRDFConverter;
import controller.OntologyManager;
import controller.RDFManager;
import model.Country;

@ManagedBean (name = "conversionBean")
@SessionScoped
public class ConversionBean {
	@ManagedProperty (value = "#{matchingBean}")
	private MatchingBean matchingBean;
	@ManagedProperty (value = "#{csvDataReaderBean}")
	private CSVDataReaderBean csvDataReaderBean;
	@ManagedProperty (value = "#{ontologyBean}")
	private OntologyBean ontologyBean;
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
	

	public CSVDataReaderBean getCsvDataReaderBean() {
		return csvDataReaderBean;
	}

	public void setCsvDataReaderBean(CSVDataReaderBean csvDataReaderBean) {
		this.csvDataReaderBean = csvDataReaderBean;
	}

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

	public String convert() throws NumberFormatException, FileNotFoundException {
		Country country = matchingBean.getSelectedCountry();
		String datasetURL = matchingBean.getUploadBean().getDatasetURL();
		CSVDataReader csvDataReader = matchingBean.getCsvDataReaderBean().getCsvDataReader();
		OntologyManager ontologyManager = matchingBean.getOntologyBean().getOntologyManager();
		RDFManager manager = new RDFManager();
				
		csvDataReader.loadData(country, 
				   Integer.parseInt(drugCodeIndex), 
				   Integer.parseInt(drugNameIndex), 
				   Integer.parseInt(compoundNameIndex),
				   Integer.parseInt(categoryIndex), 
				   Integer.parseInt(manufacturerIDIndex), 
				   Integer.parseInt(manufacturerNameIndex), 
				   Integer.parseInt(strengthIndex));

		ObjectsToRDFConverter rdfConverter = new ObjectsToRDFConverter(datasetURL, ontologyManager);
		
		this.rdfModel = rdfConverter.convertDataToRDF(csvDataReader);
		this.rdfString = rdfModel.toString();
		
		manager.systemOutput(rdfModel, "TURTLE");
		
		
		return "conversion?faces-redirect=true";
	}

}
