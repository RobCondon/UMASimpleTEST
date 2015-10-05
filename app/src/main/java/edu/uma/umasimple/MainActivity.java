package edu.uma.umasimple;
//Import Classes needed for program

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import edu.uma.umasimple.R;
import edu.uma.umasimple.ExtendedCalendarView.OnDayClickListener;

//Declare Class MainActivity (Activity sub-class)
public class MainActivity extends Activity {
	LinearLayout forContent;
	RelativeLayout CourseOfferings;
	TabHost tabHost;
	Context tthis = this;
	ArrayList<String> courseList;
	Boolean loggedin = false;
	Boolean loginComplete = false;
	Button login;
	Button skip;
	Integer currentView = 0;
	Button btnGetCourses;
	// Override Activity's onCreate method
	// Initializes Android app.
	@Override
 protected void onCreate(Bundle savedInstanceState) 
	{
		// passing in Bundle to the superclass' onCreate method
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);
     forContent = (LinearLayout)findViewById(R.id.ForContent);  //grab the LinearLayout from activity_main for removing and adding elements on tab press
   
     tabHost=(TabHost)findViewById(R.id.tabhost); //grab tab host from activity_main
     tabHost.setup();
     int padding_in_dp = 12;  // 6 dps
     final float scale = getResources().getDisplayMetrics().density;
     int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
     
     /* Fix for OpenSans Fonts */
     Typeface OpenSans = Typeface.createFromAsset(getAssets(), "fonts/OpenSans-Regular.ttf");
     SpannableStringBuilder customFonttab1 = new SpannableStringBuilder("Calendar");
     SpannableStringBuilder customFonttab2 = new SpannableStringBuilder("Email");
     SpannableStringBuilder customFonttab3 = new SpannableStringBuilder("Blackboard");
     SpannableStringBuilder customFonttab4 = new SpannableStringBuilder("Course\nOfferings");
     customFonttab1.setSpan(new CustomTypefaceSpan("", OpenSans), 0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
     customFonttab2.setSpan(new CustomTypefaceSpan("", OpenSans), 0, 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
     customFonttab3.setSpan(new CustomTypefaceSpan("", OpenSans), 0, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
     customFonttab4.setSpan(new CustomTypefaceSpan("", OpenSans), 0, 16, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
     
     TabSpec spec1=tabHost.newTabSpec("Calendar"); //setup tabs
     spec1.setContent(R.id.tab1);
     spec1.setIndicator(customFonttab1);
     TabSpec spec2=tabHost.newTabSpec("Email");
     spec2.setIndicator(customFonttab2);
     spec2.setContent(R.id.tab2);

     TabSpec spec3=tabHost.newTabSpec("Blackboard");
     spec3.setIndicator(customFonttab3);
     spec3.setContent(R.id.tab3);

     TabSpec spec4=tabHost.newTabSpec("Course Offerings");
     spec4.setIndicator(customFonttab4);
     spec4.setContent(R.id.tab4);
     
     tabHost.addTab(spec1); //add tabs to tab host
     tabHost.addTab(spec2);
     tabHost.addTab(spec3);
     tabHost.addTab(spec4);  
     TextView title = (TextView) tabHost.getTabWidget().getChildAt(3).findViewById(android.R.id.title); // get tab index 3 
     title.setSingleLine(false); //and let it use more then two lines so Course offerings can be displayed properly
     //tabHost.getTabWidget().setShowDividers(R.drawable.abc_list_divider_holo_dark);
     
     tabHost.getTabWidget().getChildAt(3).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
        	 getCourseSelectForm();
         }
     });
     tabHost.getTabWidget().getChildAt(1).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
        	 
        	 getLogin();
        	 
         }
     });
     tabHost.getTabWidget().getChildAt(0).setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
             
             getCalendar();
         }
     });
     for(int i=0; i < 4; i++) {
	    	tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_bg_selector);
	    	if(i == 3)
	    		tabHost.getTabWidget().getChildAt(i).setPadding(0, 0, 0, padding_in_px);
	    	else
	    		tabHost.getTabWidget().getChildAt(i).setPadding(0, 0, 0, (int) (padding_in_px * 1.6));

     }
     if ((savedInstanceState != null) && (savedInstanceState.getSerializable("currentView") != null))
     {
    	 currentView = (Integer) savedInstanceState.getSerializable("currentView");
     }

     	if(currentView == 0){
     		getCalendar(); }
     	else if(currentView == 1){
     		getMail();
     	}
     	else if(currentView == 2){
     		//placeholder for blackboard method
     	}
     	else if(currentView == 3){
     		populateCourseListview();
     	}
     	else if(currentView == 4){
     		getLogin();
     	}
     	else if(currentView == 5){
     		getCourseSelectForm();
     	}
     	
     	
     		
    
	}
	void getLogin() 
	{
		 tabHost.setCurrentTab(1);
		 currentView = 4;
		 forContent.removeAllViews();
		 forContent.addView(getLayoutInflater().inflate(R.layout.login, forContent, false));
	     login = (Button) findViewById(R.id.btnLogin);
	     skip = (Button) findViewById(R.id.btnSkip);
	     
	     login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),"Invalid username or Password, please try again", Toast.LENGTH_LONG).show();
			}
		});
	     skip.setOnClickListener(new OnClickListener() {
	    		@Override
	    		public void onClick(View v) {
	    			// TODO Auto-generated method stub
	    			getMail();
	    			
	    		}
	    	});
	}
	void getCourseSelectForm()
	{
	 tabHost.setCurrentTab(3);
	 currentView = 5;
   	 forContent.removeAllViews();
   	 forContent.addView(getLayoutInflater().inflate(R.layout.course_select_form, forContent, false));
   	 btnGetCourses = (Button) findViewById(R.id.btnGetCourses);
   	 btnGetCourses.setOnClickListener(new OnClickListener() {
   			@Override
   			public void onClick(View v) {
   				// TODO Auto-generated method stub
   				populateCourseListview(); 
   			}
   		});
	}
	void getMail()
	{
		currentView = 1;
		forContent.removeAllViews();
        forContent.addView(getLayoutInflater().inflate(R.layout.mail, forContent, false));
	}
	void getCalendar()
	{
//******************************************************OBJECT ACTIVITY******************************************************
     
     // New Intent Object for the UMA Calendar
    // Intent calendarIntent = new Intent(this, CalendarActivivty.class);
    // startActivity(calendarIntent);// Begin Calendar Activity 
   //  finish();// terminate Calendar Activity 
	 tabHost.setCurrentTab(0);
	 currentView = 0;
	 forContent.removeAllViews();
     forContent.addView(getLayoutInflater().inflate(R.layout.calendarview, forContent, false));
     CalendarRetriever cr = new CalendarRetriever(getContentResolver());
     cr.execute();// execute CalendarRetriever object
     
     // ExtendedCaldendarView object
     ExtendedCalendarView calendar = (ExtendedCalendarView)findViewById(R.id.calendarView1);
     
     
     // Anonymous inner class OnDayClickListener
     calendar.setOnDayClickListener(new OnDayClickListener() 
     {
         
     	// Override method onDayClicked 
			@Override
			public void onDayClicked(AdapterView<?> adapter, View view,	int position, long id, Day day) 
			{
				Log.d("", String.valueOf(day.getEvents().size()));
				Log.d("num of events: ", String.valueOf(day.getNumOfEvenets()));

					
					Log.d(day.getEvents().get(0).getStartDate("yyyy-MM-dd'T'HH:mm:ss"), day.getEvents().get(0).getDescription());
					//Toast.makeText(tthis, day.getEvents().get(0).getStartDate("yyyy-MM-dd'T'HH:mm:ss") + day.getEvents().get(0).getDescription() ,Toast.LENGTH_SHORT).show();// show event in toast dialog
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							tthis);
			 
						// set title
						alertDialogBuilder.setTitle("Your Title");
			 
						// set dialog message
						alertDialogBuilder
							.setMessage(day.getEvents().get(0).getDescription())
							.setCancelable(false)
							.setTitle(day.getEvents().get(0).getStartDate("yyyy-MM-dd'T'HH:mm:ss"))
							.setPositiveButton("OK",new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,int id) {
										
								}
							  });

							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
				
			}// end method onDayClicked
     });
	}
	void getList()
	 {
		 courseList.add("CIS 131 Web Applications and Development");
		 courseList.add("CIS 210 Programming Concepts");
		 courseList.add("CIS 231 Web Applications Development I");
		 courseList.add("CIS 251 Web Authoring Tools");
		 courseList.add("CIS 303 Management Information Systems");
		 courseList.add("CIS 380 Internship");
		 courseList.add("COL 100 Introduction to the College Experience");
		 courseList.add("COL 214 Professionalism in the Workplace"); 
	 }
	void populateCourseListview()
	{
		currentView = 3;
		tabHost.setCurrentTab(3);
        forContent.removeAllViews();
        forContent.addView(getLayoutInflater().inflate(R.layout.course_offerings, forContent, false));
        ListView coursesListView = (ListView)findViewById(R.id.courseList); //grab the listview from the layout
        courseList = new ArrayList<String>(); //instantiate courseList
        
        getList();  //for getting a list of courses and adding to the arrayList
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(tthis,R.layout.lv_textview, courseList); //used a custom view to add styling
        
        coursesListView.setAdapter(arrayAdapter); //apply the adapter to the ListView
        
        coursesListView.setOnItemClickListener(new OnItemClickListener() {  //just an example click listener for displaying course detailed info
       	  public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
             {
       		  String selectedCourse=courseList.get(position);
       		  
       		  Toast.makeText(getApplicationContext(),  "Course Selected: " +selectedCourse, Toast.LENGTH_LONG).show();
             }
        });
		
	}
	//Custom Fonts
	    public class CustomTypefaceSpan extends TypefaceSpan {
	        private final Typeface newType;

	        public CustomTypefaceSpan(String family, Typeface type) {
	            super(family);
	            newType = type;
	        }

	        @Override
	        public void updateDrawState(TextPaint ds) {
	            applyCustomTypeFace(ds, newType);
	        }

	        @Override
	        public void updateMeasureState(TextPaint paint) {
	            applyCustomTypeFace(paint, newType);
	        }

	        private void applyCustomTypeFace(Paint paint, Typeface tf) {
	            int oldStyle;
	            Typeface old = paint.getTypeface();
	            if (old == null) {
	                oldStyle = 0;
	            } else {
	                oldStyle = old.getStyle();
	            }

	            int fake = oldStyle & ~tf.getStyle();
	            if ((fake & Typeface.BOLD) != 0) {
	                paint.setFakeBoldText(true);
	            }

	            if ((fake & Typeface.ITALIC) != 0) {
	                paint.setTextSkewX(-0.25f);
	            }

	            paint.setTypeface(tf);
	        }
			public void onDayClicked(AdapterView<?> adapter, View view,	int position, long id, Day day) 
			{
				Log.d("", String.valueOf(day.getEvents().size()));
				Log.d("num of events: ", String.valueOf(day.getNumOfEvenets()));

				
				for (Event e : day.getEvents())
				{
					
					Log.d(e.getStartDate("yyyy-MM-dd'T'HH:mm:ss"), e.getDescription());
					Toast.makeText(getApplicationContext(), e.getStartDate("yyyy-MM-dd'T'HH:mm:ss") + e.getDescription() ,Toast.LENGTH_SHORT).show();// show event in toast dialog
				}
				
			}
}
	    @Override
	    protected void onSaveInstanceState(Bundle savedInstanceState) {
	        super.onSaveInstanceState(savedInstanceState);
	        savedInstanceState.putSerializable("currentView", currentView);
	    }

	    // end class MainActivity 
}
