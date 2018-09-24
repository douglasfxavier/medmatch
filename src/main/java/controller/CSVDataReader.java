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
import model.ActiveIngredient;
import model.Country;
import model.Drug;
import model.Manufacturer;
import util.IDGenerator;

public class CSVDataReader {
	protected ArrayList<Drug> drugsList = new ArrayList<Drug>();
	protected ArrayList<ActiveIngredient> activeIngredientsList = new ArrayList<ActiveIngredient>();
	protected ArrayList<Manufacturer> manufacturerList = new ArrayList<Manufacturer>();
	protected ArrayList<Category> categoryList = new ArrayList<Category>();
	protected IDGenerator activeIngredientIDGenerator = new IDGenerator();
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
							String activeIngredientNameIndex,
							String categoryIndex,
							String manufacturerIDIndex,
							String manufacturerNameIndex,
							String strengthIndex) throws Exception {
	    try {

	    	String[] line;
	    	Country country = currentCountry;
	    	
	    	while ((line = this.reader.readNext()) != null) {
	        	      	        	
	        	Drug drugInstance = null;

	        	if (drugCodeIndex == null || drugCodeIndex.length() == 0)
	        		return false;
	        	
	        	//Checking if the drug of the current line from the CSV is already on the list
		        //In case it's not, create a new instance of drug with the data from the CSV line
	        	String drugCode = line [Integer.parseInt(drugCodeIndex)];
	        	
	        	if (this.drugsList != null && this.drugsList.size() != 0) {	        		
	        		DrugController drugController = new DrugController();
		        	drugInstance = drugController.findDrugByCode(drugCode, drugsList);
		        }        	
		        	
	        	//-- #FOR A NOT INSTATIATED DRUG# --

	        	//Create a new instance and set the values from the CSV line 
		        if (drugInstance == null) {
		        	drugInstance = new Drug();		      		
			       	drugInstance.setDrugCode(drugCode);
			       	//Assigning the country to the newDrug
			       	drugInstance.setCountry(country);
			        	
			        //Checking if there is a value retrieved from the CSV for the property below
			        if (drugNameIndex != null && drugNameIndex.length() > 0){
			        	String brandName = line[Integer.parseInt(drugNameIndex)];
			        	drugInstance.setBrandName(brandName);
			        }
			        	
			        //Checking if there is a value retrieved from the CSV for the property below
			        if (manufacturerNameIndex != null && manufacturerNameIndex.length() > 0) {	
			        	String manufacturerName = line[Integer.parseInt(manufacturerNameIndex)];
				      
				       	//Checking if the manufacturer of the drug of the current line from the CSV is already on the list
				       	//In case it's not, add the new manufacturer on it				        	
				       	Manufacturer manufacturer = null;
				        	
				       	if (this.manufacturerList != null && this.manufacturerList.size() != 0) {
				       		ManufacturerController manufacturerController = new ManufacturerController();
				       		manufacturer = manufacturerController.findManufacturer(manufacturerName, this.manufacturerList);
				       	}
				      			        	
				       	if (manufacturer == null) {
				       		int manufacturerId = this.manufacturerIDGenerator.newId();
					       	manufacturer = new Manufacturer(String.format("%s", manufacturerId),manufacturerName);
				       	}   	
				       	
				       	drugInstance.setManufacturer(manufacturer);
				     }
			        
		        	//Checking if there is a value retrieved from the CSV for the property below
		        	if (categoryIndex != null && categoryIndex.length() > 0) {	
		        		String categoryName = line[Integer.parseInt(categoryIndex)];        	
			        	
	        		//Checking if the category of the drug of the current line from the CSV is already on the list
		        	//In case it's not, add the new category on it
				        	
			        	Category category = null; 
			        	
			        	if (this.categoryList != null && this.categoryList.size() != 0) {
			        		CategoryController categoryController = new CategoryController();
			        		category = categoryController.findCategory(categoryName, this.categoryList);
			        	}        	
				        	
			        	if (category == null) {
			        		
			        		category = new Category(this.categoryIDGenerator.newId(), categoryName);
			        		this.categoryList.add(category);
			        	}
				        	
			        	drugInstance.setCategory(category);
		        	}
			        	
			        if (strengthIndex != null && strengthIndex.length() > 0) {
			        	String strength = line[Integer.parseInt(strengthIndex)];
			        	drugInstance.setStrength(strength);
			        }
			        	
			        	//Adding the new drug
			        	this.drugsList.add(drugInstance);
		        }
		        	
		        //-- #FOR AN ALREADY INSTATIATED DRUG# --
		        	
		        //Checking if there is a value retrieved from the CSV for the property below
		        if (activeIngredientNameIndex != null && activeIngredientNameIndex.length() > 0) {
		        	String activeIngredientName = line[Integer.parseInt(activeIngredientNameIndex)];
	              	
		        	//Checking if the active ingredient of the drug of the current line from the CSV is already on the list
			       	//In case it's not, add the new active ingredient on it
			        	
			       	ActiveIngredient activeIngredient = null;
			        	
			       	if (this.activeIngredientsList != null && this.activeIngredientsList.size() != 0) {
			       		ActiveIngredientController activeIngredientController = new ActiveIngredientController();
			       		activeIngredient = activeIngredientController.findCompound(activeIngredientName, this.activeIngredientsList);
			       	}        	
			        	
			       	if (activeIngredient == null) {				
			       		activeIngredient = new ActiveIngredient(this.activeIngredientIDGenerator.newId(), activeIngredientName);
			       		this.activeIngredientsList.add(activeIngredient);
			        }
			        	
			        drugInstance.getActiveIngredients().add(activeIngredient);
		        }
		        

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

