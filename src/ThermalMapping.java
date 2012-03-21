/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//REDUNDANT FILE
package thermalproject;

import java.io.*;
/**
 *
 * @author fsmm2
 */
public class ThermalMapping {


public static double[][] getMap(double[] input)
    {
    //this should probably be it's own function. will call it every time cooling plan needs to be updated.
    double[][] thermal=new double[4][10];

    for(int i=0; i<10; i++)
    {
        thermal[0][i]=input[i];
        thermal[1][i]=input[(i+10)];
        thermal[2][i]=input[(i+20)];
        thermal[3][i]=input[(i+30)];
    }
    return thermal;


}
}

