package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFWriter;

public class RDFManager {	
	
	public void systemOutput(Model model, String rdfFormat){
		model.write(System.out, rdfFormat);
	}
	
	public void saveFile(Model model, String rdfOutFile, String format , String base){
		try{
			FileWriter file = new FileWriter(rdfOutFile);			
			RDFWriter writer = model.getWriter(format);
			writer.setProperty("xmlbase", base);
			writer.write(model, file, base); 
			
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
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
