package module;

import java.math.BigDecimal;

import h.entities.*;
import java.util.Date;

public class Run {

	/**
	 * @param args
	 */
	public void main(String[] args) {

                // Set Optimisation Team status to ACTIVE
                Integration.setOptimisationActiveNumber(Integration.ACTIVE);

		// TODO Auto-generated method stub
		Costplan costPlan = Integration.getCostPlan();
		BigDecimal unitPowerCost = costPlan.getTpowerCost();
		BigDecimal unitCpuReplacement = costPlan.getDamageCost();

		float timeMaximum = (float) 10; /* Great value to make sure we generate all the schedules */
		int nbIterationMaximum = 10;/* Great value to make sure we generate all the schedules */

		Optimisation opt;
		int nb_runs = 0;

		int scheduleTeamId = 2;
		int powerTeamId = 4;

		double minimum_profit = 5;

                Date last_schedule = null;


		while(true){
			boolean start = false;
			while (!start) {
                                boolean new_schedule = true;
                                if (last_schedule != null && Integration.getPscheduleRows().size() > 0){
                                        new_schedule = Integration.getPscheduleRows().get(0).getTime().after(last_schedule);
                                }
				start = (Integration.getTeamsRow(scheduleTeamId).getActive() == 1) && (Integration.getTeamsRow(powerTeamId).getActive() == 1) && (Integration.getPscheduleRows().size() > 0) && new_schedule;
			}
                        nb_runs += 1;
                        System.out.println(" start Optimisation number : " + nb_runs);
			opt = new Optimisation(unitPowerCost, unitCpuReplacement, timeMaximum, nbIterationMaximum,minimum_profit);
			last_schedule = opt.runOptimization("Report_" + String.valueOf(nb_runs));
			Integration.setOptimisationActiveNumber(opt.isProfit_optimized() ? Integration.INACTIVE : Integration.WAITING);
		}




	}

}
