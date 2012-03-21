/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package module;

/**
 *
 * @author sd15
 */
public class CoolingStat {
	//instance variables
	int rackNumber = 4;
	int memory = 1000;
	int avgNumber;
	int coolingNumber;
	int[][] coolingLevels;
	float[][] avgTemp;

	//constructor
	public CoolingStat()
    {
        avgNumber = 0;
        coolingNumber = 0;
        coolingLevels = new int[rackNumber][memory];
        avgTemp = new float[rackNumber][memory];
        for(int i = 0; i < rackNumber; i++)
        {
        	for(int j = rackNumber; j< memory; j++)
        	{
        		coolingLevels[i][j] = 0;
        		avgTemp[i][j] = 0;
        	}
        }
    }

	//method to write the cooling plan and store it
    public void setCooling(int[] a)
    {
    	for(int k = 0; k < rackNumber; k++)
    	{
    		coolingLevels[k][coolingNumber] = a[k];
    		coolingNumber++;
    		coolingNumber = coolingNumber%memory;
    	}
    }
    //method to write the avgtemp and store them
    public void setAVG(float[] a)
    {
    	for(int k = 0; k < rackNumber; k++)
    	{
    		avgTemp[k][avgNumber] = a[k];
    		avgNumber++;
    		avgNumber = avgNumber%memory;
    	}
    }
    //methods to get the stats
    public int[][] getCooling()
    {
    	return coolingLevels;
    }
    public float[][] getAVG()
    {
    	return avgTemp;
    }
}
