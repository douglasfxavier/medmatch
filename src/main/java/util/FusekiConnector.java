package util;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

public class FusekiConnector {
	private EntityBuilder builder; 
	private HttpClient client;
	
	public FusekiConnector() {
		this.builder = EntityBuilder.create();
		this.client = HttpClientBuilder.create().build();
	}
	
	public void uploadRDF(String filePath, String datasetName ) {
		File file = new File(filePath);
		FileBody fileBody = new FileBody(file,ContentType.DEFAULT_BINARY);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("file", fileBody);
        HttpEntity entity = builder.build();
        
        String datasetService = String.format("http://localhost:3030/%s/upload", datasetName);
        HttpPost request = new HttpPost(datasetService);
        
        request.setEntity(entity);
               
        try {
            this.client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void update(String fusekiService, String sparqlString) {
		  this.builder.setContentType(ContentType.create("application/sparql-update"));
		  this.builder.setText(sparqlString);
		  HttpEntity entity = builder.build();		  
		  HttpPost request = new HttpPost(fusekiService);
		  request.setEntity(entity);

	      try {
	    	  this.client.execute(request);
	      } catch (IOException e) {
	          e.printStackTrace();
	      }		  
	}
}
