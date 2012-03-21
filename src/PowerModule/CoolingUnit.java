/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PowerModule;


/**
 *
 * @author rcg2
 */

public class CoolingUnit {



    private float outputTemp;
    NonComputing nc;
    private int fanRate;

    private int currentMode;

   

    public CoolingUnit (NonComputing nonComp) {
                nc = nonComp;
                this.changeMode(Plan.OFF);
	}


    public CoolingUnit (NonComputing nonComp, int newPlan) {
                nc = nonComp;
                this.changeMode(newPlan);

	}

	public void changeMode(int newMode) {
                
		switch(newMode) {
			case Plan.OFF:
				outputTemp = 0; fanRate = 0;currentMode = newMode;
				break;
			case Plan.LOW:
				outputTemp = (float) 14.0; fanRate = 70;currentMode = newMode;
				break;
			case Plan.MEDIUM:
				outputTemp = (float) 12.0; fanRate = 85;currentMode = newMode;
				break;
			case Plan.HIGH:
				outputTemp = (float) 10.0; fanRate = 100;currentMode = newMode;
				break;
			default:
				System.out.println("No known plan associated with that value. Setting the fans to HIGH mode.");
                                outputTemp = (float) 10.0; fanRate = 100;
				break;
		}
	}

	public float getPowerCost () {
		float totalCost = (float) 0.0;
                if (currentMode != Plan.OFF) {
                    totalCost = getTotalConversionCost() + getFanCost();

                }
		return totalCost;
	}

	private float getFanCost() {
		float fanCost;

		switch(fanRate) {
                        //LOW
			case 70:
				fanCost = (float) 1.5;
				break;
                        //MEDIUM
			case 85:
				fanCost = (float) 2.9;
				break;
                        //HIGH
			case 100:
				fanCost = (float) 4.4;
				break;
			// OFF
			default:
				return 0;
		}
                System.out.println(fanCost);
		return fanCost;

	}

	private float getTotalConversionCost() {
		float cost = (float) 0.0;
		if (getCurrentMode() != Plan.OFF) {
			float deltaTemp = nc.getIntakeTemp() - outputTemp;
			cost = deltaTemp * NonComputing.KW_PER_DEGREE_CONVERTED;
		}
                System.out.println(cost);
		return cost;
	}


    /**
     * @return the currentPlan
     */
    public int getCurrentMode() {
        return currentMode;
    }





}
