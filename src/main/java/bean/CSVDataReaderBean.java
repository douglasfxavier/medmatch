package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import controller.CSVDataReader;
import util.CSVDelimiter;

@ManagedBean (name = "csvDataReaderBean")
@SessionScoped
public class CSVDataReaderBean {
	@ManagedProperty (value = "#{uploadBean}")
	private UploadBean uploadBean;
	private CSVDataReader csvDataReader;
	
	public UploadBean getUploadBean() {
		return uploadBean;
	}

	public void setUploadBean(UploadBean uploadBean) {
		this.uploadBean = uploadBean;
	}

	public CSVDataReader loadCSVDataReader() {
		
		String selectedDelimiter = this.uploadBean.getSelectedDelimiter();
		char characterDelimiter = CSVDelimiter.valueOf(selectedDelimiter).getDelimiter().charAt(0);
		this.csvDataReader = new CSVDataReader(this.uploadBean.getCsvData(),characterDelimiter);
		
		return this.csvDataReader;
	}
		
	public CSVDataReader getCsvDataReader() {
		return csvDataReader;
	}

	public void setCsvDataReader(CSVDataReader csvDataReader){
		this.csvDataReader = csvDataReader;
	}
	
	
}