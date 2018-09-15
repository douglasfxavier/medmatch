package bean;

import java.io.FileNotFoundException;
import java.io.FileWriter;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.swing.plaf.synth.SynthSeparatorUI;

import org.apache.commons.logging.Log;
import org.apache.jena.fuseki.embedded.FusekiServer;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

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
			String datasetURL = this.matchingBean.getUploadBean().getDatasetURL();
			OntologyManager ontologyManager = this.matchingBean.getOntologyBean().getOntologyManager();
			//RDFManager manager = new RDFManager();

			this.csvDataReaderBean.getCsvDataReader().loadData(country, 
					   drugCodeIndex, 
					   drugNameIndex, 
					   compoundNameIndex,
					   categoryIndex, 
					   manufacturerIDIndex, 
					   manufacturerNameIndex, 
					   strengthIndex);
	
			ObjectsToRDFConverter rdfConverter = new ObjectsToRDFConverter(datasetURL, ontologyManager);
			
			this.rdfModel = rdfConverter.convertDataToRDF(this.csvDataReaderBean.getCsvDataReader());
			
			FacesContext fc= FacesContext.getCurrentInstance();
			
			//Save as Turtle file
			String path = fc.getExternalContext().getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.ttl",country.getCountryName().replace('\n', '_')));
			FileWriter out1 = new FileWriter(path);
			rdfModel.write(out1,"turtle");
			out1.close();
			
			//manager.saveFile(rdfModel, path, "turtle");	
			
			//Save as JSON file
			path = fc.getExternalContext().getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.json",country.getCountryName().replace('\n', '_')));	
			FileWriter out2 = new FileWriter(path);
			rdfModel.write(out2,"JSON-LD");
			out2.close();
			
			//Save as RDF/XML file
			//Save as JSON file
			path = fc.getExternalContext().getRealPath(String.format("WEB-INF\\classes\\data\\Drugs_%s.rdf",country.getCountryName().replace('\n', '_')));
			FileWriter out4 = new FileWriter(path);
			rdfModel.write(out4,"RDF/XML");
			out2.close();
			
			//Dataset ds = DatasetFactory.create(rdfModel);
			//RDFDataMgr.write(System.out, ds, Lang.RDFXML);
			
			
			//manager.saveFile(rdfModel.write(out), path, "RDF/XML");	
			
/*			Dataset ds = DatasetFactory.create(rdfModel);
			FusekiServer server = FusekiServer.create()
				    .add(String.format("/%s", country.getCountryName().replace('\n', '_')),ds)
				    .build();
			server.start();		
			server.stop();*/
	
			return "conversion?faces-redirect=true";
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
