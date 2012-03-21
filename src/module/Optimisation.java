package module;

import h.entities.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Optimisation {


    /*** EPOCH values *******/
    private float timeMaximum;
    private int nbIterationMaximum;
    /************************/

    /** OUTPUTS : Result of the Algorithm **/
	private double opti_profit = 0;
	private List<h.entities.Pschedule> opti_schedule = null;
	private boolean profit_found = false;
	private boolean profit_optimized = false;
        private int best_schedule_number;

	private LinkedList<Double> profits = new LinkedList<Double>();
	private LinkedList<List<Pschedule>> schedules_analysed = new LinkedList<List<Pschedule>>();
	private LinkedList<BigDecimal> powers = new LinkedList<BigDecimal>();
	private int nbSchedulesAnalysed = 0;
	/******************************************/

	private BigDecimal unitPowerCost;
	private BigDecimal unitCpuReplacement;
	private double minimum_profit;

	public Optimisation(BigDecimal unitPowerCost, BigDecimal unitCpuReplacement, float timeMaximum,	int nbIterationMaximum,double minimum_profit) {
		super();
		this.unitPowerCost = unitPowerCost;
		this.unitCpuReplacement = unitCpuReplacement;
		this.timeMaximum = timeMaximum;
		this.nbIterationMaximum = nbIterationMaximum;
		this.minimum_profit = minimum_profit;
	}

	public int getNbSchedulesAnalysed() {
		return nbSchedulesAnalysed;
	}

	public void setNbSchedulesAnalysed(int nbSchedulesAnalysed) {
		this.nbSchedulesAnalysed = nbSchedulesAnalysed;
	}

	public float getTimeMaximum() {
		return timeMaximum;
	}

	public void setTimeMaximum(float timeMaximum) {
		this.timeMaximum = timeMaximum;
	}
	public int getNbIterationMaximum() {
		return nbIterationMaximum;
	}
	public void setNbIterationMaximum(int nbIterationMaximum) {
		this.nbIterationMaximum = nbIterationMaximum;
	}

	public boolean isProfit_found() {
		return profit_found;
	}

	public boolean isProfit_optimized() {
		return profit_optimized;
	}

	/** Function calculating the profit value**/
	public double Calculate_profit (List<h.entities.Pschedule> schedule, BigDecimal currentPowerUsedForCooling, BigDecimal unitCpuReplacement, BigDecimal unitPowerCost) {
            double profit;
	    double powerCost = currentPowerUsedForCooling.doubleValue() * unitPowerCost.doubleValue();
	    int nbDeadCpus = 0;
	    double revenue = 0;
	    for (Pschedule s : schedule){
	    	revenue = s.getTotalRevenue().doubleValue();
	    	if (s.getCpu().getHealth().getStatus()==1){
	    		nbDeadCpus ++;
	    	}
	    }
	    double maintenanceCost = nbDeadCpus * unitCpuReplacement.doubleValue();
            System.out.println("Revenu :" + revenue);
            System.out.println("maintenanceCost :" + maintenanceCost);
            System.out.println("powerCost :" + powerCost);
	    profit = (revenue - maintenanceCost - powerCost);
	    return profit;
	}

	public void getInputs () {

	}
	public Date runOptimization (String reportName) {

		long start = System.currentTimeMillis();

		List<h.entities.Pschedule> currentSchedule = null;
		BigDecimal currentPowerUsedForCooling;
		int nb_iterations = 0;
		int scheduleTeamId = 2;
		int powerTeamId = 4;
		Date last_schedule = null;

		while( nb_iterations<this.nbIterationMaximum && !this.profit_optimized ){

			boolean ready = false;
			while (!ready) {
				if ((System.currentTimeMillis()-start)/1000>=this.timeMaximum){
                                    long end = System.currentTimeMillis();
                                    long time = end - start;
                                    this.nbSchedulesAnalysed = nb_iterations;
                                    this.generateReport(reportName,time);
                                    Integration.setOptimisationActiveNumber(1);
                                    if (currentSchedule!= null){
                                        return currentSchedule.get(0).getTime();
                                    }
                                    else {
                                        return null;
                                    }                      
				}
				boolean new_schedule = false;
				if (nb_iterations==0 && Integration.getPscheduleRows().size() > 0){
					new_schedule = true;
				}
				else{
					List<Pschedule> schedule = Integration.getPscheduleRows();
					new_schedule = schedule.size() >= 0 && schedule.get(0).getTime().after(last_schedule);
				}
				ready = (Integration.getTeamsRow(scheduleTeamId).getActive() == 0) && (Integration.getTeamsRow(powerTeamId).getActive() == 0) && new_schedule;
			}
			Integration.setOptimisationActiveNumber(0);
			/***** Get all the inputs***************/
			currentSchedule = Integration.getPscheduleRows();
			this.schedules_analysed.add(currentSchedule);
			last_schedule = currentSchedule.get(0).getTime();
			currentPowerUsedForCooling = Integration.getTotalPower().getPue();
			this.powers.add(currentPowerUsedForCooling);
			nb_iterations ++;
			/*****************************************************/


			/** Calculation of the profit**/
			double profit = Calculate_profit(currentSchedule,currentPowerUsedForCooling,this.unitCpuReplacement, this.unitPowerCost);
			this.profits.add(profit);

			/** Comparison with the old value, if the value is better we set the new schedule opti_schedule and the new profit opti_profit**/
			if (profit > opti_profit){
				this.profit_found = true;
				this.opti_profit = profit;
				this.opti_schedule = currentSchedule;
			}

			/** Comparison with the minimum value MINIMUM_PROFIT_VALUE that defines if the algorithm is considered oprimized**/
			if (profit >= this.minimum_profit){
				this.profit_optimized = true;
				Integration.setScheduleRows(currentSchedule);
			}
			else {
				Integration.setOptimisationActiveNumber(2);
			}
		}
		long end = System.currentTimeMillis();
		long time = end - start;
		this.nbSchedulesAnalysed = nb_iterations;

		this.generateReport(reportName,time);
		Integration.setOptimisationActiveNumber(1);
                if (currentSchedule!= null){
                        return currentSchedule.get(0).getTime();
                }
                else {
                        return null;
                }

	}
	private void generateReport(String reportName,long time) {
		FileWriter outFile = null;
		try {
			outFile = new FileWriter(reportName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		PrintWriter out = new PrintWriter(outFile);
		String bigline = "*************************************************************************************************\n";
		String shortLine = "*******************************************\n";
		out.write(bigline);
		out.write(" GLOBAL REPORT OF THE SCHEDULE OPTIMIZATION\n");
		out.write(bigline);
		out.write("\n");
		out.write("\n");

		out.write(shortLine);
		out.write("Time of the optimization:\n");
		out.write(Float.valueOf(time/1000) + " sec\n");
		out.write(shortLine);
		out.write("\n");
		out.write("\n");

		out.write(shortLine);
		out.write("Number of generated and analysed schedules:\n");
		out.write(String.valueOf(this.nbSchedulesAnalysed) + "\n");
		out.write(shortLine);
		out.write("\n");
		out.write("\n");

		out.write(shortLine);
		out.write("Description of the schedules:\n");
		for (List<Pschedule> s : this.schedules_analysed){
			out.write("**\n");
			out.write("Schedule number: " + String.valueOf(this.schedules_analysed.indexOf(s)) + "\n");
			out.write("TimeStamp: " + String.valueOf(s.get(0).getTime()) + "\n");
			out.write("Revenue: " + String.valueOf(s.get(0).getTotalRevenue()) + "\n");

			out.write("CPUs: \n");
			for ( Pschedule row : s){
				Cpu cpu = row.getCpu();
                                List<Job> jobs = Integration.getJobRows(cpu.getCpuid());
                                if (jobs.size()>0){
                                    out.write("     id: " + String.valueOf(cpu.getCpuid()) + "\n");
                                    out.write("     Jobs: \n");

                                        for (Job job : jobs){
                                            out.write("        **\n");
                                            out.write("		id: " + String.valueOf(job.getJid()) + "\n");
                                            out.write("		duration: " + String.valueOf(job.getDuration()) + "\n");
                                            out.write("		priority: " + String.valueOf(job.getPriority()) + "\n");
                                            out.write("		revenue: " + String.valueOf(job.getRevenue()) + "\n");
                                            out.write("        **\n");
                                        }
                                }
			}
			out.write("**\n");
		}
		out.write(shortLine);
		out.write("\n");
		out.write("\n");

		out.write(shortLine);
		out.write("Result of the optimization:\n");
		out.write("Optimized schedule found ? (YES/NO): ");
		if (this.profit_optimized){
			out.write("YES\n");
		}
		else{
			out.write("NO\n");
			out.write("\n");
			out.write("Schedule with profit found ? (YES/NO): ");
			if (this.profit_found){
				out.write("YES\n");
			}
			else{
				out.write("NO\n");
			}
		}
		out.write(shortLine);
		out.write("\n");
		out.write("\n");

		if ((this.opti_schedule!=null)){
			out.write(shortLine);
			if (this.profit_optimized){
				out.write("Optimized schedule:\n");
			}
			else{
				out.write("Best schedule:\n");
			}
			List<Schedule> schedule = Integration.getScheduleRows();
			out.write("Schedule number: " + String.valueOf(this.schedules_analysed.indexOf(this.opti_schedule)) + "\n");
			out.write("TimeStamp: " + String.valueOf(schedule.get(0).getTime()) + "\n");
			out.write("Revenue: " + String.valueOf(schedule.get(0).getTotalRevenue()) + "\n");
			out.write("CPUs: \n");
			for ( Schedule row : schedule){
				Cpu cpu = row.getCpu();
                                List<Job> jobs = Integration.getJobRows(cpu.getCpuid());
                                if (jobs.size()>0){
                                    out.write("     id: " + String.valueOf(cpu.getCpuid()) + "\n");
                                    out.write("     Jobs: \n");

                                        for (Job job : jobs){
                                            out.write("        **\n");
                                            out.write("		id: " + String.valueOf(job.getJid()) + "\n");
                                            out.write("		duration: " + String.valueOf(job.getDuration()) + "\n");
                                            out.write("		priority: " + String.valueOf(job.getPriority()) + "\n");
                                            out.write("		revenue: " + String.valueOf(job.getRevenue()) + "\n");
                                            out.write("        **\n");
                                        }
                                }
                        }
			out.write("**\n");
			out.write("Profit: " + String.valueOf(this.opti_profit) + "\n");
			out.write(shortLine);
			out.write("\n");
			out.write("\n");

			out.write(shortLine);
			out.write("Cpu Health Model:\n");
			out.write("\n");
                        boolean noDead = true;
			for ( Schedule row : schedule){
				Cpu cpu = row.getCpu();
				if (cpu.getHealth().getStatus() == 1) {
                                        if (noDead) {
                                           out.write("CPUs that will need to be replaced: \n");
                                        }
                                        noDead = false;
					out.write("id: " + String.valueOf(cpu.getCpuid()) + "\n");
					out.write("Pmap1s: " + String.valueOf(cpu.getPmap1s()) + "\n");
					out.write("Pmap2s: " + String.valueOf(cpu.getPmap2s()) + "\n");
				}
			}
                        if (noDead) {
                                out.write("No CPUs will need to be replaced: \n");
                        }
			out.write("\n");
			out.write("\n");

		}
		out.close();
	}
}



    