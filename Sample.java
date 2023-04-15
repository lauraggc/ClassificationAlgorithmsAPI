package PackageExcel;

/**
* Sets de datos que se añaden a un DataSet o que se ingresa para ser clasificado por algún algoritmo. 
* Recibe un número no definido de variables tipo double. El constructor que tiene un String es para usarlo como addSample()
* y poder añadir datos a un DataSet. El constructor que no tiene un String es para meter los datos para que se les asigne una clase.
*
* @author  Jazmín, Uziel, Laura
*/
public class Sample 
{
	protected String sampleClassName;
	protected double[] arr;
	
	Sample(String sampleName, double ...ds)
	{
		this(ds);
		this.sampleClassName = sampleName;
	}
	
	Sample(double ...ds)
	{
		this.arr = new double[ds.length];
		
		for(int i = 0; i < ds.length; i++)
		{
			this.arr[i] = ds[i];
		}
	}
	
	public double[] getArr() {
		return this.arr;
	}
	
	public String getClassN() {
		return this.sampleClassName;
	}
	}