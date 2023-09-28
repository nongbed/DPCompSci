import java.io.*;
import java.util.*;

public class PremiereLeague
{
    public static String [] teams = new String [4];
    public static int [] PTS = new int [4];
    public static int qualify = -1;
    
    public static void main (String [] args) throws FileNotFoundException {
        new PremiereLeague ();
        
        IBIO.output("Your Slip has been recorded into the file");
    }
    
    public PremiereLeague ()
    {
        IBIO.output("");
        IBIO.output("");
        IBIO.output("Send in your team names");
        IBIO.output("Put in the points from the premiere league tables");
        IBIO.output("Depending on who's fourth right now, put in the points to qualify for the next round");
        IBIO.output("if your team does not meet the requirements you will be sent to the Europa League");
        IBIO.output("You can put in a maximum of four teams");
        IBIO.output("");
        IBIO.output("");
        
        
        
        
        //Set the qualifying point based on today's fourth placed team
        changeQualify();
        
                //initialise the arrays - this avoids problems with "null" (empty) records
        for (int i = 0; i <= 3; i++)
        {
            teams [i] = "-";
            PTS [i] = -1;
        }
        
        TeamsandPoints (); //enter the teams and the points 
        QualifiedAndRegisteration (); //see if qualified and gives the code 
     //only needed for people who have qualified for the next round, use the - symbol to break the code

    }
    


    
    public static void changeQualify()
    {
        qualify = IBIO.inputInt("What is the current points for the team at fourth place?");
    }
    
    public static void TeamsandPoints ()
    {
        for (int i = 0; i <= 3; i++)
        {
            teams [i] = "-";
            PTS [i] = -1;
        }     
        
        for (int i = 0; i <= 3; i++)
        {
            IBIO.output ("");
            teams [i] = IBIO.input ("TeamName?");
            // stop if the user puts in a -
            if (teams [i].charAt(0) == '-')
            {
                IBIO.output("Sorry maybe next season");
                break;
            }
            PTS [i] = IBIO.inputInt ("How many points did the team get? ");
   
        }        
    }
    
    public static void QualifiedAndRegisteration () //Qualification and registeration 
    {
        Random rand = new Random();
        IBIO.output ("LEAGUE TEAMS");
        IBIO.output ("The following have passed to the next round");
        int QualifyCode = 0; 
        for (int i = 0; i <= 3; i++){
            if (teams[i].charAt(0) == '-'){
                break;
            }
            String passed = "Not Qualified";
            if(PTS[i] >= qualify){
                passed = " have qualified";
                IBIO.output("Here are the qualification code for the teams that can advance to the next round");
                int QC = rand.nextInt(450);
                QualifyCode = QualifyCode + QC;
                IBIO.output ( teams[i] + " " + passed);
                IBIO.output("here is your qualification code: " + QualifyCode);
                IBIO.output("");
                IBIO.output("");
            }
            
            else
            {
                IBIO.output(passed);
            }

        }
        
            IBIO.output("Type in the number 0 if none of your teams have qualified for the next round");
            int stop = IBIO.inputInt("Type in 0 here: ");
        if(stop != 0){
            IBIO.output("If you have qualified please enter your Qualification Code to register for the next round");
            int QualifyNumber = IBIO.inputInt("Qualification Code: ");
            IBIO.output("");
            if(QualifyNumber == QualifyCode){
                IBIO.output("You are succesfully in the next round");
                IBIO.output("~~Qualification Slip~~");
                IBIO.output("~CODE:" + QualifyNumber + "~~");
                IBIO.output("Congratulations");
            
            }
        }
        else{ 
            IBIO.output("Thank you for checking");
        }
    }
    
    
}
 
    

