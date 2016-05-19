package importJDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//***********************************************************************************************************
//
//
//
//
//
//***********************************************************************************************************
public class SQLQueries {

	static Connection c = null;   
	static Statement stmt = null;
    static String sql;
    static ResultSet rs;

	public static int procedure_id_prc_fk (String nameOfService, String nameOfProcedure) throws SQLException
	   {
        int id_prc_fk =0;
		try 
		{
		c = DBConnection.DBConnect();
		stmt = c.createStatement();
        rs = stmt.executeQuery( "SELECT id_prc FROM "+nameOfService+".procedures where name_prc = " + nameOfProcedure +";");
        while ( rs.next() ) {

           	id_prc_fk = rs.getInt("id_prc");
               //System.out.println( "id procedure = " + id_prc);
            }
       		} 
		catch (Exception e) {
//			e.printStackTrace();
		  	System.err.println(e.getClass().getName()+":"+e.getMessage());
		}
		return id_prc_fk;
	   }
	  
public static int[] observed_properties_id_opr_fk (String nameOfService, String[] name_opr, int numOfProcedure) throws SQLException
{
	int id_opr_fk [] = new int [numOfProcedure];
    for (int i=0; i<name_opr.length;i++) 
    {
	rs = stmt.executeQuery( "SELECT id_opr FROM "+nameOfService+".observed_properties where name_opr = " + name_opr[i] +";");
	while ( rs.next() ) 
	{
		//id_eti is automatic by the system
	 id_opr_fk[i]= rs.getInt("id_opr");
	}
    }
//System.out.println( "id urn = " + id_opr_fk[i]);
return id_opr_fk;

}
public static int observed_properties_id_opr_fk_strain (String nameOfService, String name_opr, int numOfProcedure) throws SQLException
{
	int id_opr_fk = 0;

	rs = stmt.executeQuery( "SELECT id_opr FROM "+nameOfService+".observed_properties where name_opr = " + name_opr +";");
	while ( rs.next() ) 
	{
		//id_eti is automatic by the system
	 id_opr_fk= rs.getInt("id_opr");
	}
//System.out.println( "id urn = " + id_opr_fk[i]);
return id_opr_fk;

}

public static int[] proc_obs_id_pro_fk (String nameOfService, int [] id_opr_fk, int id_prc_fk) throws SQLException
{
    int [] id_pro_fk = new int[id_opr_fk.length];
    for (int i=0; i<id_opr_fk.length;i++) 
    {
    rs = stmt.executeQuery( "SELECT id_pro FROM "+nameOfService+".proc_obs where id_opr_fk = " + id_opr_fk[i] + "and  id_prc_fk = " + id_prc_fk+";");
    while ( rs.next() ) {
   	 //id_eti is automatic by the system
       id_pro_fk [i] = rs.getInt("id_pro");
    }
//    System.out.println( "id observed properties = " + id_pro_fk[i]);
//    System.out.println( "id observed properties = " + id_opr_fk[i]);

    }
	return id_pro_fk;
}
public static int proc_obs_id_pro_fk_strain (String nameOfService, int id_opr_fk, int id_prc_fk) throws SQLException
{
    int id_pro_fk = 0;

    rs = stmt.executeQuery( "SELECT id_pro FROM "+nameOfService+".proc_obs where id_opr_fk = " + id_opr_fk + "and  id_prc_fk = " + id_prc_fk+";");
    while ( rs.next() ) {
   	 //id_eti is automatic by the system
       id_pro_fk = rs.getInt("id_pro");
    }
//    System.out.println( "id observed properties = " + id_pro_fk);
//    System.out.println( "id observed properties = " + id_opr_fk);

	return id_pro_fk;
}
public static void insert_into_event_time (String nameOfService, String newtime, int id_prc_fk) throws SQLException
{
    sql = "INSERT INTO "+nameOfService+".event_time (id_prc_fk,time_eti) "
    + "VALUES ("+ id_prc_fk + "," + "'" +newtime+"'" +");";
    stmt.executeUpdate(sql);
//    System.out.println("The time inserted into event_time table successfully");
	}

public static int event_time_id_eti_fk (String nameOfService, String data  , int id_prc_fk) throws SQLException
{
    int id_eti_fk=0;
    rs = stmt.executeQuery( "SELECT id_eti FROM "+nameOfService+".event_time where time_eti = " + "'" +data+ "'" + "and  id_prc_fk = " + id_prc_fk+";");
    while ( rs.next() ) {
   	 //id_eti_fk is automatic by the system
   	 id_eti_fk = rs.getInt("id_eti");
       
//   System.out.println( "id_time = " + id_eti_fk);

    }     
	return id_eti_fk;
}

public static void insert_into_measures (String nameOfService, String [] data, int id_eti_fk, int id_qi_fk, int [] id_pro_fk) throws SQLException
{
	for (int i=0; i<id_pro_fk.length;i++)
    {
    float val_msr = Float.parseFloat(data[1+i]);
    sql = "INSERT INTO "+nameOfService+".measures (id_eti_fk,id_qi_fk,id_pro_fk,val_msr) "
   		 	  + "VALUES ("+ id_eti_fk + "," + id_qi_fk+ ","+ id_pro_fk[i]+ "," +val_msr+");";
    stmt.executeUpdate(sql);

    }
//    System.out.println("The measurements inserted into measures table successfully");
	}
public static void insert_into_measures_strain (String nameOfService, String data, int id_eti_fk, int id_qi_fk, int id_pro_fk) throws SQLException
{

    float val_msr = Float.parseFloat(data);
    sql = "INSERT INTO "+nameOfService+".measures (id_eti_fk,id_qi_fk,id_pro_fk,val_msr) "
   		 	  + "VALUES ("+ id_eti_fk + "," + id_qi_fk+ ","+ id_pro_fk+ "," +val_msr+");";
    stmt.executeUpdate(sql);

//    System.out.println("The measurements inserted into measures table successfully");
	}

public static void update_procedure_stime (String nameOfService,String startTime, String nameOfProcedure) throws SQLException
{	
	String stime_prc="";
    rs = stmt.executeQuery( "SELECT stime_prc FROM "+nameOfService+".procedures where name_prc="+nameOfProcedure+";");
    while ( rs.next() ) {
   	 //id_eti_fk is automatic by the system
    	stime_prc = rs.getString("stime_prc");
    }
   if (stime_prc == null)
   {
	sql = "update "+nameOfService+".procedures set stime_prc='"+startTime+"' where name_prc="+nameOfProcedure+";" ;
    stmt.executeUpdate(sql);
//    System.out.println("Starting time is updated into procedure table");
   }
	}
//GML:beginPosition for POST XML encoding 
public static String select_procedure_etime (String nameOfService, String nameOfProcedure) throws Exception
{
//	sql = "SELECT etime_prc FROM "+nameOfService+".procedures where name_prc="+nameOfProcedure+";" ;
	String etime_prc="";
	c = DBConnection.DBConnect();
	stmt = c.createStatement();
    rs = stmt.executeQuery( "SELECT etime_prc FROM "+nameOfService+".procedures where name_prc="+"'"+nameOfProcedure+"'"+";");
    while ( rs.next() ) {
   	 //id_eti_fk is automatic by the system
    	etime_prc = rs.getString("etime_prc");
       
//   System.out.println( "end time = " + etime_prc);
        c.close();
    }     
	return etime_prc;
	}

public static void update_procedure_etime (String nameOfService,String endTime, String nameOfProcedure) throws SQLException
{
	sql = "update "+nameOfService+".procedures set etime_prc='"+endTime+"' where name_prc="+nameOfProcedure+";" ;
    stmt.executeUpdate(sql);
//    System.out.println("End time is updated into procedure table");
    //Close connection
    stmt.close();
    rs.close();
    c.commit();
    c.close();
	}
}