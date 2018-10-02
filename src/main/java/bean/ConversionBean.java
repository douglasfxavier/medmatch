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
	@ManagedProperty (value = "#{csvDataReaderBean}")
	private CSVDataReaderBean csvDataReaderBean;
	private OntologyManager ontologyManager;
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
	

	public MatchingBean getMatchingBean() {
		return matchingBean;
	}

	public void setMatchingBean(MatchingBean matchingBean) {
		this.matchingBean = matchingBean;
	}

	public CSVDataReaderBean getCsvDataReaderBean() {
		return this.csvDataReaderBean;
	}

	public void setCsvDataReaderBean(CSVDataReaderBean csvDataReaderBean) {
		this.csvDataReaderBean = csvDataReaderBean;
	}
	
	public OntologyManager getOntologyManager() {
		return ontologyManager;
	}

	public void setOntologyManager(OntologyManager ontologyManager) {
		this.ontologyManager = ontologyManager;
	}

	public String convert() throws NumberFormatException, FileNotFoundException, ArrayIndexOutOfBoundsException {
		try {
			String filePath;
			String datasetName = this.matchingBean.getUploadBean().getDatasetName();
			Country country = this.matchingBean.getSelectedCountry();
			String namespaceIRI = this.matchingBean.getUploadBean().getNamespaceIRI();
						
			RDFManager manager = new RDFManager();
			FusekiConnector fusekiConnector = new FusekiConnector();
			
			String ontologyIRI = "http://medmatch.global/ontology/pharmacology";
			OntologyManager ontologyManager = new OntologyManager("pharmacology.owl", ontologyIRI);
			ObjectsToRDFConverter rdfConverter = new ObjectsToRDFConverter(namespaceIRI, ontologyManager);
			FacesContext facesContext= FacesContext.getCurrentInstance();
			
			this.csvDataReaderBean.getCsvDataReader()
				.loadData(country, 
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
			
			this.rdfModel = rdfConverter.convertDataToRDF(this.csvDataReaderBean.getCsvDataReader(),country);
						
/*			String path = facesContext.getExternalContext().getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.ttl",country.getCountryName().replace('\n', '_')));
			manager.saveFile(rdfModel, path, "TURTLE",base);	*/	
		
			filePath = facesContext
					.getExternalContext()
					.getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.rdf",country.getCountryName().replace('\n', '_')));
			
			manager.saveFile(rdfModel, filePath, "RDF/XML",namespaceIRI);
			
			//Upload RDF file on the country's dataset on Fuseki
			fusekiConnector.uploadRDF(filePath,datasetUploadService);

			return "conversion?faces-redirect=true";
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
