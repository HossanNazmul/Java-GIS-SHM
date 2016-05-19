package mainGUIPackage;
//***********************************************************************************************************
//
//
//
//
//
//***********************************************************************************************************
public class TimeStamp {
	
	public static String iso8601 (String timeStamp)
	{	//String timeStamp = "13/05/2014 07:56:50.101750000";
		String date = timeStamp.substring(0,2); 					//split date
		String dash = "-";
		String month = timeStamp.substring(3,5);					//split month
		String year = timeStamp.substring(6,10);					//split year
		String sec = timeStamp.substring(11);						//split seconds
		String timeZone = "+0100";									//add timezone
		String timeNew = year + dash + month + dash + date + "T" + sec + timeZone;
		return timeNew;
	}
}
