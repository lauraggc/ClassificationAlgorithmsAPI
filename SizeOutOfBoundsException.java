package Exceptions;

/**
* Excepci�n que sale cuando el tama�o de argumentos es mayor al esperado.
*
* @author  Jazm�n, Uziel, Laura
*/
public class SizeOutOfBoundsException extends Exception {

	private int sizeOutOfBounds;
	
	public SizeOutOfBoundsException(int sizeOutOfBounds) {
		super("SizeOutOfBoundsException");
		this.sizeOutOfBounds = sizeOutOfBounds;
	}
	
	public String toString() {
		return String.format("%s\nSize out of bounds found: %d", getMessage(), sizeOutOfBounds);
	}

}
