package Exceptions;

/**
* Excepci�n que sale cuando se intenta declarar k como 0. 
*
* @author  Jazm�n, Uziel, Laura
*/
public class ClassifierException extends Exception{

	private int classifier;
	
	public ClassifierException(int classifier) {
		super("ClassifierException");
		this.classifier = classifier;
	}
	
	public String toString() {
		return String.format("%s\nNumber k must be greater than 0: %d", getMessage(), classifier);
	}
}
