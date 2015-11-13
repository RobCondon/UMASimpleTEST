/*
This module will scrap the UMA public course search listing
and load the results in a 2d array resembling the master_tmp_tbl
structure. 
Created By: Brian Boggs
Created: 10/15/2015
Last Updated: 11/12/2015
*/
package scrape;
import java.io.IOException;  
import org.jsoup.Jsoup; 
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;  
import java.util.regex.*;

public class Scrape {

    static int rawIndx = 0;
    static String[] locArray = new String[] {"Augusta", "Bangor", "WEB", "ITV", "Video Conference",
                                             "Rockland", "Ellsworth/Hancock", "Bath/Brunswick", "Houlton",
                                             "Rumford/Mexico", "Saco/Biddeford", "South Paris"};
    //current teachers
    static String[] instArray = new String[] {"Abbott", "Abramova", "Adams"
                                            , "Adrienne", "Amoroso", "Anderson"
                                            , "Audette", "Ayotte", "Backman"
                                            , "Baker", "Baldridge", "Bates"
                                            , "Bean", "Bearor", "Bedell"
                                            , "Bernheim", "Bickford", "Bisulca"
                                            , "Blanchette", "Blanke", "Blesh"
                                            , "Boerner", "Boone", "Botshon"
                                            , "Bowen", "Bowne", "Boyd"
                                            , "Breton", "Buckley", "Buckmaster"
                                            , "Burdick", "Burns", "Butler"
                                            , "Cahill", "Carras", "Carty"
                                            , "Churchill", "Clark", "Cleveland"
                                            , "Cloud", "Coe", "Cole"
                                            , "Comins", "Cook", "Corlew"
                                            , "Cousins", "Curran", "Curtin"
                                            , "Daisey", "Davitt", "Day"
                                            , "Dean", "Delano", "Demchur-Merry"
                                            , "Demers", "Der Simonian", "Desisto"
                                            , "Dewaters", "Dodge", "Doucette"
                                            , "Edwards", "Elias", "Elliott"
                                            , "Ellis", "Emery", "Ennamorati"
                                            , "Evans", "Farmer", "Felch"
                                            , "Feldhousen-Giles", "Fellman", "Fitzgerald"
                                            , "Foster", "Frazer", "Freeman"
                                            , "French", "Gallagher", "Gallant"
                                            , "Galloway", "Gerard", "Gianopoulos"
                                            , "Giordano", "Goetting", "Gonzalez"
                                            , "Goodridge", "Gove", "Gronros"
                                            , "Grover", "Grunder", "Guillemette"
                                            , "Haggard", "Hallundbaek", "Halpin"
                                            , "Harper", "Hayes-Grillo", "Hays"
                                            , "Hentges", "Hicks", "Hilton"
                                            , "Hinkley", "Holden", "Howard"
                                            , "Huckey", "Hull", "Hunt"
                                            , "Jakab", "Jenkins", "Jerosch"
                                            , "Johnson", "Jolda", "Jordan"
                                            , "Katz", "Kellerman", "Kellogg"
                                            , "Kenefic", "Kershner", "King"
                                            , "Kinsella", "Klose", "Kokoska"
                                            , "Lachance", "Lage", "Lake-Corral"
                                            , "Lamasney", "Lane", "Leach"
                                            , "Lee", "Legore", "Lemay"
                                            , "Leo", "Line", "Linhardt"
                                            , "Lisi", "Little", "Lodge"
                                            , "Long", "Ludders", "Lumb"
                                            , "Mahoney", "Marcolini", "Mascaro"
                                            , "Mayhew", "Mayo", "McAleer"
                                            , "McBean", "McCarthy", "McCord"
                                            , "McCue-Herlihy", "Mcgrath", "McGuire"
                                            , "McInnis", "McLaren", "McMahon Sawyer"
                                            , "Mehrmann", "Merckens", "Milligan"
                                            , "Molloy", "Mooney", "Morin"
                                            , "Moro", "Morrissette", "Moseley"
                                            , "Naber", "Naiden", "Needham-Curtis"
                                            , "Nelson", "Norris", "Norton"
                                            , "Noyes-Hull", "O'Brien", "Orth"
                                            , "Ortiz-Vidal", "Oxley", "Pare-Peters"
                                            , "Patterson", "Payson", "Peppe"
                                            , "Peterson Cyr", "Phippen", "Piatt"
                                            , "Pierce", "Pitcher", "Potter"
                                            , "Powers", "Precourt", "Rainey"
                                            , "Ray", "Reeves", "Retzlaff"
                                            , "Rice", "Richmond", "Rieff"
                                            , "Ritchie", "Roper", "Rottmann"
                                            , "Roy", "Ruddy", "Samuelian"
                                            , "Sanborn", "Sandberg", "Sapien"
                                            , "Schlenker", "Schneider", "Seymour"
                                            , "Shattuck", "Silverstein", "Simonson"
                                            , "Spearin", "Staff", "Stark"
                                            , "Stetson", "Stoddard", "Strout"
                                            , "Surrette", "Sychterz", "Szakas"
                                            , "Tagle", "Taylor", "Thaller"
                                            , "Trefts", "Trimble", "Twitchell"
                                            , "Vail", "VanKirk", "Vose"
                                            , "Watkins", "Waugh", "Weigle"
                                            , "Weinbrown", "Whitney", "Whitsel"
                                            , "Wigderson", "Willett", "Willette"
                                            , "Williams", "Willis", "Woodard"
                                            , "Zaremba"};
    static String[] dayArray = new String[] {"Monday", "Tuesday", "Wednesday", "Thursday",
                                             "Friday", "Saturday", "Sunday"};
    
    public static void main(String[] args) throws IOException{ 
        //set jsoup scrape destination
        Document courseScrapper = Jsoup.connect("http://joomla.uma.edu/cg.php?&m=1620").get();
        //get all td elements for over estimaed rec count
        Elements records = courseScrapper.select("#cgslide h3 + div > table.cgtabledisplay tbody td");
        //get all h3 elements for accurate course count
        Elements courses = courseScrapper.select("h3");
        
        int recordCount = records.size();
        int courseCount = courses.size();
        
        String[][] scrapeRaw = new String[recordCount][10];
        
        for(int scrapeIndx = 0; scrapeIndx < courseCount * 2; scrapeIndx = scrapeIndx + 2)
        {
            //collect data we can into scrapeRaw
            scrapeRaw[rawIndx][0] = courseScrapper.select("h3:eq(" + scrapeIndx + ") .cgcoursenum").text(); 
            scrapeRaw[rawIndx][1] = courseScrapper.select("h3:eq(" + scrapeIndx + ") .cgtitle").text(); 
            scrapeRaw[rawIndx][2] = courseScrapper.select("h3:eq(" + scrapeIndx + ") .cgcredits").text(); 
            scrapeRaw[rawIndx][3] = courseScrapper.select("h3:eq(" + scrapeIndx + ") + div .cgdesc").text(); 
            
            //testing//System.out.println(scrapeRaw[rawIndx][0]); 
            //testing//System.out.println(scrapeRaw[rawIndx][1]);
            //testing//System.out.println(scrapeRaw[rawIndx][2]);
            //testing//System.out.println(scrapeRaw[rawIndx][3]);
           
            Elements tabData = courseScrapper.select("h3:eq(" + scrapeIndx + ") + div > table tbody td");
            for (Element el : tabData)
            {
                
                if(el.text() != null || !el.text().equals(" "))
                {
                    //testing//System.out.println(el.text());        
                    //check for location value
                    for (int locIndx = 0; locIndx < locArray.length; locIndx++)
                    {
                        if (el.text().equals(locArray[locIndx]))
                        {
                            if (scrapeRaw[rawIndx][4] == null)
                            {
                                //testing//System.out.println(el.text());   
                                scrapeRaw[rawIndx][4] = el.text();   //set course location
                                break;
                            }
                            else    //if this is a new location for the same class, create new record
                            {
                                //testing//System.out.println(el.text() + " - New Line");  
                                rawIndx ++;
                                scrapeRaw[rawIndx][0] = scrapeRaw[rawIndx - 1][0];
                                scrapeRaw[rawIndx][1] = scrapeRaw[rawIndx - 1][1];
                                scrapeRaw[rawIndx][2] = scrapeRaw[rawIndx - 1][2];
                                scrapeRaw[rawIndx][3] = scrapeRaw[rawIndx - 1][3];
                                scrapeRaw[rawIndx][4] = el.text();
                            }
                        }
                    }
                    
                    //search for Note values
                    if (el.text().contains("Notes: "))
                    {
                        //testing//System.out.println(el.text());  
                        scrapeRaw[rawIndx][5] = el.text();   //set course location note
                    }
                    
                    //check for section values 
                    //possible values are ex: 12345, 12345 Augusta, 12345 Augusta/Bangor, 12345 South Paris, 12345 Bangor - Delayed Viewing
                    if (Pattern.matches("\\d{5}(\\s[aA-zZ]{1,}(\\s-\\s[aA-zZ]{1,}\\s[aA-zZ]{1,})?(/)?(\\s)?[aA-zZ]{1,})?", el.text()))
                    {
                        if (scrapeRaw[rawIndx][6] == null)
                        {
                            //testing//System.out.println(el.text());  
                            scrapeRaw[rawIndx][6] = el.text();   //set course section number
                        }
                        else
                        {
                            //testing//System.out.println(el.text() + " - New Line");  
                            rawIndx ++;
                            scrapeRaw[rawIndx][0] = scrapeRaw[rawIndx - 1][0];
                            scrapeRaw[rawIndx][1] = scrapeRaw[rawIndx - 1][1];
                            scrapeRaw[rawIndx][2] = scrapeRaw[rawIndx - 1][2];
                            scrapeRaw[rawIndx][3] = scrapeRaw[rawIndx - 1][3];
                            scrapeRaw[rawIndx][4] = scrapeRaw[rawIndx - 1][4];
                            scrapeRaw[rawIndx][5] = scrapeRaw[rawIndx - 1][5];
                            scrapeRaw[rawIndx][6] = el.text();       
                        }
                    }
                    
                    //check for day values
                    for (int dayIndx = 0; dayIndx < dayArray.length; dayIndx++)
                    {
                        if (el.text().equals(dayArray[dayIndx]))
                        {
                            //testing//System.out.println(el.text());  
                            scrapeRaw[rawIndx][7] = el.text();   //set course day
                            break;
                        }
                    }
                    
                    //search for time values
                    if (Pattern.matches("\\d{1,}:\\d\\d\\s[P,A]M\\s-\\s\\d{1,}:\\d\\d\\s[P,A]M", el.text()))
                    {
                        //testing//System.out.println(el.text());  
                        scrapeRaw[rawIndx][8] = el.text();   //set course time
                    }
                    
                    //search for instructor values
                    for (int instIndx = 0; instIndx < instArray.length; instIndx++)
                    {
                        if (el.text().equals(instArray[instIndx]))
                        {
                            //testing//System.out.println(el.text());  
                            scrapeRaw[rawIndx][9] = el.text();   //set course instructor
                            break;
                        }
                    }
                    
                }
            }
            rawIndx++;
        }
        
        for (int i = 0; i < scrapeRaw.length; i++)
        {
            System.out.println( i + " -- " + scrapeRaw[i][0] + " -- " + scrapeRaw[i][1] + " -- " + scrapeRaw[i][2] + " -- " + scrapeRaw[i][3] + " -- " + scrapeRaw[i][4] + " -- " + scrapeRaw[i][5] + " -- " + scrapeRaw[i][6] + " -- " + scrapeRaw[i][7] +
                               " -- " + scrapeRaw[i][8] + " -- " + scrapeRaw[i][9]);
            if (scrapeRaw[i][0] == null)
            {
                System.out.println(scrapeRaw.length);
                break;
            }
        }
    }
    
}
