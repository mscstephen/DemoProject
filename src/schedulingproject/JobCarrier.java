package schedulingproject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class JobCarrier
{
	private ArrayList<Job> oldJobs;

	public JobCarrier(){}

	public Schedule provideOldJobs(Schedule old)
	{
		/*At this time, Job Carrier simply returns an empty Schedule.*/
		oldJobs = new ArrayList<Job>();
		Calendar now;
		Schedule newSched = new Schedule();
		//Schedule oldSched = new Schedule();
		CPU newjobs[][] = new CPU[4][10];
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				newjobs[i][j] = new CPU();
			}
		}
		//CPU remjobs[][] = new CPU[4][10];

		CPU oldjobs[][] = old.getCPUS();
		for (int i=0; i < oldjobs.length; i++)
		{
			for (int j=0; j < oldjobs[i].length; j++)
			{
				ArrayList<Job> jobs = oldjobs[i][j].getJobs();
				for (Job job : jobs)
				{
					now = Calendar.getInstance();
					// ttf to be calculated.
					//Date ttf = null;


					if (true)	  //If time to finish is known
					{			  //
						if(false) //Eventually this will check if the Job is past its deadline.
						{
							newjobs[i][j].addAJob(job);
							//System.out.println("Incomplete job found in old schedule, carried over to new schedule");
						}
						else
						{
							oldJobs.add(job);
						}
					}
				}
			}
		}
		//oldSched.setCPUS(remjobs);
		newSched.setCPUS(newjobs);
		//Schedule schedArray[] = {newSched, oldSched};

		return newSched;
	}

	public ArrayList<Job> getOldJobs()
	{
		return oldJobs;
	}
}