package Exceptions;

/**
* Excepción que sale cuando se intenta declarar k como 0. 
*
* @author  Jazmín, Uziel, Laura
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
