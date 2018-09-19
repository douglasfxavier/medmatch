package bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
import org.apache.jena.fuseki.embedded.FusekiServer;
import org.apache.jena.fuseki.server.Operation;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.riot.RIOT;
import org.apache.jena.riot.WriterDatasetRIOT;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.system.Txn;
import org.apache.jena.tdb2.TDB2Factory;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.fuseki.servlets.*;

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
			manager.saveFile(rdfModel, path, "TURTLE",base);		
		
			path = fc.getExternalContext().getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.rdf",country.getCountryName().replace('\n', '_')));
			manager.saveFile(rdfModel, path, "RDF/XML",base);			
			
			Dataset dataset = TDB2Factory.createDataset();
			dataset.begin(ReadWrite.WRITE);

			File file = new File(path);
			FileBody fileBody = new FileBody(file,ContentType.DEFAULT_BINARY);
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		        builder.addPart("file", fileBody);
		        builder.setContentType(ContentType.create("application/rdf+xml"));
		        HttpEntity entity = builder.build();
		        HttpPost request = new HttpPost("http://localhost:8082/fuseki/medmatch/upload");
		        request.setEntity(entity);
		       

		        HttpClient client = HttpClientBuilder.create().build();
		        try {
		            client.execute(request);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			
			/*			FusekiServer server = FusekiServer.create()
				    .add("/medmatch", dataset, true)
				    .addOperation("/medmatch", 
				    		"http://localhost:8082/fuseki/medmatch/update", Operation.Update)
				    .build();
			server.start();
			
			UpdateRequest request = UpdateFactory.create();
			
			request.add("DROP ALL")
				   .add("PREFIX dc: <http://purl.org/dc/elements/1.1/>\r\n" + 
				   		"INSERT DATA\r\n" + 
				   		"{ \r\n" + 
				   		"  <http://example/book1> dc:title \"A new book\" ;\r\n" + 
				   		"                         dc:creator \"A.N.Other\" .\r\n" + 
				   		"}");
				   			
			
			server.stop();*/

			return "conversion?faces-redirect=true";
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
