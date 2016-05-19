package importJDBC;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import mainGUIPackage.TimeStamp;
import au.com.bytecode.opencsv.CSVReader;
//***********************************************************************************************************
//
//
//
//
//
//***********************************************************************************************************
public class SelectNode2JDBC {
	public static void DBInsertion( String inputDataFile, String nameOfService) throws IOException {
		//point path to input file 
//		String inputDataFile = "F:/3rd Semester/GIS Project/Sensor_data/sensors/Node573.csv";
//        String nameOfService = "test";

		//create a CSVReader instance to a FileReader object, with a ',' as delimiter to differentiate elements and skipping no headers
		CSVReader reader = new CSVReader(new FileReader(inputDataFile), ',', '"');
		
		//use CSVReader method readAll to read the whole document and pass it to a list of String arrays
		List<String []> allRows = reader.readAll();
 	    int endLine = allRows.size();
 	    
 	    //--------------------------------
 	    // Read number of channels and how many lines are to be skipped from data file
 	    String[] line4_ini = allRows.get(4);
 	    String channels = Arrays.toString(line4_ini);
 	    channels = channels.substring(15,16);
 	    
 	    //---------------------------------
 	    int numOfProcedure = Integer.parseInt(channels);
 	    int noOfSkipLines = numOfProcedure+ 13;

 	    //----------------------------------
 	    // Find beginPosition from data file
 	    String[] lineStart = allRows.get(noOfSkipLines);
 	    String startTime = Arrays.toString(lineStart);
 	    startTime = startTime.substring(1,30);
 	    startTime=TimeStamp.iso8601(startTime);
 	    //System.out.println(start);
 	    
 	    //----------------------------------
 	    // Find endPosition from data file
 	    String[] lineEnd = allRows.get(endLine-1);
 	    String endTime = Arrays.toString(lineEnd);
 	    endTime = endTime.substring(1,30);
 	    endTime=TimeStamp.iso8601(endTime);
 	    //System.out.println(end);

 	    
		//read the node number from the second element of the second row of the input file
		String [] line2 = allRows.get(1);
		String nameOfProcedure = "";
		String name_opr = "";
		//WRITE ACCELERATION NODE FILE
		//if the nodeType is of 384, or 383 then call the AccelerationNode2CSV class to produce the required file
		if (line2[1].substring(0,3).equals("383")){
			//call acceleration function and complete transaction
	  	    nameOfProcedure = "'Node_383'";
	        DBAccelerationNode.Node38Series(inputDataFile, nameOfService, nameOfProcedure, numOfProcedure, noOfSkipLines, startTime, endTime);
		}
		else if (line2[1].substring(0,3).equals("384")){
			//call acceleration function and complete transaction
	  	    nameOfProcedure = "'Node384'";
	        DBAccelerationNode.Node38Series(inputDataFile, nameOfService, nameOfProcedure, numOfProcedure, noOfSkipLines, startTime, endTime);
		}
		//INSERT INTO DATABASE FOR STRAIN GAUGE NODE573
		else if (line2[1].substring(0,3).equals("573")) //if the nodeType is of 573
		{	
			//read number of channels, number of different procedures contained
			String [] line4 = allRows.get(4);
			int numOfProcedures = Integer.parseInt(line4[1]);
			
			System.out.print("got number of channels contained in Node 573 data file. ");
			System.out.println("Your node file contains "+numOfProcedures+ " different procedures");
			
			//Iterate as many times as the number of channels/procedures contained in the data file.
			for (int i=0; i<numOfProcedures; i++) {
				//in each iteration scan the type of procedure and produce the corresponding .csv file
				//start by reading from line 11 up to as many lines as the number of channels contained in the file
				String [] line11 = allRows.get(11+i);
				//and read the corresponding channel
				int channel = Integer.parseInt(line11[1]);
				//mark the column of the data file which holds the data we want to extract
				int column = i+2;
				//correspond channel number to the right procedure (4 cases in node 573, channels 1,2 and 3 are strain gauges and channel 8 temperature sensor)
				switch (channel){
					case 1 :	//channel 1 in node 573 corresponds to procedure strain_node_573
						nameOfProcedure = "'strain1_node_573'";
				        name_opr = "'strain'";
						//call appropriate method to insert data into the database forstrain1_node_573
						DBStrainGauges.strain2DB(column, inputDataFile, nameOfService, nameOfProcedure, numOfProcedures, noOfSkipLines, startTime, endTime, name_opr);					
						break;
					case 2 :	//channel 2 in node 573 corresponds to procedure strain2_node_573
				  	     nameOfProcedure = "'strain2_node_573'";
				  	     name_opr = "'strain'";
						//call appropriate method to insert data into the database forstrain1_node_573
						DBStrainGauges.strain2DB(column, inputDataFile, nameOfService, nameOfProcedure, numOfProcedures, noOfSkipLines, startTime, endTime, name_opr);					
						break;
					case 3 : //channel 5 in node 573 corresponds to procedure disp_node_573
						nameOfProcedure = "'strain3_node_573'";
						name_opr = "'strain'";
						//call appropriate method to insert data into the database forstrain1_node_573
						DBStrainGauges.strain2DB(column, inputDataFile, nameOfService, nameOfProcedure, numOfProcedures, noOfSkipLines, startTime, endTime, name_opr);					
						break;
					case 8 : //channel 8 in node 573 corresponds to procedure t_node_573
						nameOfProcedure = "'t_node_573'";
						name_opr = "'air:temperature'";
						//call appropriate method to insert data into the database forstrain1_node_573
						DBStrainGauges.strain2DB(column, inputDataFile, nameOfService, nameOfProcedure, numOfProcedures, noOfSkipLines, startTime, endTime, name_opr);					
						break;
				}	
			}	
		}
		//INSERT INTO DATABASE FOR STRAIN GAUGE NODE574
		else if (line2[1].substring(0,3).equals("574")) //if the nodeType is of 573
		{
			//read number of channels, number of different procedures contained
			String [] line4 = allRows.get(4);
			int numOfProcedures = Integer.parseInt(line4[1]);
			
			System.out.print("got number of channels contained in Node 574 data file. ");
			System.out.println("Your Node 574 contains "+numOfProcedures+ " different procedures");
			
			//Iterate as many times as the number of channels/procedures contained in the data file.
			for (int i=0; i<numOfProcedures; i++) {
				//in each iteration scan the type of procedure and produce the corresponding .csv file
				//start by reading from line 11 up to as many lines as the number of channels contained in the file
				String [] line11 = allRows.get(11+i);
				//and read the corresponding channel
				int channel = Integer.parseInt(line11[1]);
				//mark the column of the data file which holds the data we want to extract
				int column = i+2;
				
				//correspond channel number to the right procedure
				switch (channel){
					case 1 :	//channel 1 in node 574 corresponds to procedure strain_node_574
						nameOfProcedure = "'strain1_node_574'";
				        name_opr = "'strain'";
						//call appropriate method to insert data into the database forstrain1_node_574
						DBStrainGauges.strain2DB(column, inputDataFile, nameOfService, nameOfProcedure, numOfProcedures, noOfSkipLines, startTime, endTime, name_opr);					
						break;
					case 2 :	//channel 2 in node 574 corresponds to procedure strain2_node_574
						nameOfProcedure = "'strain2_node_574'";
				        name_opr = "'strain'";
						//call appropriate method to insert data into the database forstrain1_node_574
						DBStrainGauges.strain2DB(column, inputDataFile, nameOfService, nameOfProcedure, numOfProcedures, noOfSkipLines, startTime, endTime, name_opr);					
						break;
					case 5 : //channel 5 in node 574 corresponds to procedure disp_node_574			
						nameOfProcedure = "'displ_node_574'";
				        name_opr = "'displacement'";
						//call appropriate method to insert data into the database forstrain1_node_574
						DBStrainGauges.strain2DB(column, inputDataFile, nameOfService, nameOfProcedure, numOfProcedures, noOfSkipLines, startTime, endTime, name_opr);					
						break;
					case 8 : //channel 8 in node 574 corresponds to procedure t_node_574
						nameOfProcedure = "'t_node_574'";
				        name_opr = "'air:temperature'";
						//call appropriate method to insert data into the database forstrain1_node_574
						DBStrainGauges.strain2DB(column, inputDataFile, nameOfService, nameOfProcedure, numOfProcedures, noOfSkipLines, startTime, endTime, name_opr);					
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
