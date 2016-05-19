//-------------------------------------------------------------------------------
// Name:        Acceleration Node to .DAT file class
// Purpose:		Transforms the NodeMaster '.csv' output files of an acceleration 
//				Node to a '.DAT' file ready to be imported in istSOS database 
//				through the 'cmdimportcsv' routine.
//
// Input:		Takes filename of input file and output file
// Output:		Creates the .DAT files of the procedures attached to the node fed to the program
//
// Author:      Nazmul
//
// Created:     14/11/2014
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

public class AccelerationNode2DAT {
	
	public static void acceleration2DAT (String inputFile, String outputFile) throws IOException {
		//Declaration of variables
		String timeStamp; //variable to hold the event time of the input file
	    String channel_x; //channel of acceleration-X
	    String channel_y; //channel of acceleration-Y
	    String channel_z; //channel of acceleration-Z
	    String channel_temp; //channel for temperature 
	
	    //input file
	    String sourceFileName = inputFile;
	    //output file
	    String outputFileName= outputFile;
	    //Creating an instance of CSVReader class
	    /*
	     * @param FileReader(sourceFileName)
	     *            the reader to an underlying CSV source.
	     * @param ','
	     *            the delimiter to use for separating entries
	     * @param '"'
	     *            the character to use for quoted elements
	     * @param 17
	     *            the line number to skip for start reading 
	     */
	    //Importing and reading measurement file as a .csv document and skipping first 17 lines	
	    CSVReader reader = new CSVReader(new FileReader(sourceFileName), ',' , '"' , 17);
	    
	    //Creating an instance of CSVWriter
	    /*
	     * Constructs CSVWriter using a comma for the separator.
	     *
	     * @param FileWriter(myFileName)
	     *            the writer to an underlying CSV source.
	     */
	    CSVWriter writer = new CSVWriter(new FileWriter(outputFileName), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);    
	    
	    //Creating a string array in order to read line by line
	    String[] nextLine;
	    
	    //Creating observed properties URNs as entered in istSOS database
	    String urnTime = "urn:ogc:def:parameter:x-istsos:1.0:time:iso8601,";
	    String urnAcc_x = "urn:ogc:def:parameter:x-istsos:1.0:acceleration-x,";
	    String urnAcc_y = "urn:ogc:def:parameter:x-istsos:1.0:acceleration-y,";
	    String urnAcc_z = "urn:ogc:def:parameter:x-istsos:1.0:acceleration-z,";
	    String urnTemp = "urn:ogc:def:parameter:x-istsos:1.0:air:temperature";
	    
	    //Concatenating all URN together to make the Header of the new output file
	    String urn = urnTime + urnAcc_x + urnAcc_y + urnAcc_z + urnTemp;  
	       
	    //Writing the header into the destination file
	    //System.out.println(urn);
	    writer.writeNext(urn);
	    
	    //Counter to hold line number
	    int lineNumber = 0;
	    
	    //read each line of the input file and single out columns needed to get copied
	    //rearrange columns and write them to the output file
	    while ((nextLine = reader.readNext()) != null) {
	        lineNumber++;				//update the current line number
	        
	        timeStamp = nextLine[0]; 	//timestamp first column of each line of input file
	        channel_x = nextLine[2]; 	//accel X third column
	        channel_y = nextLine[3]; 	//accel Y fourht column
	        channel_z = nextLine[4]; 	//accel Z fifth column
	        channel_temp = nextLine[5];	//temperature sixth column
	      	
	        //convert event time to ISO format
	        String newtime = TimeStamp.iso8601(timeStamp);
	        String comma = ",";
	        //Build a new string - representing each line, with all the required columns, in the output file
	        String newLinedata = newtime + comma + channel_x + comma + channel_y+ comma + channel_z + comma + channel_temp;
	        
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