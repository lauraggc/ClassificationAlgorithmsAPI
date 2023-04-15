package PackageExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;

import Exceptions.ClassifierException;
import Exceptions.SizeArgumentsException;
import Exceptions.SizeOutOfBoundsException;

/**
* Test para la presentación en clase
*
* @author  Jazmín, Uziel, Laura
*/
public class Tests
{
	public static void main(String[] args) throws IOException, SizeOutOfBoundsException, ClassifierException, SizeArgumentsException 
	{	
		/*
		DataSet ds1 = new DataSet("colores.xls");  // default class column = 1
		int ro = ds1.getRows(); // ro = 8
		int co  = ds1.getColumns(); // co = 3, sólo las columnas NO clase
		System.out.println("Rows: " + ro + ", Columns: " + co);
		
		System.out.println("Original value: " + ds1.get(7, 2));
		
		ds1.set(7, 2, 42); // cambia 40 por 42
		System.out.println("Altered value: " + ds1.get(7, 2));
		//ds1.set(9, 2, 42); // Excepción: no existe fila 9
		
		//ds1.addSample(new Sample("YELLOW", 50, 255, 150));
		System.out.println("Rows " + ds1.getRows()); // ro = 9
		//ds1.addSample(new Sample("YELLOW", 253, 251)); // Excepción: se esperan 3 datos numéricos para este dataset
		
		//List<String> classNames = ds1.getClassNames(); // classNames = ["GREEN", "VIOLET", YELLOW"]
		//System.out.println(classNames);
		
		double min = ds1.getMinimum(1); // 4, mínimo valor en columna 1
		System.out.println("Min: " + min);
		double max = ds1.getMaximum(2); // 255, máximo valor en columna 2
		System.out.println("Max: " + max);
		
		Classifier knn = new KnnClassifier(5); // K = 5, default: Euclidean distance
		//knn.setK(0); // Excepción: K >= 1
		knn.setK(4); // K = 4
		
		knn.setDistance(KnnClassifier.EUCLIDEAN);
		      
		Sample s1 = new Sample (1, 255, 1);
		knn.setDataset(ds1);
		String cl1 = knn.classify(s1); 
		System.out.println("Class assigned: " + cl1);
		
		*/
		
		Classifier knn2 = new KnnClassifier(7);
		
		Sample s2 = new Sample (5.9, 3, 5.1, 1.8);
		
		int classColumn = 5;
		DataSet ds2 = new DataSet("iris.xls", classColumn);

		ds2.removeColumns(5, 6);  // de la 5 a la 6
		ds2.removeRows(149, 150); // de la 149 a la 150
		ds2.addSample(new Sample("Iris-setosa", 5.2, 3.5, 1.4, 0.2));
		ds2.addSample(new Sample("Iris-versicolor", 7.1, 3.2, 4.7, 1.3));
		//ds2.addSample(new Sample("Iris-versicolor", 7.1, 3.2, 1.3));  // Excepción: el sample a añadir debe incluir 4 datos numéricos
		knn2.setDataset(ds2);
		knn2.setDistance(KnnClassifier.EUCLIDEAN);
		String cl2 = knn2.classify(new Sample(5.1, 3.5, 1.4, 0.2));  // cl2 = "Iris-setosa"
		System.out.println(cl2);
		//String cl3 = knn.classify(new Sample(5.1, 3.5));  // cl2 = Excepción: el sample a clasificar debe incluir 4 datos numéricos
		
	}
}
