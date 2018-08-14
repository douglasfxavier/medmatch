package bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.Part;


@ManagedBean (name = "uploadBean")
@SessionScoped
public class UploadBean {
	private Part uploadedfile;
	private String csvData;
	private String url;
	private String selectedCountryNumericCode;

	public Part getUploadedfile() { 	
		return uploadedfile;
	}

		
	public String getCsvData() {
		return csvData;
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
	
	@SuppressWarnings("resource")
	public String uploadFile() {
		
	     if (this.uploadedfile != null) {
	            try {
	                InputStream inputStream = this.uploadedfile.getInputStream();
	                this.csvData = new Scanner(inputStream).useDelimiter("\\A").next();
	            } catch (IOException ex) {
	            }
	        }
		
		return "matching?faces-redirect=true";
		
	}

	
	
	
}
