package org.workflow.xacml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.io.File;



public class Attribute {
	String entity;
	String attribute;
	String value;	
	String line = null;
	//String executed_user;
	//HashMap<String,String> history = new HashMap<String, String>();
	
	
	public void writeToAttribute(String entity, String attribute, String value){
		this.entity = entity;
		this.attribute = attribute;
		this.value = value;
		String fileName = "C:/Users/Redouan/workspace/Entities/";
		File file = null;	
		
		try{
    		
		    fileName = fileName+entity+"/"+attribute+".txt";
			//System.out.println(fileName);
    		file = new File(fileName);
    		
    		//if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
		}catch(IOException e){
    		e.printStackTrace();
    	}
		
		try {
            // Assume default encoding.
            FileWriter fileWriter =
                new FileWriter(file.getAbsoluteFile(),true);
            
            

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);

                              
            bufferedWriter.write(value);
            bufferedWriter.newLine();

            // Always close files.
            
            bufferedWriter.close();
        }
		catch(IOException ex) {
            System.out.println(
                "Error writing to file '"
                + fileName + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
		
	}
	
	
	
	public boolean checkValueAttribute(String entity, String attribute, String value){
		boolean found = false;
		this.entity = entity;
		this.attribute = attribute;
		this.value = value;
		String fileName = "C:/Users/Redouan/workspace/Entities/";
		//String search = u;
		
		fileName = fileName+entity+"/"+attribute+".txt";
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);    

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                String b = line;
                if(b.equals(value)){                	
                	found = true;
                }
            }    

            // Always close files.
            bufferedReader.close();            
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                   
            // Or we could just do this: 
            // ex.printStackTrace();
        }
		return found;
	}
	
	public String readFromAttribute(String entity, String attribute){
		this.entity = entity;
		this.attribute = attribute;
		String value = null;
		String fileName = "C:/Users/Redouan/workspace/Entities/";
		
		fileName = fileName+entity+"/"+attribute+".txt";
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);
            

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                value = line;
                
            }    
            // Always close files.
            bufferedReader.close();            
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                   
            // Or we could just do this: 
            // ex.printStackTrace();
        }		
		return value;	
	}
	
	public String readConstraints(String fileConstraints){
		//this.entity = entity;
		//this.attribute = attribute;
		String value ="";
		//String fileName = "C:/Users/Redouan/workspace/Constraints/constraints.txt";
		String fileName = fileConstraints;
		//System.out.println("read the constraints");
		//fileName = fileName+entity+"/"+attribute+".txt";
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);     

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                //System.out.println(line);
                value += line;                
            }    
            // Always close files.
            bufferedReader.close();            
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                   
            // Or we could just do this: 
            // ex.printStackTrace();
        }		
		return value;	
	}
}
