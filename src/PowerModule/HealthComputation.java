/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PowerModule;

import PowerModule.Power;
import java.util.List;

/**
 *
 * @author jdoh2
 */
public class HealthComputation {

    public int columns;//number of columns
    public int rows;//number of rows
    public int v;//value of health
    public int[][] health;//health matrix of cpu's
    public float[][] temp;//temp of sectors
        int[] databaseInt = new int[40];
    Power power = new Power();
    List list = null;

    public HealthComputation(int c, int r) {
        this.rows = r;
        this.columns = c;
        health = new int[c][r];
        temp = new float[c][r];
    }

    public HealthComputation(int[][] health, double[][] temp) {
        // getting the current health matrix
        rows = health[0].length;
        columns = health.length;
        this.health = new int[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                health[columns][rows] = this.health[i][j] = health[i][j];
            }
        }
        // getting the current temp matrix
        
        rows = temp[0].length;
        columns = temp.length;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                temp = power.getThermal();
               
            }
        }
    }

    public void calculateHealth() {
//calculate the health of the cpu
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
            
                double[][] temp1 = power.getThermal();
              
                System.out.println(i +"."+ j+"TEMP  =" + temp1[i][j]);
         
                //      if (health1 == 0) {
                if (temp1[i][j] < 81.0) {
                    setHealth(i, j, 1);
                    databaseInt[count] = 1;
                } else {
               
                    setHealth(i, j, 2);
                    list.add(2);
                    databaseInt[count] = 2;
                }
                count++;
            }
        }
    }

    public int getHealth(int c, int r) {
        // returns health of given location
        return health[c][r];
    }
public int[][] getHealth(){
    return this.health;
}
    public float getTemp(int c, int r) {
        // returns temp of given location
        return temp[c][r];
    }

    public void setHealth(int c, int r, int v) {
        // sets the current health
        this.rows = r;
        this.columns = c;
        health[c][r] = v;
    }
}
