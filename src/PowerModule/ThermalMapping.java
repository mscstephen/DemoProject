/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PowerModule;

import java.io.*;
/**
 *
 * @author fsmm2
 */
public class ThermalMapping {
    public ThermalMapping()
    {}

public float[][] getMap()
    {
    float[] array=new float[40];
    try{
    //this should probably be it's own function. will call it every time cooling plan needs to be updated.
String inputFilename = "Z:/TeamProject/DataCenter/src/thermalmap.txt";
                    FileInputStream fstream = new FileInputStream(inputFilename);
                    DataInputStream in = new DataInputStream(fstream);
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String strLine;
                    int i=0;
           //       System.out.println("List of Temps");
                    while((strLine=br.readLine())!=null)
                    {
                    float parsedHeat=Float.parseFloat(strLine);
                    array[i]=parsedHeat;
                    i++;

          //          System.out.println(i +": "+parsedHeat);
        }
}
catch(Exception e)
{System.out.println(e);}

    float[][] thermal=new float[4][10];

    for(int i=0; i<10; i++)
    {
        thermal[0][i]=array[i];
        thermal[1][i]=array[(i+10)];
        thermal[2][i]=array[(i+20)];
        thermal[3][i]=array[(i+30)];
    }
    return thermal;


}
}
