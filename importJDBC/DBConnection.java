package importJDBC;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
//	public static Connection DBConnect(String URL, String userName, String password) throws Exception

	public static Connection DBConnect() throws Exception
	   {
	
	Connection c = null;
    try {
       Class.forName("org.postgresql.Driver");
//       c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/istsos","postgres", "postgres");
       c = DriverManager.getConnection("jdbc:postgresql://quader.igg.tu-berlin.de:5432/istsos","postgres", "postgres");
//
//       String conn= "\"" + "jdbc:postgresql:" + URL+"\""+"," +"\""+ userName+"\""+", " +"\""+password+"\"" ;
//       System.out.println(conn);
//       c = DriverManager.getConnection(conn);
       c.setAutoCommit(false);
       System.out.println("Database successfully connected");
    }
    catch (Exception e){
  	e.printStackTrace();
  	System.err.println(e.getClass().getName()+":"+e.getMessage());
    }
	return c;
    }
}
