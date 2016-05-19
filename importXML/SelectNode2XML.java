//-------------------------------------------------------------------------------
// Name:        import postXML main class
// Purpose:		This class reads the input file provided by the user and creates XML
//				files ready for POST xml. Then insert into database by sending a 
//				POSTXML request. Porpuse of the program is to read automa-
//				tically the type of the "Node" and the type of sensors from the input 
//				and accordingly produce the files needed and to send the independent requests 
//
// Author:      Thomas
//
// Created:     4/04/2015
// Modified:	../../2015
// Copyright:   (c) GIS Project 2015
//-------------------------------------------------------------------------------

package importXML;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class SelectNode2XML {

	public static void importXML(String serviceName, String inputFile) throws IOException, SQLException{

//	public static void main(String[] args) throws IOException{
		//Declare global variables
		String nodeType; // to hold the type of node whose data file we are reading
		String outputDataFile; // to hold outputDataFile path and name
		String nameOfService = serviceName;
		
		//point path to input file 
		//String inputDataFile = "C:/Users/Thomas/Documents/GIS Master/Lectures_Ex/3rd Semester/GIS Project/datasets/session 26-03-2015/Node574.csv";
		String inputDataFile = inputFile;
		
		//create a CSVReader instance to a FileReader object, with a ',' as delimiter to differentiate elements and skipping no headers
		CSVReader reader = new CSVReader(new FileReader(inputDataFile), ',', '"');
		
		//use CSVReader method readAll to read the whole document and pass it to a list of String arrays
		List<String []> allrows = reader.readAll();
		
		//read the node number from the second element of the second row of the input file
		String [] line2 = allrows.get(1);
		nodeType = line2[0]+ "_" +line2[1];
		//Take the path of the input file to be able to create an output file later
		//String path = inputDataFile.substring(0, inputDataFile.length()-11);
		
		//WRITE ACCELERATION NODE FILE
		//if the nodeType is of 384, or 383 then call the AccelerationNodeXML class to produce the required file
		if (line2[1].substring(0,2).equals("38")){
			//create .xml file by calling the appropriate method
			AccelerationNodeXML.acceleration2XML(nameOfService, inputDataFile, nodeType);
		}
		
		//WRITE STRAIN GAUGE NODE CONNECTED FILES FOR STRAIN GAUGE NODE573
		else if (line2[1].substring(0,2).equals("57")) //if the nodeType is of 573 or 574
		{	
			//read number of channels, number of different procedures contained
			String [] line4 = allrows.get(4);
			int numOfProcedures = Integer.parseInt(line4[1]);
			
			System.out.println("Your node file contains "+numOfProcedures+ " different procedures");
			
			//get path of input file to be able create output files later
			String path = inputDataFile.substring(0, inputDataFile.length()-11);
			
			//Iterate as many times as the number of channels/procedures contained in the data file.
			for (int i=0; i<numOfProcedures; i++) {
				//in each iteration scan the type of procedure and produce the corresponding XML file
				//start by reading from line 11 up to as many lines as the number of channels contained in the file
				String [] line11 = allrows.get(11+i);
				//and read the corresponding channel
				int channel = Integer.parseInt(line11[1]);
				//mark the column of the data file which holds the data we want to extract
				int column = i+2;
				
				//correspond channel number to the right procedure (4 cases in node 573, channels 1,2 and 3 are strain gauges and 
				//channel 8 temperature sensor, one extra case for node 573 where channel 5 is displacement)
				String procedureName = null;
				String sensorId = null;
				switch (channel){
					case 1 :	//channel 1 in node 573 corresponds to procedure strain_node_573
						if (nodeType.equals("Node_573")) {
							procedureName = "strain_node_573";
							sensorId = "dfcacda2e4492c27a058e4959450a65a";
						}
						else if (nodeType.equals("Node_574")){
							procedureName = "strain_node_574";
							sensorId = "6b7a21f638b434fe6eee0688950f6d66";
						}
						//create output file name and path
						outputDataFile = path + procedureName + ".xml";
						//call appropriate POSTXML method for strain_node_573
						WriteXMLBody.xmlBody(inputDataFile, outputDataFile, nameOfService, nodeType, procedureName, sensorId, column, numOfProcedures);
						break;
					case 2 :	//channel 2 in node 573 corresponds to procedure strain2_node_573
						if (nodeType.equals("Node_573")) {
							procedureName = "strain2_node_573";
							sensorId = "a0b6107fe18e5b872b35dcfa7545bbfb";
						}
						else if (nodeType.equals("Node_574")){
							procedureName = "strain2_node_574";
							sensorId = "a3112ab22a546b237a6cfd71318a8024";
						}
						outputDataFile = path + procedureName + ".xml";
						//call appropriate POSTXML method for strain_node_573
						WriteXMLBody.xmlBody(inputDataFile, outputDataFile, nameOfService, nodeType, procedureName, sensorId, column, numOfProcedures);
						break;
					case 3 : //channel 3 in node 573 only, corresponds to procedure disp_node_573
						procedureName = "strain3_node_573";
						sensorId = "a0b6107fe18e5b872b35dcfa7545bbfb";
						outputDataFile = path + procedureName + ".xml";
						
						//call appropriate POSTXML method for strain_node_573
						WriteXMLBody.xmlBody(inputDataFile, outputDataFile, nameOfService, nodeType, procedureName, sensorId, column, numOfProcedures);
						break;
					case 5 : //channel 5 in node 574 only, corresponds to procedure disp_node_574
						procedureName = "displ_node_574";
						sensorId = "b08b039b98552d1f2e2c227fede46669";
						outputDataFile = path + procedureName + ".xml";
						
						//call appropriate POSTXML method for strain_node_573
						WriteXMLBody.xmlBody(inputDataFile, outputDataFile, nameOfService, nodeType, procedureName, sensorId, column, numOfProcedures);
						break;	
					case 8 : //channel 8 in node 573 corresponds to procedure t_node_573
						if (nodeType.equals("Node_573")) {
							procedureName = "t_node_573";
							sensorId = "440b165145e6b840012d38120b872ec8";
						}
						else if (nodeType.equals("Node_574")){
							procedureName = "t_node_574";
							sensorId = "17ad7dfbe587e8d6bc3c7888fa8408c3";
						}
						outputDataFile = path + procedureName + ".xml";
						//call appropriate POSTXML method for strain_node_573
						WriteXMLBody.xmlBody(inputDataFile, outputDataFile, nameOfService, nodeType, procedureName, sensorId, column, numOfProcedures);
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
