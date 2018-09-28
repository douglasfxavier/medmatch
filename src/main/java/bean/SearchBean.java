package bean;

import java.io.IOException;
import java.util.HashMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import org.apache.jena.rdf.model.Model;

import controller.RDFManager;
import model.Country;
import util.FusekiConnector;

@ManagedBean (name = "searchBean")
public class SearchBean {
	@ManagedProperty (value = "#{countryListBean}")
	private CountryListBean countryListBean;
	private String selectedOriginCountryNumericCode;
	private String selectedTargetCountryNumericCode;
	private String activeIngredientInput;
	
	public String getSelectedOriginCountryNumericCode() {
		return selectedOriginCountryNumericCode;
	}
	
	public void setSelectedOriginCountryNumericCode(String selectedOriginCountryNumericCode) {
		this.selectedOriginCountryNumericCode = selectedOriginCountryNumericCode;
	}
	
	public String getSelectedTargetCountryNumericCode() {
		return selectedTargetCountryNumericCode;
	}
	
	public void setSelectedTargetCountryNumericCode(String selectedTargetCountryNumericCode) {
		this.selectedTargetCountryNumericCode = selectedTargetCountryNumericCode;
	}

	public String getActiveIngredientInput() {
		return activeIngredientInput;
	}

	public void setActiveIngredientInput(String activeIngredientInput) {
		this.activeIngredientInput = activeIngredientInput;
	}		
	
	public CountryListBean getCountryListBean() {
		return countryListBean;
	}

	public void setCountryListBean(CountryListBean countryListBean) {
		this.countryListBean = countryListBean;
	}

	public String search() throws IOException {
		int originCountryCode = Integer.parseInt(selectedOriginCountryNumericCode);
		Country originCountry = this.countryListBean.getCountry(originCountryCode);
		//int targetCountryCode = Integer.parseInt(selectedTargetCountryNumericCode);
		//Country targetCountry = this.countryListBean.getCountry(targetCountryCode);
	
		FusekiConnector fusekiConnector = new FusekiConnector();
	
		HashMap<String, String> originCountryServices = fusekiConnector.getDatasetDetailsByCountry(originCountry);
		String originCountryDumpService = originCountryServices.get("dumpService");
		Model originCountryRDFmodel = fusekiConnector.dumpData(originCountryDumpService);
		
		FacesContext facesContext= FacesContext.getCurrentInstance();
		String path = facesContext.getExternalContext().getRealPath("WEB-INF\\classes\\data\\teste.ttl");
		RDFManager manager = new RDFManager();
		manager.saveFile(originCountryRDFmodel, path, "TURTLE","http://www.anvisa.gov.br/");
		
		//System.out.println(originCountryRDFmodel);
/*		HashMap<String, String> targetCountryServices = fusekiConnector.getDatasetDetailsByCountry(targetCountry);
		String targetCountryDumpService = targetCountryServices.get("dumpService");
		Model targetCountryRDFmodel = fusekiConnector.dumpData(targetCountryDumpService);
		*/
		return null;
	}
	

}
