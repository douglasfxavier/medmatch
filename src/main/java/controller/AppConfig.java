package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.faces.context.FacesContext;

public class AppConfig {
	public static String getProperty(String propertyName) throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String path = facesContext.getExternalContext().getRealPath("WEB-INF\\classes\\");
		properties.load(new FileInputStream(path + "/config.properties"));
		
		return properties.getProperty(propertyName); 
	}
}
