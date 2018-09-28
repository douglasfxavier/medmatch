package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFReader;

import controller.AppConfig;

import model.Country;

public class FusekiConnector {
	private EntityBuilder builder; 
	private HttpClient client;
		
	public FusekiConnector() {
		this.builder = EntityBuilder.create();
		this.client = HttpClientBuilder.create().build();
	}
	
	public void uploadRDF(String filePath, String datasetUploadService) {
        try {	
			File file = new File(filePath);
			FileBody fileBody = new FileBody(file,ContentType.DEFAULT_BINARY);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        	builder.addPart("file", fileBody);
        	HttpEntity entity = builder.build();
        
        	HttpPost request = new HttpPost(datasetUploadService);
        
        	request.setEntity(entity);
               
            this.client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void storeDatasetURI(Country country,
								String datasetEndpoint,
								String datasetDumpDataService,																							
								String datasetSparqlService){	
		try {
			String countryURI = country.getUri();
			String sparqlInsert = 
			  "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
			  "PREFIX void: <http://rdfs.org/ns/void#>\n" +
			  "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" +		  
			  "INSERT DATA\n" +
			  "{\n" + 
			  	String.format("<%s> rdf:type void:Dataset;\n", datasetEndpoint) +
			  	String.format("void:dataDump <%s>;\n", datasetDumpDataService) +
			  	String.format("void:sparqlEndpoint <%s>;\n", datasetSparqlService) +
			  	String.format("wdt:P17 <%s> .\n", countryURI) + 
			  "}";
					  
			this.builder.setContentType(ContentType.create("application/sparql-update"));
			this.builder.setText(sparqlInsert);
			HttpEntity entity = builder.build();		  
			HttpPost request;
			String updateService = AppConfig.getProperty("updateService");
			request = new HttpPost(updateService);
			request.setEntity(entity);
			
			this.client.execute(request);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public HashMap<String, String> getDatasetDetailsByCountry(Country country) {
		try {
			String queryString = 
				"SELECT ?datasetEndpoint ?dumpService ?sparqlService\n" +
				"WHERE\n" +
				"{\n" + 
					"?datasetEndpoint rdf:type void:Dataset;\n" +
					"void:dataDump ?dumpService;\n" +
					"void:sparqlEndpoint ?sparqlService;\n" +
					"wdt:P17 ?country .\n" + 
					String.format("FILTER(?country = <%s>)\n", country.getUri()) +				
				"}";
		
            	Query query = QueryFactory.create();
            	query.setPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
            	query.setPrefix("wdt", "http://www.wikidata.org/prop/direct/");
            	query.setPrefix("void", "http://rdfs.org/ns/void#");
            	
	        	QueryFactory.parse(query, queryString, "", Syntax.syntaxSPARQL_11);
            	QueryExecution qexec = QueryExecutionFactory.sparqlService(AppConfig.getProperty("sparqlService"), query);
            	ResultSet result = qexec.execSelect();
            	
            	HashMap<String, String> services = new HashMap<>();
            	while(result.hasNext()) {
            		QuerySolution row = result.next();
            		String datasetEndpoint = row.get("datasetEndpoint").toString();
            		String dumpService = row.get("dumpService").toString();
            		String sparqlService = row.get("sparqlService").toString();
            		services.put("datasetEndpoint", datasetEndpoint);
            		services.put("dumpService", dumpService);
            		services.put("sparqlService", sparqlService);
            	}
           		qexec.close();
           		return services;
             
	    } catch (IOException e) {
	      e.printStackTrace();
	      return null;
	    }	
	}
	 
	public void update(String fusekiService, String sparqlString) {
		try {
			this.builder.setContentType(ContentType.create("application/sparql-update"));
		  	this.builder.setText(sparqlString);
		  	HttpEntity entity = builder.build();		  
		  	HttpPost request = new HttpPost(fusekiService);
		  	request.setEntity(entity);
		  	
	        this.client.execute(request);
	        
	      } catch (IOException e) {
	          e.printStackTrace();
	      }		  
	}
	
	public Model dumpData(String dumpService) {
		try {  
			
			HttpGet request = new HttpGet(dumpService);
		  	HttpResponse response = this.client.execute(request);
		  	InputStream inputStream = response.getEntity().getContent();
		  	Model rdfModel = ModelFactory.createDefaultModel();
		  	RDFReader rdfReader = rdfModel.getReader("N-QUADS");
		  	rdfReader.setProperty("iri-rules", "strict") ;
		  	rdfReader.setProperty("error-mode", "strict") ; // Warning will be errors.
		  	rdfReader.read(rdfModel, inputStream, null) ;
		  	inputStream.close();	  	
 	
		  	return rdfModel;

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}				
	}
}
