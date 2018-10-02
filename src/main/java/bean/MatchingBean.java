package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import controller.OntologyManager;
import model.Country;

@ManagedBean ( name = "matchingBean")
@SessionScoped
public class MatchingBean{
	@ManagedProperty (value ="#{uploadBean}")
	private UploadBean uploadBean;
	@ManagedProperty (value = "#{countryListBean}")
	private CountryListBean countryListBean;

		
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

}

