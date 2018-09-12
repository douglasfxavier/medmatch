package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import model.Country;

@ManagedBean ( name = "matchingBean")
@SessionScoped
public class MatchingBean{
	@ManagedProperty (value ="#{uploadBean}")
	private UploadBean uploadBean;
	@ManagedProperty (value = "#{ontologyBean}")
	private OntologyBean ontologyBean;
	@ManagedProperty (value = "#{countryListBean}")
	private CountryListBean countryListBean;
	
	
	public OntologyBean getOntologyBean() {
		return this.ontologyBean;
	}

	public void setOntologyBean(OntologyBean ontologyBean) {
		this.ontologyBean = ontologyBean;
	}
		
	public Country getSelectedCountry() {
		int numericCode = Integer.parseInt(this.uploadBean.getSelectedCountryNumericCode());
		Country selectedCountry = this.countryListBean.getCountry(numericCode);
		return selectedCountry;
	}

	public CountryListBean getCountryListBean() {
		return this.countryListBean;
	}


	public void setCountryListBean(CountryListBean countryListBean) {
		this.countryListBean = countryListBean;
	}


	public UploadBean getUploadBean() {
		return this.uploadBean;
	}


	public void setUploadBean(UploadBean uploadBean) {
		this.uploadBean = uploadBean;
	}
	
	public String getTermLabel(String ontTerm) {
		return ontTerm.replace("_", " ");
	}
	
	public String getIndex(String ontTerm) {
		int index;
		index = this.ontologyBean.getOntologyTerms().indexOf(ontTerm);
		String indexString = String.valueOf(index);
		
		return indexString;
	}
	
}

