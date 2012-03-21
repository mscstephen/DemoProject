/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PowerModule;

/**
 *
 * @author rcg2
 */
public class NonComputing {

    private float intakeTemp;



    private int[] currentPlan;

    private String PLAN_FALLBACK_TEXT = " Reverting to HIGH mode.";
    private static final String TEMP_FALLBACK_TEXT = " Reverting to fallback temperature (70 C)";

    public static final float KW_PER_DEGREE_CONVERTED = (float) 1.00;
    private static final int ROWS_NUM = 4;

    private static final float LOWER_TEMP_BOUNDARY = (float)0.0;
    private static final float UPPER_TEMP_BOUNDARY = (float)70.0;


    private CoolingUnit[]coolerArray = new CoolingUnit[ROWS_NUM];

    public NonComputing() {


        intakeTemp = (float) 0.0; // fallback. intakeTemp is initially set to zero

        // initialise the cooling units for each row
        for (int i = 0; i < coolerArray.length; i++) {
            coolerArray[i] = new CoolingUnit(this);

        }
    }



    public NonComputing(int initialPlan) {
        intakeTemp = (float) 0.0; // fallback. intakeTemp is initially set to zero.

        // initialise the cooling units for each row
        for (int i = 0; i < coolerArray.length; i++) {
            coolerArray[i] = new CoolingUnit(this);

        }
    }

    private void setCurrentPlan(int[] newPlan) throws ArraySizeException, NumberRangeException{

        if (newPlan.length == ROWS_NUM) {
            for (int i = 0; i < ROWS_NUM; i++) {
                //if the newPlan array does not contain values between 0 - 3 throw an exception
                if ((newPlan[i] < Plan.OFF) || (newPlan[i] > Plan.HIGH)) throw new NumberRangeException( );

            }
        }
        //array is the wrong size. throw an exception.
        else {
            throw new ArraySizeException();

        }


    }
    public void changePlan(int[] newCoolingSchedule) {
        try {
            setCurrentPlan(newCoolingSchedule);
            for (int i = 0; i < coolerArray.length; i++) {
                coolerArray[i].changeMode(newCoolingSchedule[i]);

            }

        }
        catch (Exception e) {
            // if there is a problem, revert to HIGH mode.
            System.out.println("" + e + PLAN_FALLBACK_TEXT);
            for (int i = 0; i < coolerArray.length; i++) {
                coolerArray[i].changeMode(Plan.HIGH);

            }
        }
    }

    public float getTotalCost() {
        float totalCost = (float) 0.0;
        // loop through cooler Units and get the power cost for each
        for (int i = 0; i < coolerArray.length; i++) {

            totalCost = totalCost + coolerArray[i].getPowerCost();

        }
        return totalCost;
    }

    public float getIntakeTemp() {
        return intakeTemp;
    }

    public void setIntakeTemp(float intakeTemp) {
        try {
            if (intakeTemp > LOWER_TEMP_BOUNDARY && intakeTemp < UPPER_TEMP_BOUNDARY) {
                this.intakeTemp = intakeTemp;

            } else {
                throw new TempRangeException();
            }
        }
        catch (Exception e) {
            // assume the room is extremely hot
            this.intakeTemp = UPPER_TEMP_BOUNDARY;
           // System.out.println("" + e + TEMP_FALLBACK_TEXT);
            e.printStackTrace();
        }
    }

    public int[] getCurrentPlan() {
        return currentPlan;
    }




}