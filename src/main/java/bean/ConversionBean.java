package bean;

import java.io.FileNotFoundException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

import org.apache.jena.rdf.model.Model;

import controller.CSVDataReader;
import controller.ObjectsToRDFConverter;
import controller.RDFManager;
import model.Country;
import util.CSVDelimiter;
import util.FusekiConnector;

@ManagedBean (name = "conversionBean")
@ViewScoped
public class ConversionBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ManagedProperty (value = "#{countryListBean}")
	private CountryListBean countryListBean;
	private Model rdfModel;
	private String rdfString;
	//Fields send by post from the view matching.xhtml
	private String drugCodeIndex;
	private String brandIndex;
	private String drugClassIndex;
	private String atcCodeIndex;
	private String activeIngredientNameIndex;
	private String manufacturerIDIndex;
	private String manufacturerNameIndex;
	private String strengthIndex;
	private String csvData;
	private String selectedDelimiter;
	private String datasetName;
	private String selectedCountryNumericCode;
	private CSVDataReader csvDataReader;
	
	@PostConstruct
	public void Init() {
		Flash flash = FacesContext.getCurrentInstance()
				.getExternalContext().getFlash();
		this.csvData = (String) flash.get("csvData");
		this.selectedDelimiter = (String) flash.get("selectedDelimiter");
		this.datasetName = (String) flash.get("datasetName");
		this.selectedCountryNumericCode = (String) flash.get("selectedCountryNumericCode");
		char delimiter = CSVDelimiter.valueOf(this.selectedDelimiter).getDelimiter().charAt(0);
		this.csvDataReader = new CSVDataReader(csvData, delimiter);
	}
	
	public Country getSelectedCountry() {
		int numericCode = Integer.parseInt(this.selectedCountryNumericCode); 
		Country selectedCountry = this.countryListBean.getCountry(numericCode);		
		return selectedCountry;
	}

	public CountryListBean getCountryListBean() {
		return this.countryListBean;
	}

	public void setCountryListBean(CountryListBean countryListBean) {
		this.countryListBean = countryListBean;
	}

	public CSVDataReader getCsvDataReader() {
		return csvDataReader;
	}

	public void setCsvDataReader(CSVDataReader csvDataReader) {
		this.csvDataReader = csvDataReader;
	}

	public String getCsvData() {
		return this.csvData;
	}

	public String getDrugCodeIndex() {
		return drugCodeIndex;
	}

	public void setDrugCodeIndex(String drugCodeIndex) {
		this.drugCodeIndex = drugCodeIndex;
	}
	
	public String getBrandIndex() {
		return brandIndex;
	}

	public void setBrandIndex(String brandIndex) {
		this.brandIndex = brandIndex;
	}

	public String getDrugClassIndex() {
		return drugClassIndex;
	}

	public void setDrugClassIndex(String drugClassIndex) {
		this.drugClassIndex = drugClassIndex;
	}

	public String getAtcCodeIndex() {
		return atcCodeIndex;
	}

	public void setAtcCodeIndex(String atcCodeIndex) {
		this.atcCodeIndex = atcCodeIndex;
	}

	public String getActiveIngredientNameIndex() {
		return activeIngredientNameIndex;
	}

	public void setActiveIngredientNameIndex(String activeIngredientNameIndex) {
		this.activeIngredientNameIndex = activeIngredientNameIndex;
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
		
	public String convert() throws NumberFormatException, FileNotFoundException, ArrayIndexOutOfBoundsException {
		try {
			String filePath;

			Country country = countryListBean.getCountry(Integer.parseInt(this.selectedCountryNumericCode)); 
			 
						
			RDFManager manager = new RDFManager();
			FusekiConnector fusekiConnector = new FusekiConnector();
						
			ObjectsToRDFConverter rdfConverter = new ObjectsToRDFConverter();
			FacesContext facesContext= FacesContext.getCurrentInstance();
			
				this.csvDataReader.loadData(country, 
						  drugCodeIndex, 
					      brandIndex, 
					      activeIngredientNameIndex,
					      atcCodeIndex,
					      drugClassIndex,					   
					      manufacturerIDIndex, 
					      manufacturerNameIndex, 
					      strengthIndex);

			String datasetEndpoint = String.format("http://localhost:3030/%s",datasetName);
			String datasetUploadService = String.format("%s/upload",datasetEndpoint);
			String datasetDumpDataService = String.format("%s/get",datasetEndpoint);
			String datasetSparqlService = String.format("%s/sparql",datasetEndpoint);
			
			//Store dataset details on MedMatch  
			fusekiConnector.storeDatasetURI(country,
											datasetEndpoint,
											datasetDumpDataService,
											datasetSparqlService);
			
			this.rdfModel = rdfConverter.convertDataToRDF(this.csvDataReader,country);
						
/*			String path = facesContext.getExternalContext().getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.ttl",country.getCountryName().replace('\n', '_')));
			manager.saveFile(rdfModel, path, "TURTLE",base);	*/	
		
			filePath = facesContext
					.getExternalContext()
					.getRealPath(String.format("resources/data/Drugs_%s.rdf",country.getCountryName().replace('\n', '_')));
			
			String ontologyIRI = "http://medmatch.global/ontology/pharmacology";
			manager.saveFile(rdfModel, filePath, "RDF/XML",ontologyIRI);
			
			//Upload RDF file on the country's dataset on Fuseki
			fusekiConnector.uploadRDF(filePath,datasetUploadService);
			
			return "null";
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
