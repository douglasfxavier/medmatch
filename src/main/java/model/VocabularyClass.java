package model;

public class VocabularyClass {

	public String className;
	public String classURI;
	
	public VocabularyClass(String className, String classURI) {
		super();
		this.className = className;
		this.classURI = classURI;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassURI() {
		return classURI;
	}

	public void setClassURI(String classURI) {
		this.classURI = classURI;
	}	
}
