package controller;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.rdf.model.Model;


public class RDFManager {	
	
	public void systemOutput(Model model, String rdfFormat){
		model.write(System.out, rdfFormat);
	}
	
	public void saveFile(Model model, String rdfOutFile, String rdfFormat){
		try{
			FileWriter file = new FileWriter(rdfOutFile);
			PrintWriter rdf = new PrintWriter(file);
			model.write(rdf, rdfFormat);
			file.close();
		} catch (IOException e) {
			System.out.println("Problema com o Arquivo");
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
