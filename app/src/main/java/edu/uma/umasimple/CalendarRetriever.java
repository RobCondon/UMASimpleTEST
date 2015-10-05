//**************************************************** CalendarRetriever.java****************************************************//
// Purpose: Retrieve Calendar Information/ perform Async. tasks
// CIS 389 - Software Engineering 
// Summer 2014
// Updated: 7/23/2014
// Last Updated By: Dustin Fairbanks

// User Defined Package
package edu.uma.umasimple;

// import classes needed for program 
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

//import org.apache.http.client.HttpClient;

//import org.apache.http.impl.client.HttpClientBuilder;


// import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


// Declare class CalendarRetriever (AsyncTask sub-class)
public class CalendarRetriever extends AsyncTask<String, Void, Void> 
{
	
	//private final HttpClient Client = new DefaultHttpClient();

	int calendarCount  = 6;// size of arrays for calendar
    private String[] content;// ?
    String[] serverURLs;// String array to hold server URLs
    String data = "alt=json-in-script&callback=insertAgenda&orderby=starttime&max-results=99&singleevents=true&sortorder=ascending&futureevents=true";
	// Calendar myCalendar;// Declaring new Calendar object
    ContentResolver myContentResolver;
	
	// CalendarRetriever constructor
    public CalendarRetriever(ContentResolver mcr) 
    {
        super();// call to superclass constructor
        // this.myCalendar = c;// assign parameter to myCalendar object
        
        this.myContentResolver = mcr;
        Log.d("", "");// log output
        
        serverURLs = new String[calendarCount];// Instantiate serverURLs object
        
        // Place server URLs in array
        serverURLs[0] = "maine.edu_l20kpu0tog2mr38iqr9q81gs14@group.calendar.google.com"; // UMA Augusta Student Life
      //serverURLs[0] = "maine.edu_clbkfcu937eeqh0kv7upl7oqdg@group.calendar.google.com"; // WHY IS THIS LINE COMMENTED OUT?
        serverURLs[1] = "maine.edu_t16osk004sl2v44hpsn2bgodtk@group.calendar.google.com"; // UMA Alumni
        serverURLs[2] = "maine.edu_fh0vu435ia61dfhsa6jiatpho8@group.calendar.google.com"; // UMA Architecture Program
        serverURLs[3] = "maine.edu_md572qqg0tk7ob1tdrahotd3u4@group.calendar.google.com"; // UMA General
        serverURLs[4] = "maine.edu_f8rl2bs8mgfobn5oeb0umdt6cs@group.calendar.google.com"; // UMA Bangor Student Life
        serverURLs[5] = "maine.edu_8lrn81vfsmfi0t2qbp93ad7t0o@group.calendar.google.com"; // UMA French program
        
        content = new String[calendarCount];// Instantiate content object
     }// end CalendarRetriever constructor
	
    // method onPreExecute
	protected void onPreExecute() 
	{
		Log.e("onPreExecute", "");//log output
	}
	
	// method doInBackground
	protected Void doInBackground(String... urls) 
	{
		
		// make post call to web server
		
		BufferedReader reader = null;
		
		int loopCount = 0;
		
		for (String serverURL : serverURLs)
		{
			StringBuilder sb = new StringBuilder();
			Log.d("before data to sever", serverURL);// log output
			
			// send data
			try
			{

				Log.d(serverURL + data, serverURL + data);// log output
				
				URL url = new URL("http://www.google.com/calendar/feeds/" + serverURL + "/public/full?alt=json&orderby=starttime&max-results=1000&singleevents=true&sortorder=ascending&futureevents=true");
				
				//Log.d("url", url.toString());// log output
				
				// send post data request
				URLConnection conn = url.openConnection();
				conn.connect();

				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = null;
				
				// read server response
				//Log.d("reader var: " , reader.toString());// log output
				while((line = reader.readLine()) != null)
				{
					// append server response in string
					sb.append(line); // why the "" ?
					//Log.d("line var: ", line);
				}// end while
				
				content[loopCount] = sb.toString();
				loopCount++;
			}// end try
			catch (MalformedURLException e)
			{ 
	        	//Error = e.getMessage();
				Log.d("MalformedURLException", "");// log output
	        } // end catch MalformedURLException
	        catch (IOException e) 
	        {   
	        	//Error = e.getMessage();
	        	Log.d("IOException" , "");// log output
	        }// end catch IOException
	        catch(Exception ex)
	        {
	            //Error = ex.getMessage();
	        	Log.d("Exception", "");// log output
	        }// end catch Exception
	        finally
	        {
	            try
	            {
	  
	                reader.close();// close BufferedReader
	            }// end try
	            catch(Exception ex) {}// end catch Exception
	        } // end try catch
		} // end for loop
		
		return null;
	} // End of method doInBackground
	
	
	// method onPostExecute
	protected void onPostExecute(Void unused)
	{
		TimeZone tz = TimeZone.getDefault();
		// start parsing the response JSON data
		Log.d("onPostExecute", "OnPostExecute");// log output
		String outputData = "";
		
		
		
			for (String oneCalendarContent : content)
			{
				try
				{
					JSONObject jsonResponse;
					
		            jsonResponse = new JSONObject(oneCalendarContent);
		            JSONObject JSONObjecte= jsonResponse.optJSONObject("feed");
		            JSONArray jsonSubNode = JSONObjecte.optJSONArray("entry");
		            
		            for (int z = 0; z < jsonSubNode.length(); z++)
		            {
		                    // Get Object for each JSON node.
		                    JSONObject jsonChildNode = jsonSubNode.getJSONObject(z);
		                      
		                    // Fetch node values 
		                    String title       = jsonChildNode.getJSONObject("title").getString("$t").toString();
		                    String startTime     = jsonChildNode.getJSONArray("gd$when").getJSONObject(0).get("startTime").toString();
		                    String endTime = jsonChildNode.getJSONArray("gd$when").getJSONObject(0).get("endTime").toString();
		                    String descrip  = jsonChildNode.getJSONObject("content").get("$t").toString(); // jsonChildNode.get("content").
		                    
		                    //String startTime, String description, String attendess, String location, String endTime
		                    Log.d("Event Added: ",startTime + " " + descrip);

		                    ContentValues values = new ContentValues();
	                    	values.put(CalendarProvider.COLOR, Event.COLOR_GREEN);
	                    	values.put(CalendarProvider.DESCRIPTION, descrip);
	                    	values.put(CalendarProvider.LOCATION, "Some location");
	                    	values.put(CalendarProvider.EVENT, title);
	                    	Calendar cal = Calendar.getInstance();
	                    	
	                    	
	                    	Date startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(startTime);
	                    	
	                    	cal.setTime(startDate);
	                    	Log.d(startDate.toString(), descrip);
	                    	
	                    	int julianDay = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
	                        values.put(CalendarProvider.START, cal.getTimeInMillis());
	                        values.put(CalendarProvider.START_DAY, julianDay);
	                        
	                        Date endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(endTime);
	                        Log.d(endDate.toString(), "");
	                        
	                        
	                        cal.setTime(endDate);
	                        int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
	                        values.put(CalendarProvider.END, cal.getTimeInMillis());
	                        values.put(CalendarProvider.END_DAY, endDayJulian);

	                        Uri uri = myContentResolver.insert(CalendarProvider.CONTENT_URI, values);         
		            }// end inner for
				} // end of try

				//catch (JSONException e) 
				//{
					//e.printStackTrace();
					
					//Log.d("JSON EXCEPTION!!!!!!!","JSON EXCEPTION!!!!!!!");
					
					
				//}// end catch JSONException	
					//catch (ParseException e) {
			 		// startDate or endDate parse failed
					// TODO Auto-generated catch block
					//e.printStackTrace();
				//}	
				catch (Exception e)
				{
					// nothing					
				}
				
				// InvocationTargetException
	    		
			}// end outer for	

		


	} // end method onPostExecute 
}// end class CalendarRetriever 
