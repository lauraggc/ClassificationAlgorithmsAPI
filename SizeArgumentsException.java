package Exceptions;

/**
* Excepci�n que sale cuando no se ingres� el n�mero de m�todos esperado. 
*
* @author  Jazm�n, Uziel, Laura
*/
public class SizeArgumentsException extends Exception{
	
	private int sizeArguments;
	private String filename;
	
	public  SizeArgumentsException(String pathFile, int sizeArguments) {
		super("SizeArgumentsException");
		this.sizeArguments = sizeArguments;
		this.filename = pathFile;
	}
	
	public String toString() {
		return String.format("%s\nClass Method expects more/less arguments for %s: %d", getMessage(), filename, sizeArguments);
	}

}
