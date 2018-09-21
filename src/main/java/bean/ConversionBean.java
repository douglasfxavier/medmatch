package bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Model;

import controller.ObjectsToRDFConverter;
import controller.OntologyManager;
import controller.RDFManager;
import model.Country;
import util.FusekiConnector;
import util.SparqlUtil;

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
			String baseURI = this.matchingBean.getUploadBean().getDatasetURL();
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
	
			ObjectsToRDFConverter rdfConverter = new ObjectsToRDFConverter(baseURI, ontologyManager);
			
			this.rdfModel = rdfConverter.convertDataToRDF(this.csvDataReaderBean.getCsvDataReader());
						
			FacesContext facesContext= FacesContext.getCurrentInstance();
/*			String path = facesContext.getExternalContext().getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.ttl",country.getCountryName().replace('\n', '_')));
			manager.saveFile(rdfModel, path, "TURTLE",base);	*/	
		
			String path = facesContext.getExternalContext().getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.rdf",country.getCountryName().replace('\n', '_')));
			manager.saveFile(rdfModel, path, "RDF/XML",baseURI);

/*			SparqlUtil sparqlUtil = new SparqlUtil();
			String fusekiService = "http://localhost:3030/drugs_brazil/update";
			sparqlUtil.insertTriples(rdfModel, baseURI, fusekiService);*/
			
			File file = new File(path);
			FileBody fileBody = new FileBody(file,ContentType.DEFAULT_BINARY);
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		        builder.addPart("file", fileBody);
		        HttpEntity entity = builder.build();
		        HttpPost request = new HttpPost("http://localhost:3030/drugs_brazil/upload");
		        request.setEntity(entity);
		        
		        HttpClient client = HttpClientBuilder.create().build();
		        try {
		            client.execute(request);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			return "conversion?faces-redirect=true";
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
