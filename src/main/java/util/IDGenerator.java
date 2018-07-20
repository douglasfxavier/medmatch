package util;

public class IDGenerator {
	protected int generatedID;

	public IDGenerator() {
		generatedID = 0;
	}
	
	public int newId() {
		return ++this.generatedID;
	}
	
}
