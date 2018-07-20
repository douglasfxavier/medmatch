package model;

public class VocabularyProperty {

	public String propertyName;
	public String propertyURI;
	
	public VocabularyProperty(String propertyName, String propertyURI){
		this.propertyName = propertyName;
		this.propertyURI = propertyURI;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyURI() {
		return propertyURI;
	}

	public void setPropertyURI(String propertyURI) {
		this.propertyURI = propertyURI;
	}


	
}
