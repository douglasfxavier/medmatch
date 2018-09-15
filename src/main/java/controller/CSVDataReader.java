package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import model.Category;
import model.Compound;
import model.Country;
import model.Drug;
import model.Manufacturer;
import util.IDGenerator;

public class CSVDataReader {
	protected ArrayList<Drug> drugsList = new ArrayList<Drug>();
	protected ArrayList<Compound> compoundList = new ArrayList<Compound>();
	protected ArrayList<Manufacturer> manufacturerList = new ArrayList<Manufacturer>();
	protected ArrayList<Category> categoryList = new ArrayList<Category>();
	protected IDGenerator compoundIDGenerator = new IDGenerator();
	protected IDGenerator categoryIDGenerator = new IDGenerator();
	protected IDGenerator manufacturerIDGenerator = new IDGenerator();
	protected String csvData;
	protected char delimiter;
	protected CSVParser parser;
	protected CSVReader reader;
	

	public CSVDataReader (String csvData, char delimiter) {
		this.csvData = csvData;
		this.delimiter = delimiter;
		this.parser = new CSVParserBuilder()
						.withSeparator(this.delimiter)
						.withIgnoreQuotations(true)
						.build();
		this.reader = new CSVReaderBuilder(new StringReader(this.csvData))
						.withSkipLines(0)
						.withCSVParser(parser)
						.build();		
	}

	public HashMap<String, String> getMetadata() throws FileNotFoundException{
		try {
			String[] header = this.reader.readNext(); 
		
			HashMap<String, String>  list = new HashMap<String, String>();
			
			for (int i=0; i < header.length; i++) {
				list.put(header[i], String.valueOf(i));
			}
			return list;
			
		}catch (IOException e) {
	        e.printStackTrace();
	        
	        return null;
	    }		
	}

	public boolean loadData(Country currentCountry,
							String drugCodeIndex,							
							String drugNameIndex,
							String compoundNameIndex,
							String categoryIndex,
							String manufacturerIDIndex,
							String manufacturerNameIndex,
							String strengthIndex) throws Exception {
	    try {

	    	String[] line;
	    	Country country = currentCountry;
	    	
	    	
	        while ((line = this.reader.readNext()) != null) {
	        	      	
	        	Drug newDrug = new Drug();
	        	
	        	//Assigning the country to the newDrug
	        	newDrug.setCountry(country);
	            	       	
	        	if (drugCodeIndex != null && drugCodeIndex.length() > 0){
	        		String drugCode = line [Integer.parseInt(drugCodeIndex)];
	        		String brandName = drugNameIndex != null ? line[Integer.parseInt(drugNameIndex)] : null;
	        		newDrug.setDrugCode(drugCode);
	        		newDrug.setBrandName(brandName);
	        	}else {
	        		throw new Exception();
	        	}
	        		        	
	        	//Checking if there a value retrieved from the CSV for the property below
	        	if (compoundNameIndex != null && compoundNameIndex.length() > 0) {
	        		String compoundName = line[Integer.parseInt(compoundNameIndex)];
	              	
	        		//Checking if the compound of the drug of the current line from the CSV is already on the list
		        	//In case it's not, add the new compound on it
		        	CompoundController compoundController = new CompoundController();
		        	Compound compound = null;
		        	
		        	if (this.compoundList != null && this.compoundList.size() != 0) {
		        		compound = compoundController.findCompound(compoundName, this.compoundList);
		        	}        	
		        	
		        	if (compound == null) {
		        		
		        		compound = new Compound(this.compoundIDGenerator.newId(), compoundName, null);
		        		this.compoundList.add(compound);
		        	}
		        	
		        	newDrug.setCompound(compound);
	        	}
	        	
	        	//Checking if there a value retrieved from the CSV for the property below
	        	if (manufacturerNameIndex != null && manufacturerNameIndex.length() > 0) {	
	        		String manufacturerName = line[Integer.parseInt(manufacturerNameIndex)];
	        		
	        		String manufacturerId = (manufacturerIDIndex != null && manufacturerIDIndex.length() > 0) 
	        				? line[Integer.parseInt(manufacturerIDIndex)] 
	        						:  String.format("%s", this.manufacturerIDGenerator.newId());
		        	
		        	//Checking if the manufacturer of the drug of the current line from the CSV is already on the list
		        	//In case it's not, add the new manufacturer on it
		        	ManufacturerController manufacturerController = new ManufacturerController();
		        	Manufacturer manufacturer = null;
		        	
		        	if (this.manufacturerList != null && this.manufacturerList.size() != 0) {
		        		manufacturer = manufacturerController.findManufacturer(manufacturerName, this.manufacturerList);
		        	}
		        	
		        	if (manufacturer == null) {
		        		manufacturer = new Manufacturer(manufacturerId,manufacturerName);
		        		this.manufacturerList.add(manufacturer);
		        	}
		        	
		        	newDrug.setManufacturer(manufacturer);
	        	}
	        	
	        	//Checking if there a value retrieved from the CSV for the property below
	        	if (categoryIndex != null && categoryIndex.length() > 0) {	
	        		String categoryName = line[Integer.parseInt(categoryIndex)];        	
		        	
	        		//Checking if the category of the drug of the current line from the CSV is already on the list
		        	//In case it's not, add the new category on it
		        	CategoryController categoryController = new CategoryController();
		        	Category category = null; 
		        	
		        	if (this.categoryList != null && this.categoryList.size() != 0) {
		        		category = categoryController.findCategory(categoryName, this.categoryList);
		        	}        	
		        	
		        	if (category == null) {
		        		
		        		category = new Category(this.categoryIDGenerator.newId(), categoryName);
		        		this.categoryList.add(category);
		        	}
		        	
		        	newDrug.setCategory(category);
	        	}
	        	
	        	if (strengthIndex != null && strengthIndex.length() > 0) {
	        		String strength = line[Integer.parseInt(strengthIndex)];
	        		newDrug.setStrength(strength);
	        	}
	        	
	        	//Adding the new drug
	           	this.drugsList.add(newDrug);
       	
	        }
	        
	        this.reader.close();
	        return true;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}
}

