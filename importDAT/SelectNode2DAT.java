//-------------------------------------------------------------------------------
// Name:        import csv main class
// Purpose:		This class reads the input file provided by the user and provides
//				the appropriate number of .csv files used for data insertion through
//				the cmdimportcsv.py routine. Porpuse of the program is to read automa-
//				tically the type of the "Node" and the type of sensors from the input 
//				and accordingly produce the .csv files needed 
//
// Author:      Thomas
//
// Created:     26/03/2015
// Modified:	../../2015
// Copyright:   (c) GIS Project 2015
//-------------------------------------------------------------------------------

package importDAT;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;



public class SelectNode2DAT {

//	public static void main(String[] args) throws IOException {
	public static void csv2DAT(String inputFile) throws IOException {

		//Declare global variables
		String nodeType; // to hold the type of node whose data file we are reading
		String outputDataFile; // to hold outputDataFile path and name
		
		//point path to input file 
		//String inputDataFile = "C:/Users/Thomas/Documents/GIS Master/Lectures_Ex/3rd Semester/GIS Project/datasets/session 26-03-2015/Node574.csv";
		String inputDataFile = inputFile;
		
		//create a CSVReader instance to a FileReader object, with a ',' as delimiter to differentiate elements and skipping no headers
		CSVReader reader = new CSVReader(new FileReader(inputDataFile), ',', '"');
		
		//use CSVReader method readAll to read the whole document and pass it to a list of String arrays
		List<String []> allrows = reader.readAll();
		
		//read the node number from the second element of the second row of the input file
		String [] line2 = allrows.get(1);
		nodeType = line2[0]+line2[1];
		
		//Take the path of the input file to be able to create an output file later
		String path = inputDataFile.substring(0, inputDataFile.length()-11);
		
		//if the nodeType is of 384, or 383 then call the AccelerationNode2CSV class to produce the required file
		if (line2[1].substring(0,2).equals("38")){
			//create output file by adding the nodType and the fyle type '.DAT' to the file path
			 outputDataFile = path + nodeType + ".DAT";
			AccelerationNode2DAT.acceleration2DAT(inputDataFile, outputDataFile);
		}
		//else if the nodeType is of 573, then call the AccelerationNode2CSV class to produce the required file
		else if (line2[1].substring(0,3).equals("573")) //if the nodeType is of 573
		{	
			//read number of channels, number of different procedures contained
			String [] line4 = allrows.get(4);
			int numOfProcedures = Integer.parseInt(line4[1]);
			
			System.out.print("got number of channels contained in Nonde 573 data file. ");
			System.out.println("Your node file contains "+numOfProcedures+ " different procedures");
			
	 		//
			for (int i=0; i<numOfProcedures; i++) {
				//in each iteration scan the type of procedure and produce the corresponding .csv file
				//start by reading from line 11 up to as many lines as the number of channels contained in the file
				String [] line11 = allrows.get(11+i);
				//and read the corresponding channel
				int channel = Integer.parseInt(line11[1]);
				//mark the column of the data file which holds the data we want to extract
				int column = i+2;
				
				//correspond channel number to the right procedure (4 cases in node 573, channels 1,2 and 3 are strain gauges and channel 8 temperature sensor)
				
				//
				switch (channel){
					case 1 :	//channel 1 in node 574 corresponds to procedure strain_node_574
						String procedureName = "strain_node_573";
						outputDataFile = path + procedureName + ".DAT";

						//call appropriate method to produce a '.DAT' file for strain_node_574
						StrainNode2DAT.strain2DAT(inputDataFile, outputDataFile, column, numOfProcedures);
						break;
					case 2 :	//channel 2 in node 574 corresponds to procedure strain2_node_574
						procedureName = "strain2_node_573";
						outputDataFile = path + procedureName + ".DAT";

						//call appropriate method to produce a '.DAT' file for strain_node_574
						StrainNode2DAT.strain2DAT(inputDataFile, outputDataFile, column, numOfProcedures);
						break;
					case 3 : //channel 5 in node 574 corresponds to procedure disp_node_574
						procedureName = "strain3_node_573";
						outputDataFile = path + procedureName + ".DAT";
						
						//call appropriate method to produce a '.DAT' file for disp_node_574
						StrainNode2DAT.strain2DAT(inputDataFile, outputDataFile, column, numOfProcedures);
						
						break;
					case 8 : //channel 8 in node 574 corresponds to procedure t_node_574
						procedureName = "t_node_573";
						outputDataFile = path + procedureName + ".DAT";
						
						//call appropriate method to produce a '.DAT' file for disp_node_574
						StrainNode2DAT.temp2DAT(inputDataFile, outputDataFile, column, numOfProcedures);
						break;
				}	
			}	
		}
		
		//else if the nodeType is of 573, then call the AccelerationNode2CSV class to produce the required file
		else if (line2[1].substring(0,3).equals("574")) //if the nodeType is of 573
		{
			//read number of channels, number of different procedures contained
			String [] line4 = allrows.get(4);
			int numOfProcedures = Integer.parseInt(line4[1]);
			
			System.out.print("got number of channels contained in Nonde 574 data file. ");
			System.out.println("Your node file contains "+numOfProcedures+ " different procedures");
			
			for (int i=0; i<numOfProcedures; i++) {
				//in each iteration scan the type of procedure and produce the corresponding .csv file
				//start by reading from line 11 up to as many lines as the number of channels contained in the file
				String [] line11 = allrows.get(11+i);
				//and read the corresponding channel
				int channel = Integer.parseInt(line11[1]);
				//mark the column of the data file which holds the data we want to extract
				int column = i+2;
				
				//
				switch (channel){
					case 1 :	//channel 1 in node 574 corresponds to procedure strain_node_574
						String procedureName = "strain_node_574";
						outputDataFile = path + procedureName + ".DAT";

						//call appropriate method to produce a '.DAT' file for strain_node_574
						StrainNode2DAT.strain2DAT(inputDataFile, outputDataFile, column, numOfProcedures);
						break;
					case 2 :	//channel 2 in node 574 corresponds to procedure strain2_node_574
						procedureName = "strain2_node_574";
						outputDataFile = path + procedureName + ".DAT";

						//call appropriate method to produce a '.DAT' file for strain_node_574
						StrainNode2DAT.strain2DAT(inputDataFile, outputDataFile, column, numOfProcedures);
						break;
					case 5 : //channel 5 in node 574 corresponds to procedure disp_node_574
						procedureName = "displ_node_574";
						outputDataFile = path + procedureName + ".DAT";
						
						//call appropriate method to produce a '.DAT' file for disp_node_574
						StrainNode2DAT.disp2DAT(inputDataFile, outputDataFile, column, numOfProcedures);
						
						break;
					case 8 : //channel 8 in node 574 corresponds to procedure t_node_574
						procedureName = "t_node_574";
						outputDataFile = path + procedureName + ".DAT";
						
						//call appropriate method to produce a '.DAT' file for disp_node_574
						StrainNode2DAT.temp2DAT(inputDataFile, outputDataFile, column, numOfProcedures);
						break;
				}
				
			}	
		}
		else 
			System.out.println("i can not recognize the node file you are trying to convert /n possibly node hasn't be declared in database");
		
		//close the CSVReader
		reader.close();
	}

}
