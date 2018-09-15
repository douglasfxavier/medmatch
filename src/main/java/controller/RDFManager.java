package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;


public class RDFManager {	
	
	public void systemOutput(Model model, String rdfFormat){
		model.write(System.out, rdfFormat);
	}
	
	public void saveFile(Model model, String rdfOutFile, Lang rdfLang, String base){
		try{
			FileWriter file = new FileWriter(rdfOutFile);
			PrintWriter rdf = new PrintWriter(file);
			model.write(rdf, rdfLang.getLabel(),base);
			file.close();
		} catch (IOException e) {
			System.out.println("Something wrong happened. File not saved.");
		}
	}
	/*
	public void publishResource(){
		if(this.model != null){
			Iterator<Triple> iter = this.model.getGraph().find(Node.ANY, Node.ANY, Node.ANY);
			while (iter.hasNext()) {
				Triple triple = iter.next();
				LattesResource.getInstance().getVirtGraph().performAdd(triple);
			}
		}
	}
	*/

}
