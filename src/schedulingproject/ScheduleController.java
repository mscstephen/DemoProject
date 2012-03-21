package schedulingproject;

import java.util.ArrayList;
import java.util.HashMap;


public class ScheduleController implements ScheduleCreatorInterface{
	
	private Schedule schedule;
	private boolean[][] healthMap;
	private double[][] thermalMap;
	private HashMap<Integer, Boolean> jobLookUp;
	private ArrayList<Job> unassignedJobs;
	private double rateCPU;
	
	public ScheduleController(Schedule schedule, boolean[][] healthMap, double[][] thermalMap, Double rateCPU){
		this.schedule = schedule;
		this.healthMap = healthMap;
		this.thermalMap = thermalMap;
		this.jobLookUp = new HashMap<Integer, Boolean>();
		this.unassignedJobs = new ArrayList<Job>();
		this.rateCPU = rateCPU;
	}
	
	public Schedule assignJobs(ArrayList<Job> jobs){
		
		ScheduleMaker firstThread = new ScheduleMaker(1);
		firstThread.setResources(this.schedule, this.healthMap, this.thermalMap, jobs, jobLookUp, 0.75);
		firstThread.run();
		
		ScheduleMaker secondThread = new ScheduleMaker(2);
		secondThread.setResources(this.schedule, this.healthMap, this.thermalMap, jobs, jobLookUp, 0.75);
		secondThread.run();
		
		//while(firstThread.isAlive() | secondThread.isAlive()){}
		
		ScheduleMaker thirdThread = new ScheduleMaker(3);
		
		if(!allJobsAreAssigned(jobs)){
			thirdThread.setResources(this.schedule, this.healthMap, this.thermalMap, unassignedJobs, jobLookUp, 0.75);
			thirdThread.run();
		}
		
		//while(thirdThread.isAlive()){}
		
		return this.schedule;
	}
	
	public boolean allJobsAreAssigned(ArrayList<Job> jobs){	
		
		boolean notAssigned = true;
		
		for(int k = 0; k < jobs.size(); k++){
			if(!jobLookUp.containsKey(jobs.get(k))){
				unassignedJobs.add(jobs.get(k));
				notAssigned = false;
			}
		}
		
		return notAssigned;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setHealthMap(boolean[][] healthMap) {
		this.healthMap = healthMap;
	}

	public boolean[][] getHealthMap() {
		return healthMap;
	}

	public void setThermalMap(double[][] thermalMap) {
		this.thermalMap = thermalMap;
	}

	public double[][] getThermalMap() {
		return thermalMap;
	}

        public void setRate(double rate) {
		this.rateCPU = rate;
        }

}
