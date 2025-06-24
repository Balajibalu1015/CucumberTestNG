package org.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
	static Map<String, String> map = new HashMap<String, String>();
	
	public Map<String, String> getData(String fileName, String sheetName) throws IOException {
		FileInputStream input = new FileInputStream(new File(fileName));
		@SuppressWarnings("resource")
		Workbook workBook = new XSSFWorkbook(input);
			Sheet sheet = workBook.getSheet(sheetName);
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				for (int j = 0; j < row.getLastCellNum()-1; j++) {
					map.put(row.getCell(j).getStringCellValue(), row.getCell(j+1).getStringCellValue());	
				}
			}
		
		return map;
	}
	
	public Map<String, String> getData(String fileName, String sheetName, int j) throws IOException {
		FileInputStream input = new FileInputStream(new File(fileName));
		try (Workbook workBook = new XSSFWorkbook(input)) {
			Sheet sheet = workBook.getSheet(sheetName);
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
					map.put(row.getCell(0).getStringCellValue(), row.getCell(j).getStringCellValue());	
			}
		}
		return map;
	}

}
