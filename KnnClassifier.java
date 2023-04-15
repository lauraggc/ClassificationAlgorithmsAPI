package PackageExcel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Exceptions.*;

/**
 * KnnClassifier permite realizar el algoritmo de los vecinos K mas cercanos
 * esto a través de varios metodos, entre ellos los encargados de calcular la distancia desde el nuevo punto a los puntos
 * ya obtenidos del dataset, los encargados de encontrar segun estas distancias los k vecinos mas cercanos
 * y por ultimo el encargado de encontrar cual de estos vecinos se repite mas de entre los mas cercanos
 * para poder de ahi seleccionar la clase del nuevo objeto
 * @author Jazmin, Laura, Uziel
 *
 */
public class KnnClassifier extends Classifier 
{
	protected static int distancia = 1;
	protected static String EUCLIDEAN = "EUCLIDEAN"; // 1
	protected static String MANHATTAN = "MANHATTAN"; // 2
	protected static String CHEBYSHEV = "CHEBYSHEV"; // 2
	
	KnnClassifier(int k)
	{
		super.k = k;
	}
	
	@Override
	public void setDistance(String s) 
	{
		switch(s)
		{
			case "EUCLIDEAN": distancia = 1; break;
			case "MANHATTAN": distancia = 2; break;
			case "CHEBYSHEV": distancia = 3; break;
			default: distancia = 1;
		}
	}
	
	static class getDistance implements Comparator<Resultados>{
		@Override
		public int compare(Resultados o1, Resultados o2) {
			return o1.Distancia < o2.Distancia ? -1 : o2.Distancia == o1.Distancia ? 0 : 1;
		}
	}
	
	public static String findMajorityClass(String[] neigh, List<String> fNeigh) {
		String[] values = fNeigh.toArray(new String[0]);
		int[] cont = new int[values.length];
		int MAX = 0;
		for(int i = 0; i < fNeigh.size() ; i++) {
			cont[i] = 0;
			for(int j = 0; j < neigh.length; j++) {
				if(values[i].equals(neigh[j])) {
					cont[i]++;
					if(MAX < cont[i]) {
						MAX = cont[i];
					}
				}
			}
		}
		
		for(int i = 0; i < neigh.length ; i++) {
			if(cont[i] == MAX) return neigh[i].toString();
		}
		return null;			
	}
	
	
	private List<Resultados> dist = new ArrayList<Resultados>();
	
	public String classify(Sample s) throws IOException
	{
		dist.clear();
		switch(distancia) {
		case 1: EuclideanDistance(ds.getMatrixData(), s, dist); break;
		case 2: ManhattanDistance(ds.getMatrixData(), s, dist); break;
		case 3: ChebyshevDistance(ds.getMatrixData(), s, dist); break;
		default: EuclideanDistance(ds.getMatrixData(), s, dist);
		}
		
		Collections.sort(dist, new getDistance());
		String[] neighbors = new String [k]; 
		for(int i = 0; i < k; i++) {
			neighbors[i] = dist.get(i).Clase;
		}	
		
		return findMajorityClass(neighbors, ds.getClassNames());
	}
	
	public static void EuclideanDistance(Sample[] dataList, Sample newData, List<Resultados> dists) {
		double x;
		double root;
		int cont = 0;
		
		for(Sample S: dataList) {
			x = 0.0;
			cont = 0;
			for(double d: S.getArr()) {
				x += Math.pow((d - newData.getArr()[cont]), 2);
				cont++;
		}
			root = Math.sqrt(x);
			dists.add(new Resultados(root, S.getClassN()));
		}
		/*
		for(double[] d: ) {
			x = 0.0;
			for(int i = 0; i < d.length; i++) {
				x += Math.pow((d[i] - newData[i]), 2);
			}*/
		}
	
	public static void ManhattanDistance(Sample[] dataList, Sample newData, List<Resultados> dists) {
		double x;
		int cont = 0;
		double act = 0;
		for(Sample S: dataList) {
			x = 0.0;
			cont = 0;
			for(double d: S.getArr()) {
				x += (Math.abs(newData.getArr()[cont] - d));
				cont++;
		}
			dists.add(new Resultados(x, S.getClassN()));
		}
	}
	
	public static void ChebyshevDistance(Sample[] dataList, Sample newData, List<Resultados> dists) {
		double x;
		double root;
		int cont = 0;
		
		for(Sample S: dataList) {
			x = 0.0;
			cont = 0;
			for(double d: S.getArr()) {
				x += Math.max(x,(Math.abs(newData.getArr()[cont] - d)));
				cont++;
		}
			root = Math.sqrt(x);
			dists.add(new Resultados(root, S.getClassN()));
		}
	}
}