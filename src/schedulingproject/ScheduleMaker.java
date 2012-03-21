package schedulingproject;

import java.util.ArrayList;
import java.util.HashMap;


public class ScheduleMaker{// extends Thread{
	
	private Schedule schedule; 
	private boolean[][] healthMap;
	private double[][] thermalMap;
	private ArrayList<Job> jobs;
	private HashMap<Integer, Boolean> jobLookUp;
	private int id;
	private double rateCPU;

	public ScheduleMaker(int id){
		this.id = id;
		this.rateCPU = 0.5;
	}

	public void run() {
		
		for(int k = 0; k < jobs.size(); k++){
			
			if(!jobs.get(k).isLocked() && !jobLookUp.containsKey(jobs.get(k).getId())){	
				jobs.get(k).lockJob();
				assignAJobToCPU(k);
				jobs.get(k).unlockJob();
			}
		}
	}
	
	public void assignAJobToCPU(int k){
		
		for(int i = 0; i < 10; i++){
			
			for(int j = 0; j < 4; j++){
				
				if(!schedule.getCPUS()[j][i].isLocked() && healthMap[j][i] == false 
						&& !jobLookUp.containsKey(jobs.get(k).getId())){
					
					schedule.getCPUS()[j][i].lockCPU();
					double rate = jobWillFinish(schedule.getCPUS()[j][i], jobs.get(k));
					
					if(rate < rateCPU && safeThermal(schedule.getCPUS()[j][i], jobs.get(k), thermalMap[j][i])
							&& !jobLookUp.containsKey(jobs.get(k).getId())){
						
						jobs.get(k).setProcessingRate(rate);
						schedule.getCPUS()[j][i].addAJob(jobs.get(k));
						jobLookUp.put(jobs.get(k).getId(), true);
						schedule.getCPUS()[j][i].unlockCPU();
						jobs.get(k).unlockJob();
						
						
						return;
					}
				}
			}
		}
	}
	
	public boolean safeThermal(CPU cpu, Job job, double currentTemp){
		
		double expectedTemp = currentTemp;
		
		for(int i = 0; i < cpu.getJobs().size(); i++){
			expectedTemp = expectedTemp + (cpu.getJobs().get(i).getDuration() * cpu.getJobs().get(i).getProcessingRate());
		}
		
		if(expectedTemp < 75){
			return true;
		}
		
		else return true;
		
	}
	
	public double jobWillFinish(CPU cpu, Job job){
		
		double totalRuntime = 0;
		
		for(int i = 0; i < cpu.getJobs().size(); i++){
			totalRuntime = totalRuntime + (cpu.getJobs().get(i).getDuration() * (1.00 - cpu.getJobs().get(i).getProcessingRate()));
		}
		
		return 0.5;//((job.getDuration() * job.getPriority()) - totalRuntime) / job.getDuration();
	}
	
	public void setResources(Schedule schedule, boolean[][] healthMap, 
			double[][] thermalMap, ArrayList<Job> jobs, HashMap<Integer, Boolean> jobLookUp, double rateCPU){
		this.schedule = schedule; 
		this.healthMap = healthMap;
		this.thermalMap = thermalMap;
		this.jobs = jobs;
		this.jobLookUp = jobLookUp;
		this.rateCPU = rateCPU;
	}
	
}
