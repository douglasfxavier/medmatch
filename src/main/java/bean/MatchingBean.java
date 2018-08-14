package bean;

import java.io.IOException;
import java.util.HashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import controller.CSVDataReader;
import model.Country;

@ManagedBean ( name = "matchingBean")
@SessionScoped
public class MatchingBean{
	@ManagedProperty (value ="#{uploadBean}")
	private UploadBean uploadBean;
	@ManagedProperty (value = "#{countryListBean}")
	private CountryListBean countryListBean;
	@ManagedProperty (value = "#{ontologyBean}")
	private OntologyBean ontologyBean;
	private HashMap<String,String> mathings; 
	private CSVDataReader csvDataReader;
	

	public OntologyBean getOntologyBean() {
		return ontologyBean;
	}

	public void setOntologyBean(OntologyBean ontologyBean) {
		this.ontologyBean = ontologyBean;
	}



	public CSVDataReader getCsvDataReader() throws IOException {
		this.csvDataReader = new CSVDataReader(this.uploadBean.getCsvData(),'\t');
		return csvDataReader;
	}


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
	
	
/*	public ArrayList<String> loadMetadata() {
		OntologyManager ontologyManager = new OntologyManager();
		OntModel ontologyModel = ontologyManager.getOntologyModel();
		
		
		//ObjectsToRDFConverter objectsToRDFConverter = new ObjectsToRDFConverter(uploadBean.getUrl());
		return ontologyManager.getClassesNames(ontologyModel);

	}*/
}

