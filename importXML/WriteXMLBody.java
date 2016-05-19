//-------------------------------------------------------------------------------
// Name:        Write main body of XML document class
// Purpose:		writes the main part of an xml document except the values part and 
//				the closing tags. 
//
// Input:		id of the involved procedure
//				name of the involved procedure
//				begin and endTime of observations to be inserted
//				feature of interest of the procedure
// Output:		creates a string which contains the first 7 parts of the XML document
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
import java.sql.SQLException;
import java.util.List;

import importJDBC.SQLQueries;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import mainGUIPackage.TimeStamp;

public class WriteXMLBody {
	
	public static void xmlBody(String inputFileName, String outputFileName, String nameOfService, String nodeType, String procedure, String sensorId, int channelColumn, int totalProcedures) throws IOException, SQLException {
		//declare variables needed
		String eventTime;		//to hold the event Time entries 
		String dataChannel;		//to hold the sensor data
		String sourceFileName = inputFileName;
		String outputXML = outputFileName;
		String id = sensorId;
		String foi = nodeType;
		String beginTime = "";
		String endTime;
		String procedureName = procedure;
		
		//header lines of file to be read
		int headerLines = 11 + totalProcedures + 2;
		
		 try {
			beginTime = SQLQueries.select_procedure_etime(nameOfService, procedure);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    //Importing and reading measurement file as a .csv document and skipping headerLines	
	    CSVReader reader = new CSVReader(new FileReader(sourceFileName), ',' , '"' , headerLines);
	    
	    //read the whole document and pass it to a list of String arrays
	    List<String[]> allrows = reader.readAll();
	  
	    //read end time from the last line of the data file
	    int endLine = allrows.size();
	    String [] lastLine = allrows.get(endLine-1);
	    endTime = lastLine[0];
	    //convert endTime to iso8601 format
	    endTime = TimeStamp.iso8601(endTime);
	    
		//------------------------------------------------------------------------------------------
		//WRITE PART 1 OF THE DOCUMENT
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
	    String sensorIdTag = "<sos:AssignedSensorId>" + id + "</sos:AssignedSensorId>" + "\n";
	    String omTag = "<om:Observation>" + "\n";
	    
	    String part2 = sensorIdTag + omTag;
	    //----------------------------------------------------------------------------
	    //Write Part 3
	    String omProcedureTag = "<om:procedure xlink:href=\'urn:ogc:def:procedure:x-istsos:1.0:" + procedureName + "\'/>" + "\n";
	    String samplingTimeTag = "<om:samplingTime>" + "\n" + "<gml:TimePeriod>" + "\n";
	    String beginPositionTag = "<gml:beginPosition>" + beginTime + "</gml:beginPosition>" + "\n";	    
	    String endPositionTag = "<gml:endPosition>" + endTime + "</gml:endPosition>" + "\n";
	    String samplingTimeTagEnd = "</gml:TimePeriod>" + "\n" + "</om:samplingTime>" + "\n";
	    
	    String part3 = omProcedureTag + samplingTimeTag + beginPositionTag + endPositionTag + samplingTimeTagEnd;	    
	    //----------------------------------------------------------------------------
	    //Write Part 4
	    String observedPropertyTag = "<om:observedProperty xlink:href=\'urn:ogc:def:property:x-istsos:1.0:composit\'>" + "\n"
	    + "<swe:CompositPhenomenon dimension=\'2\'>" + "\n";
	    String urnTime = "<swe:component xlink:href=\'urn:ogc:def:parameter:x-istsos:1.0:time:iso8601\'/>" + "\n";
	    //change observed property urn according to procedure
	    String obsPropUrn = "";
	    if (procedureName.charAt(0)== 's')
	    		obsPropUrn  = "<swe:component xlink:href=\'urn:ogc:def:parameter:x-istsos:1.0:strain\'/>" + "\n";
	    else if (procedureName.charAt(0)== 't')
	    		obsPropUrn  = "<swe:component xlink:href=\'urn:ogc:def:parameter:x-istsos:1.0:air:temperature\'/>" + "\n";
	    else if (procedureName.charAt(0)== 'd')
	    		obsPropUrn  = "<swe:component xlink:href=\'urn:ogc:def:parameter:x-istsos:1.0:displacement\'/>" + "\n";
	    else 
	    	System.out.println("Was not able to correspond observed property urn to procedure");
	   
	    String observedPropertyTagEnd = "</swe:CompositPhenomenon"
	    		+ "33n>" + "\n" + "</om:observedProperty>" + "\n";
	    
	    String part4 = observedPropertyTag + urnTime + obsPropUrn + observedPropertyTagEnd;
	    //----------------------------------------------------------------------------
	    //Write Part 5 - FOI tag
	    //(FOI) feature of interest according to node type
	    //what does this number of features means?????
	    String noOfFeatures = "3";
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
	    
	    //As above add <swe:field name> according to procedure being processed
	    String obsPropDefTag = "";
	    if (procedureName.charAt(0)== 's')
	    	obsPropDefTag = "<swe:field name=\'strain\'>" + "\n";
	    else if (procedureName.charAt(0)== 't')
	    	obsPropDefTag = "<swe:field name=\'air-temeperature\'>" + "\n";
	    else if (procedureName.charAt(0)== 'd')
	    	obsPropDefTag = "<swe:field name=\'displacement\'>" + "\n";
	    else 
	    	System.out.println("Was not able to correspond observed property urn to procedure");
	    obsPropDefTag = obsPropDefTag
	    		+ "<swe:Quantity definition=\'urn:ogc:def:property:x-istsos:1.0:strain\'>" + "\n"
	    		+ "<swe:uom code=\'μstrain\'/>" + "\n"
	    		+ "</swe:Quantity>" + "\n"
	    		+ "</swe:field>" + "\n";
	    
	    String dataRecordTagEnd = "</swe:DataRecord>" + "\n" + "</swe:elementType>" + "\n";
	    String part6 = elementTypeTag + fieldTimeTag + timeDefTag + obsPropDefTag + dataRecordTagEnd;
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
		
	    CSVWriter writer = new CSVWriter(new FileWriter(outputXML), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
	    System.out.println("-----------------------------XML BODY---------------------------------------------------------------------------");
	    System.out.println(xml);
	    writer.writeNext(xml);
	    
	    //count number of lines executed
	    int lineNumber = 0;
	    String [] nextLine;
	    
	    //Importing and reading measurement file as a .csv document and skipping headerLines	
	    reader = new CSVReader(new FileReader(sourceFileName), ',' , '"' , headerLines);
	    
	    while ((nextLine = reader.readNext()) != null) {
	    	lineNumber ++;
	    	eventTime = nextLine[0];
	    	dataChannel = nextLine[channelColumn];
	    	
	    	eventTime = TimeStamp.iso8601(eventTime);
	    	String comma = ",";
	    	
	    	if (lineNumber == endLine) {
	    		//last data line
	    		String lastDataLine = eventTime + comma + dataChannel;
	    		writer.writeNext(lastDataLine);
	    	}
	    	else {
	    		String dataLine = eventTime + comma + dataChannel + "@";
	    		writer.writeNext(dataLine);
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
	    writer.close();
	    reader.close();	
	    
	    String strURL = "http://quader.igg.tu-berlin.de/istsos";
	    //Send the post request
	    try {
			PostHTTP.post(strURL, outputFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	}

}
