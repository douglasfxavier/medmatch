package bean;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.jena.ontology.OntModel;

import controller.ObjectsToRDFConverter;
import controller.OntologyManager;
import model.Country;

@ManagedBean ( name = "matchingBean")
@SessionScoped
public class MatchingBean{
	@ManagedProperty (value ="#{uploadBean}")
	private UploadBean uploadBean;
	@ManagedProperty (value = "#{countryListBean}")
	private CountryListBean countryListBean;
	private HashMap<String,String> mathings;
	

	public Country getSelectedCountry() {
		int numericCode = Integer.parseInt(uploadBean.getSelectedCountryNumericCode());
		Country selectedCountry = countryListBean.getCountry(numericCode);
		return selectedCountry;
	}


	public HashMap<String, String> getMathings() {
		return mathings;
	}

	public void setMathings(HashMap<String, String> mathings) {
		this.mathings = mathings;
	}
	

	public CountryListBean getCountryListBean() {
		return countryListBean;
	}


	public void setCountryListBean(CountryListBean countryListBean) {
		this.countryListBean = countryListBean;
	}


	public UploadBean getUploadBean() {
		return uploadBean;
	}


	public void setUploadBean(UploadBean uploadBean) {
		this.uploadBean = uploadBean;
	}


	public String convert() {
		return "Converted";
	}
	
	
/*	public String getDatasetFile() {
		String content = null;
		
        if (uploadedfile != null) {
            try {
                InputStream inputStream = uploadedfile.getInputStream();
                content = new Scanner(inputStream).nextLine();
            } catch (IOException ex) {
            }
        }
        return content;
    }*/
	
	
	public ArrayList<String> loadMetadata() {
		OntologyManager ontologyManager = new OntologyManager();
		OntModel ontologyModel = ontologyManager.getOntologyModel();
		
		
		//ObjectsToRDFConverter objectsToRDFConverter = new ObjectsToRDFConverter(uploadBean.getUrl());
		return ontologyManager.getClassesNames(ontologyModel);

	}
}

