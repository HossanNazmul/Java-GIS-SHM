//-------------------------------------------------------------------------------
// Name:        Acceleration Node to POST XML request file class
// Purpose:		Transforms the NodeMaster '.csv' output files of an acceleration 
//				Node to an XML file POST request ready and inserts it to database 
//				through a post request routine.
//
// Input:		Takes filename of input file and node type of input file
// Output:		Creates an XML file of the procedures attached to the node fed to the program
//				and send an Post XML request
//
// Author:      Thomas
//
// Created:     04/04/2015
// Modified:	../../2015
// Copyright:   (c) GIS Project 2015
//-------------------------------------------------------------------------------
package importXML;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import importJDBC.SQLQueries;

import java.sql.SQLException;
import java.util.List;

import mainGUIPackage.TimeStamp;
//importing OPENCSV library files
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class AccelerationNodeXML {

	public static void acceleration2XML(String nameOfService, String inputFile, String nodeType) throws IOException, SQLException{
		//Declaration of variables
		String eventTime; //variable to hold the event time of the input file
	    String channel_x; //channel of acceleration-X
	    String channel_y; //channel of acceleration-Y
	    String channel_z; //channel of acceleration-Z
	    String channel_temp; //channel for temperature 
	    String sensorId = "";	//to hold the sensorId for the XML file
	    String foi; 		//variable to hold feature of interest of procedure
	    String beginTime = "";
	
	    //input file
	    String sourceFileName = inputFile;
	    //output file
	    String outputFileName= sourceFileName.replace(".csv", ".xml");
	    
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
		
		//read the whole document and pass it to a list of String arrays
		List<String[]> allrows = reader.readAll();
		//get the procedure type
		String procedure = nodeType;
		System.out.println(procedure);
		//At this point we have to query the end position or end time from the database
	    try {
	    	beginTime = SQLQueries.select_procedure_etime(nameOfService, procedure);
	    } catch(Throwable se){
	        //Handle errors for JDBC
	        se.printStackTrace();
	     }
	    //read end time from the last line of data file
	    int endLine = allrows.size();
	    String [] lastLine = allrows.get(endLine-1);
	    String endTime = lastLine[0];
	    endTime = TimeStamp.iso8601(endTime);
		
	    //Write part 1 of the XML Document
	    //Part 1. Creating XML file header
	    String part1 = "<?xml version= \'1.0\' encoding=\'UTF-8\'?>" + "\n"
				+ "<sos:InsertObservation"+"\n"
				+ "xmlns:xsi=\'http://www.w3.org/2001/XMLSchema-instance\'"+ "\n"
				+ "xsi:schemaLocation=\'http://schemas.opengis.net/sos/1.0.0/sosAll.xsd\'"+ "\n"
				+ "xmlns:sos=\'http://www.opengis.net/sos/1.0\'"+ "\n"
				+ "xmlns:xlink=\'http://www.w3.org/1999/xlink\'"+ "\n"
				+ "xmlns:sa=\'http://www.opengis.net/sampling/1.0\'"+ "\n"
				+ "xmlns:swe=\'http://www.opengis.net/swe/1.0.1\'"+ "\n"
				+ "xmlns:gml=\'http://www.opengis.net/gml/3.2\'"+ "\n"
				+ "xmlns:ogc=\'http://www.opengis.net/ogc\'"+ "\n"
				+ "xmlns:om=\'http://www.opengis.net/om/1.0\' service=\'SOS\' version=\'1.0.0\' >" +"\n";
	    //----------------------------------------------------------------------------
	    //Write Part 2
	    //assign sensor ID according to node 383 or 384
	    if (procedure.equals("Node_383")){
	    	sensorId = "0e582006c220db85e2730011a191d795";
	    }
	    else if (procedure.equals("Node_384")){
	    	sensorId = "dcafdd3a7dd99046d408392ae0240152";
	    }
	    else 
	    	System.out.println("was not able to read the procedure name right. SensorId was unassigned!");
	    String sensorIdTag = "<sos:AssignedSensorId>" + sensorId + "</sos:AssignedSensorId>" + "\n";
	    String omTag = "<om:Observation>" + "\n";
	    
	    String part2 = sensorIdTag + omTag;
	    //----------------------------------------------------------------------------
	    //Write Part 3
	    String omProcedureTag = "<om:procedure xlink:href=\'urn:ogc:def:procedure:x-istsos:1.0:" + procedure + "\'/>" + "\n";
	    String samplingTimeTag = "<om:samplingTime>" + "\n" + "<gml:TimePeriod>" + "\n";
	    String beginPositionTag = "<gml:beginPosition>" + beginTime + "</gml:beginPosition>" + "\n";	    
	    String endPositionTag = "<gml:endPosition>" + endTime + "</gml:endPosition>" + "\n";
	    String samplingTimeTagEnd = "</gml:TimePeriod>" + "\n" + "</om:samplingTime>" + "\n";
	    
	    String part3 = omProcedureTag + samplingTimeTag + beginPositionTag + endPositionTag + samplingTimeTagEnd;	    
	    //----------------------------------------------------------------------------
	    //Write Part 4
	    String observedPropertyTag = "<om:observedProperty xlink:href=\'urn:ogc:def:property:x-istsos:1.0:composit\'>" + "\n"
	    + "<swe:CompositPhenomenon dimension=\'5\'>" + "\n";
	    String urnTime = "<swe:component xlink:href=\'urn:ogc:def:parameter:x-istsos:1.0:time:iso8601\'/>" + "\n";
	    String urnAccX = "<swe:component xlink:href=\'urn:ogc:def:property:x-istsos:1.0:acceleration-x\'/>" + "\n";
	    String urnAccY = "<swe:component xlink:href=\'urn:ogc:def:property:x-istsos:1.0:acceleration-y\'/>" + "\n";
	    String urnAccZ = "<swe:component xlink:href=\'urn:ogc:def:property:x-istsos:1.0:acceleration-z\'/>" + "\n";
	    String urnTemp = "<swe:component xlink:href=\'urn:ogc:def:property:x-istsos:1.0:air:temperature\'/>" + "\n";
	    String observedPropertyTagEnd = "</swe:CompositPhenomenon>" + "\n" + "</om:observedProperty>" + "\n";
	    
	    String part4 = observedPropertyTag + urnTime + urnAccX + urnAccY + urnAccZ + urnTemp + observedPropertyTagEnd;
	    //----------------------------------------------------------------------------
	    //Write Part 5 - FOI tag
	    //(FOI) feature of interest according to procedure
	    foi = procedure;
	    //what does this number of features means?????
	    String noOfFeatures = "6";
	    String part5 = "<om:featureOfInterest xlink:href=\'urn:ogc:object:feature:x-istsos:1.0:station:" + foi + "\'/>" + "\n"
	    		+ "<om:result>" + "\n" + "<swe:DataArray>" + "\n" + "<swe:elementCount>" + "\n"
	    		+ "<swe:Count>" + "\n" + "<swe:value>" +noOfFeatures + "</swe:value>" + "\n" + "</swe:Count>" 
	    		+ "\n" + "</swe:elementCount>" + "\n";
	    //----------------------------------------------------------------------------
	    //Write Part 6
	    String elementTypeTag = "<swe:elementType name=\'SimpleDataArray\'>" + "\n";
	    String fieldTimeTag = "<swe:DataRecord definition=\'urn:ogc:def:dataType:x-istsos:1.0:timeSeries\'>"+ "\n";
	    String timeDefTag = "<swe:field name=\'Time\'>"+ "\n" +
	    					"<swe:Time definition=\'urn:ogc:def:parameter:x-istsos:1.0:time:iso8601\'/>" + "\n" + "</swe:field>" + "\n";
	    String accXDefTag = "<swe:field name=\'acceleration-X\'>" + "\n" 
	    		+ "<swe:Quantity definition=\'urn:ogc:def:property:x-istsos:1.0:acceleration-x\'>" + "\n"
	    		+ "<swe:uom code=\'G\'/>" + "\n"
	    		+ "</swe:Quantity>" + "\n"
	    		+ "</swe:field>" + "\n";
	    String accYDefTag = "<swe:field name=\'acceleration-Y\'>" + "\n" 
				+ "<swe:Quantity definition=\'urn:ogc:def:property:x-istsos:1.0:acceleration-y\'>" + "\n"
				+ "<swe:uom code=\'G\'/>" + "\n"
				+ "</swe:Quantity>" + "\n"
				+ "</swe:field>" + "\n";	    				
	    
	    String accZDefTag = "<swe:field name=\'acceleration-Z\'>" + "\n" 
				+ "<swe:Quantity definition=\'urn:ogc:def:property:x-istsos:1.0:acceleration-z\'>" + "\n"
				+ "<swe:uom code=\'G\'/>" + "\n"
				+ "</swe:Quantity>" + "\n"
				+ "</swe:field>" + "\n";
	    
	    String TempDefTag = "<swe:field name=\'air:temperature\'>" + "\n" 
				+ "<swe:Quantity definition=\'urn:ogc:def:property:x-istsos:1.0:air:temperature\'>" + "\n"
				+ "<swe:uom code=\'Degree\'/>" + "\n"
				+ "</swe:Quantity>" + "\n"
				+ "</swe:field>" + "\n";
	    String dataRecordTagEnd = "</swe:DataRecord>" + "\n" + "</swe:elementType>" + "\n";
	    String part6 = elementTypeTag + fieldTimeTag + timeDefTag + accXDefTag + accYDefTag + accZDefTag + TempDefTag + dataRecordTagEnd;
	    //----------------------------------------------------------------------------
	    //Part 7
	    String encodingTag = "<swe:encoding>" + "\n"
	    		+ "<swe:TextBlock tokenSeparator=\',\' blockSeparator=\'@\' decimalSeparator=\'.\'/>"+ "\n"
	    		+ "</swe:encoding>" +"\n";
	    String valueTag = "<swe:values>";
	    String part7 = encodingTag + valueTag;
	    //----------------------------------------------------------------------------
	    //Add all parts together and start writing
	    String xml = part1 + part2 + part3 + part4 + part5 + part6 + part7 ;

	    System.out.println("-----------------------------XML BODY---------------------------------------------------------------------------");

	    System.out.println(xml);
	    writer.writeNext(xml);
	    
	    //count number of lines executed
	    int lineNumber = 0;
	    String [] nextLine;
	    
	    reader = new CSVReader(new FileReader(sourceFileName), ',' , '"' , 17);
	    
	    while ((nextLine = reader.readNext()) != null) {
	    	lineNumber ++;
	    	eventTime = nextLine[0];
	    	channel_x = nextLine[2];
	    	channel_y = nextLine[3];
	    	channel_z = nextLine[4];
	    	channel_temp = nextLine[5];
	    	
	    	eventTime = TimeStamp.iso8601(eventTime);
	    	String comma = ",";
	    	
	    	if (lineNumber == endLine) {
	    		//build a data line
	    		String xmlLastLine = eventTime + comma + channel_x + comma + channel_y + comma + channel_z + comma + channel_temp;
	    		writer.writeNext(xmlLastLine);
	    		System.out.println(xmlLastLine);
	    	}
	    	else {
	    		String xmlDataLine = eventTime + comma + channel_x + comma + channel_y + comma + channel_z + comma + channel_temp + "@";
	    		writer.writeNext(xmlDataLine);
	    		System.out.println(xmlDataLine);
	    	}
	    }
	    //Write Part 8
	    String valuesTagEnd = "</swe:values>" + "\n"
    			+ "</swe:DataArray>"+ "\n"
    			+ "</om:result>"+ "\n"
    			+ "</om:Observation>"+ "\n"
    			+ "</sos:InsertObservation>";
	    writer.writeNext(valuesTagEnd);
	    System.out.println(valuesTagEnd);
	    System.out.println("---------------------------------------------------------------------------------------------------------------");

	    writer.close();
	    reader.close();
	    
	    String strURL = "http://quader.igg.tu-berlin.de/istsos/bridgedemoservice";
	    //Send the post request
	    try {
			PostHTTP.post(strURL, outputFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    System.out.println(outputFileName);
	}

}
