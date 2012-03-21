package schedulingproject;

import java.util.ArrayList;
import java.util.Date;

public class JobParser
{
	public JobParser(){}

	public boolean parseJob(Job j, boolean isNewJob)//checks to make sure an incoming Job is not null.
	//Will also do some other checking
	//module will return false if an error is found.
	{
		if (j == null)
		{
			System.out.println("j is null");
			return false;
		}
		if (j.getId() < 0)
		{
			System.out.println("j less than 0");
			return false;
		}

		if (j.getTimeStamp() == null)
		{
			System.out.println("TimeStamp null");
			return false;
		}
		if (j.getDuration() == 0 || (j.getDuration() < 0))
		{
			System.out.println("Duration 0 or less than 0");
			return false;
		}
		if ((j.getRevenue() == 0) || (j.getRevenue() < 0))
		{
			System.out.println("Revenue 0 or less than 0");
			return false;
		}
		if ((isNewJob = false) && ((j.getProcessingRate() == 0) || (j.getProcessingRate() < 0)))
		{
			System.out.println("Processing rate 0 or less than 0");
			return false;
		}

		if (j.getPriority() < 1)
		{
			System.out.println("Priority less than 1");
			return false;
		}
		return true;
	}
	public ArrayList<Job> ParseJobList(ArrayList<Job> j)
	{
		ArrayList<Job> ret = new ArrayList<Job>();
		for (Job temp: j)
		{
			boolean b = parseJob(temp, true);
			//System.out.println(temp);
			if (b == true)
			{
				ret.add(temp);
			}
			else
			{
				System.out.println("Error in a job, value found to be null , 0 or less than 0. Job disregarded");
			}
		}
		ret.trimToSize();
		return ret;
	}
}