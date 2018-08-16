package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import controller.CSVDataReader;

@ManagedBean (name = "csvDataReaderBean")
@SessionScoped
public class CSVDataReaderBean {
	
	private CSVDataReader csvDataReader;
	
	public CSVDataReader getCsvDataReader() {
		return csvDataReader;
	}
	
	public void setCsvDataReader(CSVDataReader csvDataReader){
		this.csvDataReader = csvDataReader;
	}
}
