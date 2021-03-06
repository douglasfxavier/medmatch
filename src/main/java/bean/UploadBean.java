package bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.Part;

import util.CSVDelimiter;

@ManagedBean(name = "uploadBean")
@RequestScoped
public class UploadBean {
	private Part uploadedfile;
	private String csvData;
	private String selectedDelimiter;
	private String namespaceIRI;
	private String datasetName;
	private String selectedCountryNumericCode;

	public CSVDelimiter[] getCsvDelimiters() {
		return CSVDelimiter.values();
	}

	public String getSelectedDelimiter() {
		return selectedDelimiter;
	}

	public void setSelectedDelimiter(String selectedDelimiter) {
		this.selectedDelimiter = selectedDelimiter;
	}

	public Part getUploadedfile() {
		return uploadedfile;
	}

	public String getCsvData() {
		return csvData;
	}

	public String getNamespaceIRI() {
		return this.namespaceIRI;
	}

	public void setNamespaceIRI(String namespaceIRI) {
		String namespaceIRIwithoutHTTP = namespaceIRI.replace("http://", "");
		this.namespaceIRI = namespaceIRIwithoutHTTP;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
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
	public String uploadFile() throws IOException {
		
		Flash flash = FacesContext.getCurrentInstance()
						.getExternalContext().getFlash();
			
		if (this.uploadedfile != null) {
			try {
				InputStream inputStream = this.uploadedfile.getInputStream();
				this.csvData = new Scanner(inputStream, "utf-8").useDelimiter("\\A").next();
			} catch (IOException ex) {
				String msgtext = "No file was selected.";
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msgtext, null);
				FacesContext facesContext = FacesContext.getCurrentInstance();
				facesContext.addMessage(null, msg);
			}
		}

		flash.put("csvData",csvData);
		flash.put("selectedDelimiter",selectedDelimiter);
		flash.put("namespaceIRI",namespaceIRI);
		flash.put("datasetName",datasetName);
		flash.put("selectedCountryNumericCode",selectedCountryNumericCode);
		
		return "matching?faces-redirect=true";

	}

}
