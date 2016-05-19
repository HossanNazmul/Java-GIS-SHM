package importJDBC;

import java.io.FileReader;
import java.io.IOException;

import mainGUIPackage.TimeStamp;
import au.com.bytecode.opencsv.CSVReader;
//***********************************************************************************************************
//
//
//
//
//
//***********************************************************************************************************
public class DBStrainGauges {
	public static void strain2DB (int channelColumn, String fileName, String nameOfService, String nameOfProcedure, int numOfProcedure, int noOfSkipLines, String startTime, String endTime, String name_opr) throws IOException {
//		public static void Node38Series (String fileName, String nameOfService, String nameOfProcedure){
		try {
		       
			String timeStamp;
			String channel;
			//Creating a string array in order to read line by line
		    String[] nextLine;
	 	    //inputs to be feed into the program
//	        String name_opr = "'strain'";

	         int id_qi_fk = 100;

	         //observed property id according to observed property name.primary key of table observed_properties
	          int id_prc_fk = SQLQueries.procedure_id_prc_fk(nameOfService, nameOfProcedure);
//	          System.out.println( "id procedure = " + id_prc_fk);
	         
	        int id_opr_fk= SQLQueries.observed_properties_id_opr_fk_strain(nameOfService, name_opr, numOfProcedure);
//	         System.out.println( "id urn = " + id_opr_fk);

//		    String etime_prc = SQLQueries.select_procedure_etime(nameOfService, nameOfProcedure);
	        int count = 0;
	        CSVReader reader = new CSVReader(new FileReader(fileName), ',' , '"' , noOfSkipLines);

		    while ((nextLine = reader.readNext()) != null) {
		        count++;				//update the current line number
		        
		        timeStamp = nextLine[0]; 	//timestamp first column of each line of input file
		        channel = nextLine[channelColumn]; 	//get the right column, which holds the strain channel data
		      	
		        //Calling the default constructor TimpStamp from class TimeStamp and rearrange data  
		        String newtime = TimeStamp.iso8601(timeStamp);
		        //Build a new string - representing each line, with all the required columns, in the output file
		        String[] data  = {newtime, channel};		         
	          
		        int id_pro_fk = SQLQueries.proc_obs_id_pro_fk_strain(nameOfService, id_opr_fk, id_prc_fk);

		          SQLQueries.insert_into_event_time(nameOfService, newtime, id_prc_fk);

		         int id_eti_fk=SQLQueries.event_time_id_eti_fk(nameOfService, data[0], id_prc_fk);

		         SQLQueries.insert_into_measures_strain(nameOfService, data[1], id_eti_fk, id_qi_fk, id_pro_fk);
			    }
//			    String etime_prc = SQLQueries.select_procedure_etime(nameOfService, nameOfProcedure);
			    SQLQueries.update_procedure_stime(nameOfService, startTime, nameOfProcedure);
			    SQLQueries.update_procedure_etime(nameOfService, endTime, nameOfProcedure);   
	       
	         System.out.println("-------------------------------------------------------------------------------------------");        

	         System.out.println(count + " Observations are inserted to the " + nameOfProcedure +" successfully");
		     System.out.println("Operation successfull");	
	//----------------------------------------------------------------------------------------------------------------------------------	         
	      reader.close();
	      }

	//----------------------------------------------------------------------------------------------------------------------------------
	      catch (Exception e){
	    	e.printStackTrace();
	    	System.err.println(e.getClass().getName()+":"+e.getMessage());
	      }
      
}
		      

}