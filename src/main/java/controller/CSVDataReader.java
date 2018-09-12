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
	protected IDGenerator idCompoundGenerator = new IDGenerator();
	protected IDGenerator idCategoryGenerator = new IDGenerator();
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
							int drugCodeIndex,							
							int drugNameIndex,
							int compoundNameIndex,
							int categoryIndex,
							int manufacturerIDIndex,
							int manufacturerNameIndex,
							int strengthIndex) throws FileNotFoundException {
	    try {

	    	String[] line;
	        
	        while ((line = this.reader.readNext()) != null) {
	        	Country country = currentCountry; 
	        	String categoryName = line[categoryIndex];
	        	String compoundName = line[compoundNameIndex];
	        	String drugCode = line [drugCodeIndex];
	        	String drugName = line[drugNameIndex];
	        	String manufacturerId = line[manufacturerIDIndex];
	        	String manufacturerName = line[manufacturerNameIndex];
	        	String strength = line[strengthIndex];
	        	Compound compound = null;
	        	Manufacturer manufacturer = null;
	        	Category category = null;
	        	
	        	//Checking if the compound of the drug of the current line from the CSV is already on the list
	        	//In case it's not, add the new compound on it
	        	CompoundController compoundController = new CompoundController();
	        	
	        	if (this.compoundList != null && this.compoundList.size() != 0) {
	        		compound = compoundController.findCompound(compoundName, this.compoundList);
	        	}        	
	        	
	        	if (compound == null) {
	        		
	        		compound = new Compound(this.idCompoundGenerator.newId(), compoundName, null);
	        		this.compoundList.add(compound);
	        	}       	
	        	
	        	//Checking if the manufacturer of the drug of the current line from the CSV is already on the list
	        	//In case it's not, add the new manufacturer on it
	        	ManufacturerController manufacturerController = new ManufacturerController();
	        	
	        	if (this.manufacturerList != null && this.manufacturerList.size() != 0) {
	        		manufacturer = manufacturerController.findManufacturer(manufacturerName, this.manufacturerList);
	        	}
	        	
	        	if (manufacturer == null) {
	        		manufacturer = new Manufacturer(manufacturerId,manufacturerName);
	        		this.manufacturerList.add(manufacturer);
	        	}
	        	
	        	//Checking if the category of the drug of the current line from the CSV is already on the list
	        	//In case it's not, add the new category on it
	        	CategoryController categoryController = new CategoryController();
	        	
	        	if (this.categoryList != null && this.categoryList.size() != 0) {
	        		category = categoryController.findCategory(categoryName, this.categoryList);
	        	}        	
	        	
	        	if (category == null) {
	        		
	        		category = new Category(this.idCategoryGenerator.newId(), categoryName);
	        		this.categoryList.add(category);
	        	}      
	        	
	        	
	        	//Creating instance of drug	        	
	        	Drug drug = new Drug(drugCode,drugName,null,strength,manufacturer,country,compound,category);
	           	this.drugsList.add(drug);
       	
	        }
	        
	        this.reader.close();
	        return true;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}

