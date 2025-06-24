package dataProvider;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.base.ExcelUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataProvider {

	Map<String, String> map;
	public static JSONObject jsonObject;
	
	public Map<String, String> dataProvider() throws IOException {
		ExcelUtils excelUtils = new ExcelUtils();
		map = excelUtils.getData(System.getProperty("user.dir") + "\\src\\test\\resources\\Config\\TestDataSheet.xlsx", "Login");
//		map = excelUtils.getData(System.getProperty("user.dir") + "\\src\\test\\resources\\Config\\TestDataSheet.xlsx", "KT");
		return map;
	}
	
	public JSONObject jsonFileReader() throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		jsonObject = (JSONObject) jsonParser.parse(new FileReader(new File(System.getProperty("user.dir") + "\\src\\test\\resources\\Config\\TestData.json")));
		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getJsonList(String listKey) {
		List<String> list = new ArrayList<>();
		JSONArray jsonArray = (JSONArray) jsonObject.get(listKey);
		list.addAll(jsonArray);
		return list;
	}
	
	public String getJsonMapValue(String key) {
		String value = (String)jsonObject.get(key);
		return value;
	}
}
