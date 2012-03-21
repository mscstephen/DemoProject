package thermalproject.optimization_thermal;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class cooling1
{
    public void main()
    {
        //PARAMETRES USED TO DEFINE THE SIZE OF THE DATA-CENTER
        //number of CPUs in each rack
        int rackSize = 10;
        //number of racks
        int rackNumber = 4;
        //OUTPUT PARAMETER (string array, the size is going to be the rackNumber)
        //cooling level
        int[] coolingLevel = new int[rackNumber];
        //cycle needed to initialize the coolingLevel, suppose everything is off at the beginning
        for(int j = 0; j<rackNumber; j++)
        {
            coolingLevel[j] = 0;
        }
        //An array of float is needed for each rack, the array has the size of a rack,
        //they are going to be used in order to read the input
        float[] rack1 = new float[rackSize];
        float[] rack2 = new float[rackSize];
        float[] rack3 = new float[rackSize];
        float[] rack4 = new float[rackSize];

        //Variables needed to calculate the current average temperature in each rack
        //Initialized at 25, could be outside temperature
        float AVGtemp1 = 25;
        float AVGtemp2 = 25;
        float AVGtemp3 = 25;
        float AVGtemp4 = 25;

        //The past AVG temperature for each rack is needed
        //Initialized as 25, could be outside temperature
        float past_Rack1 = 25;
        float past_Rack2 = 25;
        float past_Rack3 = 25;
        float past_Rack4 = 25;

        int fails = 0;

        while(true)
        {
            //count the number of fails, when searching for the correct schedule
            //boolean needed in order to notify the system when a correct schedule is found
            boolean scheduleFound = false;
            //repeat this cycle every 5 secs, untill a valid schedule is found
            while(scheduleFound == false)
            {
                scheduleFound = true;
                //try-catch block, used in order to read the input
                try
                {
                    //first of all, the file has to be found and read, the name of the file can be changed
                    //the input file should contain a list of floats, one for each line
                    String inputFilename = "thermalmap.txt";
                    FileInputStream fstream = new FileInputStream(inputFilename);
                    DataInputStream in = new DataInputStream(fstream);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));

                    //prepare the output files, one for the result, one for reporting messages
                    String outputFilename = "generated_cooling_plan.txt";
                    String reportFileError = "report.txt";
                    //filewriter, ready to complete the report file
                    FileWriter writer = new FileWriter(reportFileError);
                    PrintWriter pw = new PrintWriter(writer);

                    //STARTING TO PROCESS THE INPUT FILE AND VERIFY IT IS CORRECT
                    //temporary string used to read a line at a time from the input file
                    String strLine;
                    //integer counter, used in order to be sure that the size of the input is correct
                    int i = 0;

                    //read a line at a time, and cycle until the file ends
                    while ((strLine = br.readLine()) != null)
                    {
                        //parse the line as float, if not possible, error message is sent
                        float processed_temp = Float.parseFloat(strLine);
                        //check if the tamperature is in the correct range, if it is not...

                        if(processed_temp < 0 || processed_temp > 70)
                        {
                            //TO BE MODIFIED, A MESSAGE FILE SHOULD BE SENT TO DATABASE
                            pw.println("ERROR TYPE 1: Temperature out of valid range");
                            pw.close();
                            fails++;
                            scheduleFound = false;
                            try
                            {
                                //wait 5 seconds before doing a new attempt
                                Thread.currentThread().sleep(5000);
                            }
                            catch(InterruptedException ie){}
                        }
                        //Now the value is processed, depending on the value of i it is sent to the correct array
                        //Incoming data for rack 1, copied from the file to the correct array
                        if(i < rackSize)
                        {
                            rack1[i] = processed_temp;
                        }

                        //Incoming data for rack 2, copied from the file to the correct array
                        if(i >= rackSize && i < 2*rackSize)
                        {
                            rack2[i-rackSize] = processed_temp;
                        }

                        //Incoming data for rack 3, copied from the file to the correct array
                        if(i >= 2*rackSize && i < 3*rackSize)
                        {
                            rack3[i-2*rackSize] = processed_temp;
                        }

                        //Incoming data for rack 4, copied from the file to the correct array
                        if(i >= 3*rackSize && i < 4*rackSize)
                        {
                            rack4[i-3*rackSize] = processed_temp;
                        }

                        //incrementing the counter at each loop
                        i++;
                    }

                    //check that the correct amount of data have been received
                    if(i != rackNumber*rackSize)
                    {
                        //TO BE MODIFIED, A MESSAGE FILE SHOULD BE SENT TO DATABASE
                        pw.println("ERROR TYPE 2: Invalide input size");
                        pw.close();
                        fails++;
                        scheduleFound = false;
                        try
                        {
                            //wait 5 seconds before doing a new attempt
                            Thread.currentThread().sleep(5000);
                        }
                        catch(InterruptedException ie){}

                    }
                    else
                    {
                        pw.println("OK 1: valid input");
                        pw.close();
                    }
                    if(scheduleFound == true)
                    {
                        //Initialize the file writer, in order to have the output in a file
                        FileWriter writer1 = new FileWriter(outputFilename);
                        PrintWriter pw1 = new PrintWriter(writer1);
                        //apply the cooling policy to rack 1
                        coolingLevel[0]  = apply_cool(rack1,past_Rack1,AVGtemp1,coolingLevel[0]);
                        past_Rack1 = calculateAVG(rack1);
                        pw1.println(coolingLevel[0]);
                        //apply the cooling policy to rack 2
                        coolingLevel[1] = apply_cool(rack2,past_Rack2,AVGtemp2,coolingLevel[1]);
                        past_Rack2 = calculateAVG(rack2);
                        pw1.println(coolingLevel[1]);
                        //apply the cooling policy to rack 3
                        coolingLevel[2] = apply_cool(rack3,past_Rack3,AVGtemp3,coolingLevel[2]);
                        past_Rack3 = calculateAVG(rack3);
                        pw1.println(coolingLevel[2]);
                        //apply the cooling policy to rack 4
                        coolingLevel[3] = apply_cool(rack4,past_Rack4,AVGtemp4,coolingLevel[3]);
                        past_Rack4 = calculateAVG(rack4);
                        pw1.println(coolingLevel[3]);
                        //close the output file writer
                        writer1.close();
                        try
                        {
                            //wait for a certain amount of time
                            Thread.currentThread().sleep(15*1000 - fails*5000);
                            fails = 0;
                        }
                        catch(InterruptedException ie){}
                    }
                }

                catch(IOException e)
                {
                    //TO BE MODIFIED, A MESSAGE FILE SHOULD BE SENT TO DATABASE
                    System.out.println("ERROR TYPE 3: File not found");
                    //increment the number of fails
                    fails++;
                    System.out.println("" + fails);
                    try
                    {
                        //wait 5 seconds before doing a new attempt
                        Thread.currentThread().sleep(5000);
                    }
                    catch(InterruptedException ie){}
                }

                catch(NumberFormatException e)
                {
                    //TO BE MODIFIED, A MESSAGE FILE SHOULD BE SENT TO DATABASE
                    System.out.println("ERROR TYPE :Invalid Input Format");
                    //increment the number of fails
                    fails++;
                    try
                    {
                        //wait 5 seconds before doing a new attempt
                        Thread.currentThread().sleep(5000);
                    }
                    catch(InterruptedException ie){}
                }

            }
        }
    }
    //END OF THE MAIN METHOD HERE
    //NOW ADDITIONAL METHODS WILL FOLLOW


    //simple method used to calculate the maximum temperature
    public static float calculateMax(float[]a)
    {
        float max = 0;                                //maximum temperature initialized
        for(int k = 0; k < 10; k++)                //cycle through all the elements in the array
        {
            float currentTemp = a[k];
            if(currentTemp > max)                    //if the temperature is bigger than current max
            max = currentTemp;                        //update the max
        }
        return max;                                    //return the value
    }

    //simple method used to calculate the average temperature
    public static float calculateAVG(float[]a)
    {

        float sum = 0;                                //sum used in order to calculate the AVG temp
        for(int k = 0; k < 10; k++)                //cycle through all the elements in the array
        {
            float currentTemp = a[k];
            sum = sum + currentTemp;                //sum all the temperatures
        }
        float AVGtemp = sum/10;                    //calculate the average
        return AVGtemp;                                //return the average
    }

    //method used to calculate the cooling level rack-by-rack
    //in order to understand how it works, it is better to look at the automata
    public static int calculateCooling(float max,float AVGtemp,float delta,int s)
    {
        int level;
        if(max > 60)
        {
            level = 3;
            return level;
        }
        if(AVGtemp > 30 && delta > 4)
        {
            level = 3;
            return level;
        }
        if(AVGtemp > 30 && delta < 4 && delta > 2 && s == 0)
        {
            level = 2;
            return level;
        }
        if(AVGtemp > 30 && delta < 2 && s == 0)
        {
            level = 1;
            return level;
        }
        if(AVGtemp < 24)
        {
            level = 0;
            return level;
        }
        if(delta > 0 && s == 1)
        {
            level = 2;
            return level;
        }
        if(delta > 0 && s == 2)
        {
            level = 3;
            return level;
        }
        else
            level = s;
            return level;
    }

    //method used to apply the cooling policy
    public static int apply_cool(float[] a,float past,float AVGtemp, int level)
    {
        float max = calculateMax(a);                                    //calculate the max temperature
        AVGtemp = calculateAVG(a);                                        //calculate the AVG temperature
        float delta = AVGtemp - past;                                    //calculate the average temperature variation
        past = AVGtemp;                                                    //update the values
        System.out.println("Rack\nMAX: " + max + "\nAVG: " + AVGtemp + "\nDEL: " + delta);
        int temp = calculateCooling(max,AVGtemp,delta,level);        //apply the policy
        System.out.println("Cooling level: " + temp + "\n");            //checksum
        return temp;
    }
}