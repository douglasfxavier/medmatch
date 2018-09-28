package controller;

import java.util.Map;
import java.util.TreeMap;

import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class MatchingSearch {
	public static Map<Float,String> searchDrugByBrandName(String brandName, 
												Model rdfModel, 
												OntologyManager ontologyManager) {
		OntProperty hasCompound = ontologyManager.findProperty("hasCompound");
		StmtIterator statements = rdfModel.listStatements(new SimpleSelector(null,hasCompound,(RDFNode) null));
		Map<Float,String> metricsMap = new TreeMap<Float, String>();		
		
		while(statements.hasNext()) {
			Statement s = statements.next();
			if(s.getSubject().getClass().getName().equals("Drug")) {
				Float metric = StringComparison.euclideanDistance(brandName, s.getObject().toString());
				if (metric >=0 && metric <=3) {
					metricsMap.put(metric,s.getSubject().getURI());
				}
			}
			
		}
		return null;
	}

}
