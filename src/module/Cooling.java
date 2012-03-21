package module;
import module.Integration.CoolingSetting;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static module.Integration.CoolingSetting.*;

public class Cooling
{
    public void main() {


         CoolingStat statistics = new CoolingStat();

        // PARAMETRES USED TO DEFINE THE SIZE OF THE DATA-CENTER
        // number of CPUs in each rack

        int rackSize = 10;
        // number of racks

        int rackNumber = 4;
        // OUTPUT PARAMETER (string array, the size is going to be the rackNumber) cooling level
        int[] coolingLevel = new int[rackNumber];

        // cycle needed to initialize the coolingLevel, suppose everything is
        // off at the beginning
        for (int j = 0; j < rackNumber; j++) {
            coolingLevel[j] = 0;
        }
        // An array of float is needed for each rack, the array has the size of
        // a rack,
        // they are going to be used in order to read the input
        float[][] thermalMap = new float[rackNumber][rackSize];

        // Variables needed to calculate the current average temperature in each rack
        // Initialized at 25, could be outside temperature
        float[] AVGtemp = new float[rackNumber];
        for (int i = 0; i < rackNumber; i++) {
            AVGtemp[i] = 25;
        }

        // The past AVG temperature for each rack is needed
        // Initialized as 25, could be outside temperature
        float pastTemp[] = new float[rackNumber];
        for (int i = 0; i < rackNumber; i++) {
            pastTemp[i] = 25;
        }

        //==========================================================//
        CoolingPlan plan = new CoolingPlan();
        int fails = 0;


         //start of the program itself and run forever
        while (true) {
            if (fails == 12) {
                coolingLevel = safePlan();
                statistics.setCooling(coolingLevel);
                fails = 0;
                callSleep(4 * 60);
            }
        //==========================================================//
             // count the number of fails, when searching for the correct schedule
            // boolean needed in order to notify the system when a correct schedule is found
            boolean scheduleFound = false;
            // repeat this cycle every 5 secs, untill a valid schedule is found
            while (scheduleFound == false) {
                scheduleFound = true;
                // try-catch block, used in order to read the input
                try {
                    float[][] map = Integration.getTemperatures();
                    // Prepare the output files, one for the result, one for
                    // reporting messages


                   
                    //Integration.setCoolingPlan(new CoolingSetting[]{COOLING_OFF, COOLING_LOW, COOLING_MEDIUM, COOLING_HIGH});
                    //CoolingSetting[] coolingPlan = Integration.getCoolingPlan();
                    

                    String outputFilename = "output.txt";
                    String reportFileError = "report.txt";

                    // Filewriter, ready to complete the report file

                    FileWriter writer = new FileWriter(reportFileError);
                    PrintWriter pw = new PrintWriter(writer);

                    // STARTING TO PROCESS THE INPUT FILE AND VERIFY IT IS
                    // CORRECT
                    // integer counter, used in order to be sure that the size
                    // of the input is correct
                    int i = 0;

                    // read a line at a time, and cycle until the file ends
                    while (i < 40) {
                        // parse the line as float, if not possible, error
                        // message is sent
                        float processed_temp = map[i / rackSize][i % rackNumber];
                        // check if the tamperature is in the correct range, if
                        // it is not...

                        if (processed_temp < 0 || processed_temp > 70) {
                            // TO BE MODIFIED, A MESSAGE FILE SHOULD BE SENT TO
                            // DATABASE
                            pw.println("ERROR TYPE 1: Temperature out of valid range");
                            pw.close();
                            fails++;
                            scheduleFound = false;
                            callSleep(5);
                        }
                        // Now the value is processed, depending on the value of
                        // i it is sent to the correct array
                        // Incoming data for rack 1, copied from the file to the
                        // correct array
                        if (i < rackSize * rackNumber) {
                            thermalMap[i / rackSize][i % rackSize] = processed_temp;
                        }
                        i++;
                    }

                    // check that the correct amount of data have been received
                    if (i != rackNumber * rackSize) {
                        // TO BE MODIFIED, A MESSAGE FILE SHOULD BE SENT TO
                        // DATABASE
                        pw.println("ERROR TYPE 2: Invalide input size");
                        pw.close();
                        fails++;
                        scheduleFound = false;
                        callSleep(5);

                    } else {
                        pw.println("OK 1: valid input");
                        pw.close();
                    }
                    if (scheduleFound == true) {
                        // Initialize the file writer, in order to have the
                        // output in a file
                        FileWriter writer1 = new FileWriter(outputFilename);
                        PrintWriter pw1 = new PrintWriter(writer1);

                        for (int k = 0; k < rackNumber; k++) {
                            // apply the cooling policy to rack 1
                            coolingLevel[k] = apply_cool(thermalMap[k], pastTemp[k], AVGtemp[k], coolingLevel[k]);
                            pastTemp[k] = calculateAVG(thermalMap[k]);
                            pw1.println(coolingLevel[k]);
                        }
                        // close the output file writer
                        writer1.close();

                  //===================================================================//
                        int[] setCooling = new int[rackNumber];
                        for(int z=0;z<rackNumber;z++)
                        {
                            setCooling[z] = coolingLevel[z] + 1;
                        }
                        plan.setNum(setCooling);
                        plan.updateCoolingPlan();
                        callSleep(60 - 5 * fails);
                  //=====================================================================//
                  
                    }
                } catch (IOException e) {
                    // TO BE MODIFIED, A MESSAGE FILE SHOULD BE SENT TO DATABASE
                    System.out.println("ERROR TYPE 3: File not found");
                    // increment the number of fails
                    fails++;
                    System.out.println("" + fails);
                    callSleep(5);
                } 
            }
        }
    }
    
    // simple method used to calculate the maximum temperature
    public static float calculateMax(float[] a) {
        int x = a.length;
        float max = 0; // maximum temperature initialized
        for (int k = 0; k < x; k++) // cycle through all the elements in the
        // array
        {
            float currentTemp = a[k];
            if (currentTemp > max) // if the temperature is bigger than current
            // max
            {
                max = currentTemp; // update the max
            }
        }
        return max; // return the value
    }
    // simple method used to calculate the average temperature
    public static float calculateAVG(float[] a) {

        float sum = 0; // sum used in order to calculate the AVG temp
        for (int k = 0; k < a.length; k++) // cycle through all the elements in
        // the array
        {
            float currentTemp = a[k];
            sum = sum + currentTemp; // sum all the temperatures
        }
        float AVGtemp = sum / a.length; // calculate the average
        return AVGtemp; // return the average
    }

    // method used to calculate the cooling level rack-by-rack
    // in order to understand how it works, it is better to look at the automata
    public static int calculateCooling(float max, float AVGtemp, float delta,
            int s) {
        int level;
        if (max > 60) {
            level = 3;
            return level;
        }
        if (AVGtemp > 30 && delta > 4) {
            level = 3;
            return level;
        }
        if (AVGtemp > 30 && delta < 4 && delta > 2 && s == 0) {
            level = 2;
            return level;
        }
        if (AVGtemp > 30 && delta < 2 && s == 0) {
            level = 1;
            return level;
        }
        if (AVGtemp < 24) {
            level = 0;
            return level;
        }
        if (delta > 0 && s == 1) {
            level = 2;
            return level;
        }
        if (delta > 0 && s == 2) {
            level = 3;
            return level;
        } else {
            level = s;
        }
        return level;
    }
    // method used to apply the cooling policy
    public static int apply_cool(float[] a, float past, float AVGtemp, int level) {
        float max = calculateMax(a); // calculate the max temperature
        AVGtemp = calculateAVG(a); // calculate the AVG temperature
        float delta = AVGtemp - past; // calculate the average temperature variation

        past = AVGtemp; // update the values
        System.out.println("Rack\nMAX: " + max + "\nAVG: " + AVGtemp + "\nDEL: " + delta);
        int temp = calculateCooling(max, AVGtemp, delta, level); // apply the policy
        System.out.println("Cooling level: " + temp + "\n"); // checksum
        return temp;
    }

  public static void callSleep(int time) {
        try {
            //wait 5 seconds before doing a new attempt
            Thread.currentThread().sleep(time * 1000);
        } catch (InterruptedException ie) {
        }
    }

  public static int[] safePlan() {
        int[] plan = new int[4];
        for (int i = 0; i < 4; i++) {
            plan[i] = 3;
        }
        return plan;
    }

}