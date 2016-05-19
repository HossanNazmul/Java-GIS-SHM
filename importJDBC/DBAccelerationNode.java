package importJDBC;

import java.io.FileReader;

import mainGUIPackage.TimeStamp;
import au.com.bytecode.opencsv.CSVReader;
//***********************************************************************************************************
//
//
//
//
//
//***********************************************************************************************************
public class DBAccelerationNode {
	public static void Node38Series (String fileName, String nameOfService, String nameOfProcedure, int numOfProcedure, int noOfSkipLines, String startTime, String endTime){
	try {       
    	String timeStamp;
 	    String channel_x;
 	    String channel_y;
 	    String channel_z;
 	    String channel_temp;
 	    //inputs to be feed into the program
        String [] name_opr = {"'accelerationx'","'accelerationy'","'accelerationz'","'air:temperature'"};
 	    //Source file name and its location
   	 
         int id_qi_fk = 100;

         //observed property id according to observed property name.primary key of table observed_properties
          int id_prc_fk = SQLQueries.procedure_id_prc_fk(nameOfService, nameOfProcedure);
//          System.out.println( "id procedure = " + id_prc_fk);
         
         int id_opr_fk [] = new int [numOfProcedure];
        for (int i=0; i<name_opr.length;i++) 
         {
        id_opr_fk= SQLQueries.observed_properties_id_opr_fk(nameOfService, name_opr, numOfProcedure);
//         System.out.println( "id urn = " + id_opr_fk[i]);
         }
//	    String etime_prc = SQLQueries.select_procedure_etime(nameOfService, nameOfProcedure);
        int count = 0;
	    String[] rawData;
  
	    CSVReader reader = new CSVReader(new FileReader(fileName), ',' , '"' , noOfSkipLines);

	    while ((rawData = reader.readNext()) != null) {
	       count++;
	       
	        timeStamp = rawData[0];
	        channel_x = rawData[2];
	        channel_y = rawData[3];
	        channel_z = rawData[4];
	        channel_temp = rawData[5];
	        
	        //Calling the default constructor TimpStamp from class TimeStamp and rearrange data  
	        String newtime = TimeStamp.iso8601(timeStamp);
	        String [] data = {newtime, channel_x, channel_y, channel_z, channel_temp};
         
          int [] id_pro_fk = SQLQueries.proc_obs_id_pro_fk(nameOfService, id_opr_fk, id_prc_fk);

          SQLQueries.insert_into_event_time(nameOfService, newtime, id_prc_fk);

         int id_eti_fk=SQLQueries.event_time_id_eti_fk(nameOfService, data[0], id_prc_fk);
        
         SQLQueries.insert_into_measures(nameOfService, data, id_eti_fk, id_qi_fk, id_pro_fk);
	    }
//	    Update the time of the procedure
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

