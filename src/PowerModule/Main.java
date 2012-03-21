/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PowerModule;

import j.h.entities.PowerUpdate;

/**
 *
 * @author noc3
 */
public class Main {

    static final int ROWS = 10;
    static final int COLS = 4;
    static double jobs[][];
    static boolean health[][];
    static float computingPower;
    static float nonComputingPower;
    static float totalPower;
    static Power power; // object for communicating

    public void main() {
        PowerUpdate pow = new PowerUpdate();
        pow.ChangeStatus(true);
         power = new Power();

        HealthComputation helt = new HealthComputation(4, 10);

        float currentTemp = 25;

        ComputingPower comp = new ComputingPower();
        NonComputing nonComp = new NonComputing();
        TotalPower total = new TotalPower();
        PUE pue = new PUE();
        nonComp.setIntakeTemp(currentTemp);
        jobs = new double[COLS][ROWS];
        health = new boolean[COLS][ROWS];
      /*  for (int columes = 0; columes < jobs.length; columes++) {
            for (int rows = 0; rows < jobs[0].length; rows++) {
                jobs[columes][rows] = 0.60;
                health[columes][rows] = true;
            }
        }*/

        comp.computingInputs(power.getProcessingRate(),power.getHealth());
        computingPower = comp.getComputingPower();
        System.out.println("computing: " + computingPower);

        int[] a = new int[4];
        a = power.getCooling();
    
        nonComp.changePlan(a);

        nonComp.setIntakeTemp(currentTemp);
        System.out.println("Total power cost on LOW mode:" + nonComp.getTotalCost());
        nonComputingPower = nonComp.getTotalCost();

        total.computing = computingPower;
        total.nonComputing = nonComputingPower;
        total.CalcTotalPower();
        totalPower = total.getTotalPower();
        pue.CalcPUE(computingPower, totalPower);

        System.out.println("GET TEMP 2.1 =" + helt.getTemp(0, 6));
        System.out.println("GET TEMP 2.2 =" + helt.getTemp(1, 1));

        helt.calculateHealth();

        System.out.println("GET HEALTH 2.5 =" + helt.getHealth(2, 5));
        System.out.println("GET HEALTH 2.6 =" + helt.getHealth(2, 6));
        System.out.println("GET HEALTH 0.5 =" + helt.getHealth(2, 7));
        System.out.println("GET HEALTH 1.8 =" + helt.getHealth(2, 8));
int[][] heal = helt.getHealth();

int[] heal2 = this.convert(heal);

       // mite need this float[][] array = power.getThermal();
        //System.out.println(array);
power.updateHealth(heal2);
        power.updatePower(total.getTotalPower(), pue.getPUE());
pow.ChangeStatus(false);
    }


public int[] convert(int[][] i){
    int[] array = new int[40];
    int y = 0;
    for (int row = 0; row < 4; row++){
        for(int col = 0; col < 10; col++){
           array[y] = i[row][col];
           y++;
        }
    }
    return array;
}
}
/*
  Timer timer1 = new Timer();
        Timer timer2 = new Timer();
        Timer timer3 = new Timer();

        long delay1 = 3*1000;
        long delay2 = 12*1000;
        long delay3 = 15*1000;

        timer1.schedule(new checkStatus(), delay1);
        timer2.schedule(new checkStatus(), delay2);
        timer3.schedule(new updateTask(), delay3);
 * */
 