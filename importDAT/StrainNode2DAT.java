//-------------------------------------------------------------------------------
// Name:        Strain Gauge Node to .DAT file class
// Purpose:		Transforms the NodeMaster '.csv' output files of an strain gauge 
//				Node to all separate '.DAT' files needed, according to the different 
//				procedures contained in the input file. The produced '.DAT' files are
//				ready to be imported in istSOS database through the 'cmdimportcsv' routine.
//
// Input:		Takes filename of input file and output file
// Output:		Creates the .DAT files of the procedures attached to the node fed to the program
//
// Author:      Thomas
//
// Created:     27/03/2015
// Modified:	27/03/2015
// Copyright:   (c) GIS Project 2015
//-------------------------------------------------------------------------------

package importDAT;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import mainGUIPackage.TimeStamp;
//importing OPENCSV library files
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class StrainNode2DAT {
	
	public static void strain2DAT (String inputFile, String outputFile, int channelColumn, int totalProcedures) throws IOException {
		//variables
		String timeStamp;
		String gaugeChannel;
		String sourceFileName = inputFile;
		String outputFileName = outputFile;
		
		//urn of observed property strain gauge and time
		String urnGauge = "urn:ogc:def:parameter:x-istsos:1.0:strain";
		String urnTime = "urn:ogc:def:parameter:x-istsos:1.0:time:iso8601,";
		
		//Creating an instance of CSVReader class. CAUTION with the number of header lines to skip
		int headerLines = 11 + totalProcedures + 2;
		CSVReader reader = new CSVReader(new FileReader(sourceFileName), ',' , '"' , headerLines);
		
		//Creating an instance of CSVWriter
	    CSVWriter writer = new CSVWriter(new FileWriter(outputFileName), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
	    
	    //Creating a string array in order to read line by line
	    String[] nextLine;
	    
	    //header of the output file containing both urns
	    String urn = urnTime + urnGauge;
	    
	    //Writing the header into the destination file
	    writer.writeNext(urn);
	    
	    //Counter to hold line number
	    int lineNumber = 0;
	    
	    //read each line of the input file and single out columns needed to get copied
	    while ((nextLine = reader.readNext()) != null) {
	        lineNumber++;				//update the current line number
	        
	        timeStamp = nextLine[0]; 	//timestamp first column of each line of input file
	        gaugeChannel = nextLine[channelColumn]; 	//get the right column, which holds the strain channel data
	      	
	        //convert event time to ISO format
	        String newtime = TimeStamp.iso8601(timeStamp);
	        String comma = ",";
	        //Build a new string - representing each line, with all the required columns, in the output file
	        String newLinedata = newtime + comma + gaugeChannel;
	        
	        //Write data to the destination file
	        writer.writeNext(newLinedata);
	        // Show output how the data looks like
	        //System.out.println(newLinedata);
	        	        
	      }
	       //Show how many lines are executed
	       System.out.println("Total number of lines imported and exported: " + lineNumber);

	      //Close the reader and writer
	      writer.close();
	      reader.close();
	}
	
	public static void disp2DAT (String inputFile, String outputFile, int channelColumn, int totalProcedures) throws IOException {
		//variables
		String timeStamp;
		String dispChannel;
		String sourceFileName = inputFile;
		String outputFileName = outputFile;
		
		//urn of observed property strain gauge and time
		String urnDisp = "urn:ogc:def:parameter:x-istsos:1.0:displacement";
		String urnTime = "urn:ogc:def:parameter:x-istsos:1.0:time:iso8601,";
		
		//Creating an instance of CSVReader class. CAUTION with the number of header lines to skip
		int headerLines = 11 + totalProcedures + 2;
		CSVReader reader = new CSVReader(new FileReader(sourceFileName), ',' , '"' , headerLines);
		
		//Creating an instance of CSVWriter
	    CSVWriter writer = new CSVWriter(new FileWriter(outputFileName), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
	    
	    //Creating a string array in order to read line by line
	    String[] nextLine;
	    
	    //header of the output file containing both urns
	    String urn = urnTime + urnDisp;
	    
	    //Writing the header into the destination file
	    writer.writeNext(urn);
	    
	    //Counter to hold line number
	    int lineNumber = 0;
	    
	    //read each line of the input file and single out columns needed to get copied
	    while ((nextLine = reader.readNext()) != null) {
	        lineNumber++;				//update the current line number
	        
	        timeStamp = nextLine[0]; 	//time stamp first column of each line of input file
	        dispChannel = nextLine[channelColumn]; 	//get the right column, which holds the strain channel data
	      	
	        //convert event time to ISO format
	        String newtime = TimeStamp.iso8601(timeStamp);
	        String comma = ",";
	        //Build a new string - representing each line, with all the required columns, in the output file
	        String newLinedata = newtime + comma + dispChannel;
	        
	        //Write data to the destination file
	        writer.writeNext(newLinedata);
	        // Show output how the data looks like
	        //System.out.println(newLinedata);
	        	        
	      }
	       //Show how many lines are executed
	       System.out.println("Total number of lines imported and exported: " + lineNumber);

	      //Close the reader and writer
	      writer.close();
	      reader.close();
	}
	
	public static void temp2DAT (String inputFile, String outputFile, int channelColumn, int totalProcedures) throws IOException {
		//variables
		String timeStamp;
		String tempChannel;
		String sourceFileName = inputFile;
		String outputFileName = outputFile;
		
		//urn of observed property strain gauge and time
		String urnTemp = "urn:ogc:def:parameter:x-istsos:1.0:air:temperature";
		String urnTime = "urn:ogc:def:parameter:x-istsos:1.0:time:iso8601,";
		
		//Creating an instance of CSVReader class. CAUTION with the number of header lines to skip
		int headerLines = 11 + totalProcedures + 2;
		CSVReader reader = new CSVReader(new FileReader(sourceFileName), ',' , '"' , headerLines);
		
		//Creating an instance of CSVWriter
	    CSVWriter writer = new CSVWriter(new FileWriter(outputFileName), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
	    
	    //Creating a string array in order to read line by line
	    String[] nextLine;
	    
	    //header of the output file containing both urns
	    String urn = urnTime + urnTemp;
	    
	    //Writing the header into the destination file
	    writer.writeNext(urn);
	    
	    //Counter to hold line number
	    int lineNumber = 0;
	    
	    //read each line of the input file and single out columns needed to get copied
	    while ((nextLine = reader.readNext()) != null) {
	        lineNumber++;				//update the current line number
	        
	        timeStamp = nextLine[0]; 	//time stamp first column of each line of input file
	        tempChannel = nextLine[channelColumn]; 	//get the right column, which holds the strain channel data
	      	
	        //convert event time to ISO format
	        String newtime = TimeStamp.iso8601(timeStamp);
	        String comma = ",";
	        //Build a new string - representing each line, with all the required columns, in the output file
	        String newLinedata = newtime + comma + tempChannel;
	        
	        //Write data to the destination file
	        writer.writeNext(newLinedata);
	        // Show output how the data looks like
	        //System.out.println(newLinedata);
	        	        
	      }
	       //Show how many lines are executed
	       System.out.println("Total number of lines imported and exported: " + lineNumber);

	      //Close the reader and writer
	      writer.close();
	      reader.close();
	}

}
