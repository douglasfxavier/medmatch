package bean;


import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;

import model.Country;

@ManagedBean (name = "uploadBean")
@SessionScoped
public class UploadBean {
	private Part uploadedfile;
	private String url;
	private String selectedCountryNumericCode;

	public Part getUploadedfile() {
		return uploadedfile;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public void setSelectedCountryNumericCode(String selectedCountryNumericCode) {
		this.selectedCountryNumericCode = selectedCountryNumericCode;
	}

	public String getSelectedCountryNumericCode() {
		return selectedCountryNumericCode;
	}
	
	public void setUploadedfile(Part uploadedfile) {
		this.uploadedfile = uploadedfile;
	}
	
	public String uploadFile() {
		
		return "matching?faces-redirect=true";
		
	}

	
	
	
}
