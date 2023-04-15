package Exceptions;

/**
* Excepción que sale cuando el tamaño de argumentos es mayor al esperado.
*
* @author  Jazmín, Uziel, Laura
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
