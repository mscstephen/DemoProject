
package schedulingproject;
import java.util.ArrayList;
import java.util.Date;
import java.io.*;

public class Schedule implements Serializable
{

	private CPU[][] cpus;
	private Date timeStamp;
	private Double revenue;

	public Schedule()
	{
		this.cpus = new CPU[4][10];
		this.timeStamp = new Date();
		this.revenue = 0.00;
		//initialise the CPU array with working CPUs
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				this.cpus[i][j] = new CPU();
			}
		}

	}


	public void setCPUS(CPU[][] cpus) {
		this.cpus = cpus;
	}

	public CPU[][] getCPUS() {
		return cpus;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	public Double getRevenue() {
		return revenue;
	}

}
