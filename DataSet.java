package PackageExcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
/**
 * Clase que contiene todos los métodos necesarios para extraer la información de archivos .xls, modificarla, borrarla y agregarla.
 * 
 * 
 * @author Jazmin, Laura, Uziel
 *
 */

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;

import Exceptions.SizeOutOfBoundsException;
import Exceptions.ClassifierException;
import Exceptions.SizeArgumentsException;

/**
* Clase que contiene todos los métodos necesarios para manejar archivos .xls
* Extrae, cambia, elimina y agrega datos al archivo proporcionado.
* Se necesita una instancia de DataSet para clasificar datos.
*
* @author  Jazmín, Uziel, Laura
*/
public class DataSet 
{
	private String filepath;
	private int rows = 0;
	private int cols = 0;
	private int classColumn = 1;
	protected double[][] dataMatrix;
	protected List<String> cNames = new Vector<String>();
	private double missingValue;
	
	DataSet(String f) throws IOException
	{
		this.filepath = f;
		this.rows = getRows();
		this.cols = getColumns();
		saveMatrix();
	}
	
	DataSet(String f, int n) throws IOException
	{
		this.filepath = f;
		this.rows = getRows();
		this.cols = getColumns();
		this.classColumn = n;
		
		if(n != 1)
		{
			replace(n);
		}
		//saveMatrix();
	}
	
	DataSet(String f, int n, double v) throws IOException
	{
		this.filepath = f;
		this.rows = getRows();
		this.cols = getColumns();
		this.classColumn = n;
		
		fillVoids(v);
		
		if(n != 1)
		{
			replaceInts(n);
		}
		this.missingValue = v;
		convertString();
		//saveMatrix();
	}
	
	public List<String> getArrayNames() throws IOException
	{
		List<String> s = new ArrayList<String>();
		for(int i = 0; i < this.rows; i++) s.add(getClassName(i+1)); 
		return s;
	}
	
	public Sample[] getMatrixData() throws IOException
	{
		Sample[] S = new Sample[this.rows];
		int i = 0;
		for(double[] d : dataMatrix) {
			S[i] = new Sample(getArrayNames().get(i), d);
			i++;
		}		
		return S;
	}
	
	public void convertString() throws IOException
	{
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		
		CellAddress cellAddress = new CellAddress(0, 0);
		Row row = sheet.getRow(cellAddress.getRow());
		Cell cell = row.getCell(cellAddress.getColumn());
		
		for(int i = 0; i < this.rows; i++)
		{
			cellAddress = new CellAddress(i, 0);
			row = sheet.getRow(cellAddress.getRow());
			cell = row.getCell(cellAddress.getColumn());
			
			double b = cell.getNumericCellValue();
			String s = String.valueOf(b);
			cell.setCellValue(s);
		}
	}
	
	public void replaceInts(int x) throws IOException
	{
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		
		CellAddress cellAddress1 = new CellAddress(x-1, 0);
		Row row1 = sheet.getRow(cellAddress1.getRow());
		Cell cell1 = row1.getCell(cellAddress1.getColumn());
		
		CellAddress cellAddress2 = new CellAddress(x-1, 0);
		Row row2 = sheet.getRow(cellAddress2.getRow());
		Cell cell2 = row2.getCell(cellAddress2.getColumn());
		
		for(int j = x-1; j > 0; j--)
		{
			for(int i = 0; i < getRows(); i++)
			{
				cellAddress1 = new CellAddress(i, j);
				row1 = sheet.getRow(cellAddress1.getRow());
				cell1 = row1.getCell(cellAddress1.getColumn());
				
				cellAddress2 = new CellAddress(i, j-1);
				row2 = sheet.getRow(cellAddress2.getRow());
				cell2 = row2.getCell(cellAddress2.getColumn());
				
				double temp = cell1.getNumericCellValue();
				double temp2 = cell2.getNumericCellValue();
				
				cell1.setCellValue(temp2);
				cell2.setCellValue(temp);
			}
		}
		FileOutputStream out = new FileOutputStream(this.filepath, false);
	    wb.write(out);
	    out.close(); 
	}
	
	public void fillVoids(double v) throws IOException
	{
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		
		CellAddress cellAddress = new CellAddress(0, 0);
		Row row = sheet.getRow(cellAddress.getRow());
		Cell cell = row.getCell(cellAddress.getColumn());
		
		for(int i = 0; i < this.rows; i++)
		{
			for(int j = 0; j <= this.cols; j++)
			{
				cellAddress = new CellAddress(i, j);
				row = sheet.getRow(cellAddress.getRow());
				cell = row.getCell(cellAddress.getColumn());
				if(cell.getCellType() != cell.CELL_TYPE_NUMERIC)
				{
					cell.setCellValue(v);
				}
			}
		}
		
		FileOutputStream out = new FileOutputStream(this.filepath, false);
	    wb.write(out);
	    out.close();
	}
	
	@Override
	public String toString()
	{
		String s = "";
		try {
			for(int i = 0; i < getRows(); i++)
			{
				s = s + String.format("[%s]:\t [ ", getClassName(i+1));
				for(int j = 1; j <= getColumns(); j++)
				{
					s = s + String.format("%.1f ", getValue(i, j));
				}
				s = s + String.format("]\n");
			}
		} catch (IOException | SizeOutOfBoundsException e)
		{
			e.printStackTrace();
		}
		
		return s;
	}
	
	public List<String> getClassNames() throws IOException
	{
		Set<String> s = new HashSet<String>();
		for(int i = 0; i < this.rows; i++) s.add(getClassName(i+1)); 
        List<String> aList = new ArrayList<String>(s.size());
        for (String x : s) cNames.add(x);
		cNames.sort(null);
		return cNames;
	}
	
	public void printData(double[][] data)
	{
		for(double[] row : data)
		{
			System.out.println(Arrays.toString(row));
		}
	}
	
	public String getClassName(int r) throws IOException
	{
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		
		CellAddress cellAddress = new CellAddress(r-1, 0);
		Row row = sheet.getRow(cellAddress.getRow());
		Cell cell = row.getCell(cellAddress.getColumn());

		return cell.getStringCellValue();
	}
	
	public double getValue(int r, int c) throws IOException, SizeOutOfBoundsException
	{
		if(r > getRows()) throw new SizeOutOfBoundsException(r);
		if(c > getRows()) throw new SizeOutOfBoundsException(c);
		
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		
		CellAddress cellAddress = new CellAddress(r, c);
		Row row = sheet.getRow(cellAddress.getRow());
		Cell cell = row.getCell(cellAddress.getColumn());
		
		return cell.getNumericCellValue();
	}
	
	public void saveMatrix() throws IOException
	{	
		dataMatrix = new double[this.rows][this.cols];
		for(int i = 0; i < this.rows; i++)
		{
			for(int j = 1; j <= this.cols; j++)
			{
				try {
					dataMatrix[i][j-1] = getValue(i, j);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SizeOutOfBoundsException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public int getRows() throws IOException
	{
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		
		this.rows = sheet.getLastRowNum() + 1;
		return sheet.getLastRowNum() + 1;
	}
	
	public int getColumns() throws IOException
	{
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);

		int r = 0, c = 0;
		
		for(Row row: sheet)
		{  
			c = 0;
			for(Cell cell: row)	{  
				c++;
			}  
			r++;
		}
		
		this.cols = c-1;
		return c - 1;
	}
	
	public double get(int r, int c) throws IOException, SizeOutOfBoundsException
	{
		return getValue(r-1, c);
	}
	
	public void set(int r, int c, double value) throws IOException, SizeOutOfBoundsException
	{		
		
		if(r > getRows()) throw new SizeOutOfBoundsException(r);
		if(c > getColumns()) throw new SizeOutOfBoundsException(c);
		
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		
		CellAddress cellAddress = new CellAddress(r-1, c);
		Row row = sheet.getRow(cellAddress.getRow());
		Cell cell = row.getCell(cellAddress.getColumn());
		
		
		cell.setCellValue(value);
		
		FileOutputStream out = new FileOutputStream(this.filepath, false);
	    wb.write(out);
	    out.close();
	    
	    dataMatrix[r-1][c-1] = value;
	}
	
	public void addSample(Sample s) throws IOException, SizeArgumentsException
	{
		
		if(s.arr.length < getColumns()) throw new SizeArgumentsException(this.filepath, s.arr.length);
		
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		
		Row row = sheet.createRow((short)getRows());

		row.createCell(0).setCellValue(s.sampleClassName);  
	
		for(int i = 0; i < getColumns(); i++)
		{
			row.createCell(i+1).setCellValue(s.arr[i]);  
		}
		
		FileOutputStream out = new FileOutputStream(this.filepath, false);
	    wb.write(out);
	    out.close();    
	    
	    this.rows++;
	    saveMatrix();
	}
	
	public double getMinimum(int c) throws IOException
	{
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		
		CellAddress cellAddress = new CellAddress(0, c);
		Row row = sheet.getRow(cellAddress.getRow());
		Cell cell = row.getCell(cellAddress.getColumn());
		
		double min = cell.getNumericCellValue();
	
		for(int j = 0; j < getRows(); j++)
		{
			cellAddress = new CellAddress(j, c);
			row = sheet.getRow(cellAddress.getRow());
			cell = row.getCell(cellAddress.getColumn());
			
			if(cell.getNumericCellValue() < min) min = cell.getNumericCellValue();
			
		}
		
		return min;
	}
	
	public double getMaximum(int c) throws IOException
	{
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		
		CellAddress cellAddress = new CellAddress(0, c);
		Row row = sheet.getRow(cellAddress.getRow());
		Cell cell = row.getCell(cellAddress.getColumn());
		
		double max = cell.getNumericCellValue();
	
		for(int j = 0; j < getRows(); j++)
		{
			cellAddress = new CellAddress(j, c);
			row = sheet.getRow(cellAddress.getRow());
			cell = row.getCell(cellAddress.getColumn());
			
			if(cell.getNumericCellValue() > max) max = cell.getNumericCellValue();
			
		}
		
		return max;
	}
	
	public void removeRow(int x) throws IOException
	{
		removeRows(x, x);
	}
	
	public void removeRows(int x, int y) throws IOException
	{
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		x -= 1;
		y -= 1;
		int lastIndex;
		
		for(int i = x; i <= y; i++)
		{
			Row row = sheet.getRow(i);
			sheet.removeRow(row);
			/*lastIndex = sheet.getLastRowNum();
		    sheet.shiftRows(x + 1, lastIndex, -1);*/
		}
		
		FileOutputStream out = new FileOutputStream(this.filepath, false);
	    wb.write(out);
	    out.close();
	    
	    
	    this.rows--;
	}
	
	public void removeColumns(int x, int y) throws IOException
	{
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		
		CellAddress cellAddress = new CellAddress(0, x);
		Row row = sheet.getRow(cellAddress.getRow());
		Cell cell = row.getCell(cellAddress.getColumn());
		
		for(int i = x; i <= y; i++)
		{
			for(int j = 0; j < getRows(); j++)
			{
				cellAddress = new CellAddress(j, i);
				row = sheet.getRow(cellAddress.getRow());
				cell = row.getCell(cellAddress.getColumn());
				if(cell != null)
				{
					Row r = sheet.getRow(j);
					r.removeCell(cell);
				}	
			}
		}
		
		FileOutputStream out = new FileOutputStream(this.filepath, false);
	    wb.write(out);
	    out.close();
	    
	    sheet.autoSizeColumn(this.cols - ((y+1) - x));
	    this.cols = getColumns();
	}
	
	public void replace(int x) throws IOException
	{
		FileInputStream f = new FileInputStream(new File(this.filepath));   
		HSSFWorkbook wb = new HSSFWorkbook(f);   
		HSSFSheet sheet = wb.getSheetAt(0);
		
		CellAddress cellAddress1 = new CellAddress(x-1, 0);
		Row row1 = sheet.getRow(cellAddress1.getRow());
		Cell cell1 = row1.getCell(cellAddress1.getColumn());
		
		CellAddress cellAddress2 = new CellAddress(x-1, 0);
		Row row2 = sheet.getRow(cellAddress2.getRow());
		Cell cell2 = row2.getCell(cellAddress2.getColumn());
		
		for(int j = x-1; j > 0; j--)
		{
			for(int i = 0; i < getRows(); i++)
			{
				cellAddress1 = new CellAddress(i, j);
				row1 = sheet.getRow(cellAddress1.getRow());
				cell1 = row1.getCell(cellAddress1.getColumn());
				
				cellAddress2 = new CellAddress(i, j-1);
				row2 = sheet.getRow(cellAddress2.getRow());
				cell2 = row2.getCell(cellAddress2.getColumn());
				
				String temp = cell1.getStringCellValue();
				double temp2 = cell2.getNumericCellValue();
				
				cell1.setCellValue(temp2);
				cell2.setCellValue(temp);
			}
		}
		FileOutputStream out = new FileOutputStream(this.filepath, false);
	    wb.write(out);
	    out.close(); 
	}
	
	public boolean isRowEmpty(Row row) 
	{
		boolean isEmpty = true;
		DataFormatter dataFormatter = new DataFormatter();

		if (row != null) {
			for (Cell cell : row) {
				if (dataFormatter.formatCellValue(cell).trim().length() > 0) 
				{
					isEmpty = false;
					break;
				}
			}
		}

		return isEmpty;
	}
	
	public void normalize() throws IOException {
		for(int f = 0; f < this.rows; f++) {
			for(int c = 0; c < this.cols; c++) {
				double x = this.dataMatrix[f][c];
				this.dataMatrix[f][c] = (x - getMinimum(c)) / (getMaximum(c) - getMinimum(c));
			}
		}
		saveMatrix();
	}
}