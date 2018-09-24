package bean;

import java.io.FileNotFoundException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.jena.rdf.model.Model;

import controller.ObjectsToRDFConverter;
import controller.OntologyManager;
import controller.RDFManager;
import model.Country;
import util.FusekiConnector;

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
	private String activeIngredientNameIndex;
	private String drugCodeIndex;
	private String brandNameIndex;
	private String manufacturerIDIndex;
	private String manufacturerNameIndex;
	private String strengthIndex;

	public String getCategoryIndex() {
		return categoryIndex;
	}

	public void setCategoryIndex(String categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	public String getDrugCodeIndex() {
		return drugCodeIndex;
	}

	public void setDrugCodeIndex(String drugCodeIndex) {
		this.drugCodeIndex = drugCodeIndex;
	}

	public String getActiveIngredientNameIndex() {
		return activeIngredientNameIndex;
	}

	public void setActiveIngredientNameIndex(String activeIngredientNameIndex) {
		this.activeIngredientNameIndex = activeIngredientNameIndex;
	}

	public String getBrandNameIndex() {
		return brandNameIndex;
	}

	public void setBrandNameIndex(String brandNameIndex) {
		this.brandNameIndex = brandNameIndex;
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
			String filePath;
			String datasetName = this.matchingBean.getUploadBean().getDatasetName();
			Country country = this.matchingBean.getSelectedCountry();
			String namespaceURI = this.matchingBean.getUploadBean().getNamespaceURI();
			
			RDFManager manager = new RDFManager();
			FusekiConnector fusekiConnector = new FusekiConnector();
			
			
			this.csvDataReaderBean.getCsvDataReader().loadData(country, 
					   drugCodeIndex, 
					   brandNameIndex, 
					   activeIngredientNameIndex,
					   categoryIndex, 
					   manufacturerIDIndex, 
					   manufacturerNameIndex, 
					   strengthIndex);
	
			
			OntologyManager ontologyManager = this.matchingBean.getOntologyBean().getOntologyManager();
			ObjectsToRDFConverter rdfConverter = new ObjectsToRDFConverter(namespaceURI, ontologyManager);
			
			this.rdfModel = rdfConverter.convertDataToRDF(this.csvDataReaderBean.getCsvDataReader());
						
			FacesContext facesContext= FacesContext.getCurrentInstance();
/*			String path = facesContext.getExternalContext().getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.ttl",country.getCountryName().replace('\n', '_')));
			manager.saveFile(rdfModel, path, "TURTLE",base);	*/	
		
			filePath = facesContext.getExternalContext().getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.rdf",country.getCountryName().replace('\n', '_')));
			manager.saveFile(rdfModel, filePath, "RDF/XML",namespaceURI);
			
			fusekiConnector.uploadRDF(filePath, datasetName);

			return "conversion?faces-redirect=true";
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
