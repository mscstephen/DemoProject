package schedulingproject;

import java.util.Date;
import java.io.*;



public class Job implements Serializable
{

	private int id;
	private Date timeStamp;
	//private Date timeToFinish;
	private int duration;
	private Double revenue;
	private Double processingRate;
	private int priority;
	private boolean lock;

	public Job(){
		this.id = 0;
		this.timeStamp = new Date();
		this.duration = 0;
		this.revenue = 0.00;
		this.processingRate = 0.00;
		this.priority = 0;
		lock = false;
	}
	public Job(int iD, Date ts, int dur, double rev, double pro, int pri)
	{
		id = iD;
		timeStamp = ts;
		duration = dur;
		revenue = rev;
		processingRate = pro;
		priority = pri;
		lock = false;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getDuration() {
		return duration;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	public Double getRevenue() {
		return revenue;
	}

	public void setProcessingRate(Double processingRate) {
		this.processingRate = processingRate;
	}

	public Double getProcessingRate() {
		return processingRate;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public void lockJob() {
		lock = true;
	}

	public boolean isLocked() {
		return lock;
	}

	public void unlockJob() {
		lock = false;
	}

	public String toString()//This will make testing easier
	{
		String str = "Job: " +id +" TS: " +timeStamp +" Duration: " +duration +" Revenue: " +revenue +" Rate: " +processingRate +" Priority: " +priority;
		return str;
	}

}
