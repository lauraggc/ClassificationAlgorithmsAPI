package PackageExcel;

import java.io.IOException;

import Exceptions.*;

/**
 * Esta clase es la que unira todas las demas clases del metodo, es la encargada de guardar la informacion del dataSet
 * ademas de la creacion de los metodos abstractos que dependiendo del algoritmo se sobreescribiran.
 * 
 * 
 * @author Jazmin, Laura, Uziel
 *
 */
public abstract class Classifier 
{
	protected int k;
	protected DataSet ds;
	
	public void setK(int k) throws ClassifierException
	{
		if(k < 1) throw new ClassifierException(k);
		this.k = k;
	}
	
	static class Resultados{
		double Distancia;
		String Clase;
		public Resultados(double dis, String Clase) {
			this.Distancia = dis;
			this.Clase = Clase;
		}
		@Override
		public String toString() {
			return "[" + this.Distancia + "] " + this.Clase;
		}
	}
	
	public abstract void setDistance(String s);
	public abstract String classify(Sample s) throws IOException;
	
	public void setDataset(DataSet d)
	{
		this.ds = d;
	}
	
}
