
package schedulingproject;

import java.util.ArrayList;
import java.io.*;


public class CPU implements Serializable
{

	private int id;
	private ArrayList<Job> jobs;
	private int processingPower;
	private boolean lock;

	public CPU(){
		this.id = 0;
		this.jobs = new ArrayList<Job>();
		this.processingPower = 0;
		lock = false;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setJobs(ArrayList<Job> jobs) {
		this.jobs = jobs;
	}

	public ArrayList<Job> getJobs() {
		return jobs;
	}

	public void addAJob(Job j){
		this.jobs.add(j);
		jobs.trimToSize();
	}

	public void setProcessingPower(int processingPower) {
		this.processingPower = processingPower;
	}

	public int getProcessingPower() {
		return processingPower;
	}

	public void lockCPU() {
		lock = true;
	}

	public boolean isLocked() {
		return lock;
	}

	public void unlockCPU() {
		lock = false;
	}

}
