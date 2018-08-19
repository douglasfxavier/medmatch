package bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.FaceletContext;
import javax.servlet.http.Part;


@ManagedBean (name = "uploadBean")
@SessionScoped
public class UploadBean {
	private Part uploadedfile;
	private String csvData;
	private String datasetURL;
	private String selectedCountryNumericCode;

	public Part getUploadedfile() { 	
		return uploadedfile;
	}

		
	public String getCsvData() {
		return csvData;
	}


	public String getDatasetURL() {
		return datasetURL;
	}
	public void setDatasetURL(String url) {
		this.datasetURL = url;
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
	public String uploadFile() throws IOException{
		
		
	     if (this.uploadedfile != null) {
	            try {
	                InputStream inputStream = this.uploadedfile.getInputStream();
	                this.csvData = new Scanner(inputStream,"utf-8").useDelimiter("\\A").next();
	            } catch (IOException ex) {
	            	String msgtext = "No file was selected.";
	            	FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,msgtext,null);
	            	FacesContext facesContext = FacesContext.getCurrentInstance();
	            	facesContext.addMessage("upload-form:dataset-file", msg);
	            }
	        }
		
		return "matching?faces-redirect=true";
		
	}

	
	
	
}
