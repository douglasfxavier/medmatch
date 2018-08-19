package bean;

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
	@ManagedProperty (value ="#{csvDataReaderBean}")
	private CSVDataReaderBean csvDataReaderBean;
	
	
	public OntologyBean getOntologyBean() {
		return ontologyBean;
	}

	public void setOntologyBean(OntologyBean ontologyBean) {
		this.ontologyBean = ontologyBean;
	}
	
	public void setCsvDataReaderBean(CSVDataReaderBean csvDataReaderBean) {
		this.csvDataReaderBean = csvDataReaderBean;
	}

	public CSVDataReaderBean getCsvDataReaderBean() {
		CSVDataReader csvDataReader = new CSVDataReader(this.uploadBean.getCsvData(),'\t');
		this.csvDataReaderBean = new CSVDataReaderBean();
		this.csvDataReaderBean.setCsvDataReader(csvDataReader);
		return csvDataReaderBean;
	}

	public Country getSelectedCountry() {
		int numericCode = Integer.parseInt(uploadBean.getSelectedCountryNumericCode());
		Country selectedCountry = countryListBean.getCountry(numericCode);
		return selectedCountry;
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
	
	public String getTermLabel(String ontTerm) {
		return ontTerm.replace("_", " ");
	}
	
	public String getIndex(String ontTerm) {
		int index;
		index = ontologyBean.getOntologyTerms().indexOf(ontTerm);
		String indexString = String.valueOf(index);
		
		return indexString;
	}
	
}

