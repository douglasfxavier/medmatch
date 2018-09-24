package util;

import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdfconnection.SparqlUpdateConnection;
import org.apache.jena.shared.PrefixMapping;

public class SparqlUtil {
	public SparqlUtil() {
		
	}
	
	
	public String mountPrefixesHead(Model model) {
		String prefixes = "";
		for(String key: model.getNsPrefixMap().keySet()) {
			prefixes += "PREFIX " + key
								 + ": <" + model.getNsPrefixMap().get(key) + ">\n";
		}
		return prefixes;
	}
	
	public void insertTriples(Model model, String graphURI, String fusekiService) {
		FusekiConnector fusekiConnector = new FusekiConnector();
		StmtIterator smtIterator = model.listStatements();
    	String statementsString = "";
    	int i = 0;
		while(smtIterator.hasNext()) {
			i++;
			Statement statement = smtIterator.next();
    		statementsString += String.format("<%s> <%s> %s .\n" , 
    									statement.getSubject(), 
    									statement.getPredicate(), 
    									statement.getObject().isResource() ? 
    											String.format("<%s>", statement.getObject())
    											: String.format("\"%s\"", statement.getObject()));
    		
    		if (i % 100 == 0) {
    			String sparqlString =
    					  "INSERT DATA {\n"
    					  + "GRAPH <" + graphURI + "> {\n" 
    					  + statementsString + "\n"
    					  + "} .\n"
    					  + "}";
    			System.out.println(sparqlString);
    			fusekiConnector.update(fusekiService, sparqlString);
    			statementsString = "";
    		}
    	}
	}
}
