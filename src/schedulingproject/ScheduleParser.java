package schedulingproject;

import java.util.ArrayList;

public class ScheduleParser
{
	public ScheduleParser()
	{
	}
	JobParser jParse;

	//Perform some basic checking to ensure that the prime CPU array
	//is not null and that it has members, both should be true.
	public boolean parseSchedule(Schedule s)
	{
		jParse = new JobParser();
		//if anything is Null, return false
		if ((s.getCPUS() == null) || (s.getCPUS().length == 0))
		{
			return false;
		}
		//otherwise run the job parser on all jobs of all CPUs
		// in the schedule.
		int i1 = 0;
		CPU c[][] = s.getCPUS();
		if (c.length != 4) return false;
		while (i1 < 4) // EDITED by DAN
		{
			CPU temp[] = c[i1];
			if (temp.length != 10) return false;
			int i = 0;
			while (i < temp.length)
			{
				//Check the jobs list for errors.
				int j = 0;
				ArrayList<Job> aj = temp[i].getJobs();
				while (j < aj.size())
				{
					if (jParse.parseJob(aj.get(j), false) == false) return false;
					j++;
				}

				i++;
			}
			i1++;
		}

		return true;
	}

	public Schedule checkAndRepairSchedule(Schedule old)
	{
		if (parseSchedule(old))
		{
			return old;
		}
		else
		{
			//Repair the schedule
			jParse = new JobParser();
			CPU oldcpus[][] = repairSize(old.getCPUS());//resize the old schedules CPU[][] array, if necessary
			Schedule NEW = new Schedule();
			CPU newcpus[][] = NEW.getCPUS();


			//Check jobs in the given CPU array and return them if they're good.
			for (int i=0; i<oldcpus.length; i++)
			{
				for (int j=0; j<oldcpus[i].length; j++)
				{
					for (Job job: oldcpus[i][j].getJobs())
					{
						if (jParse.parseJob(job, false))
						{
							newcpus[i][j].addAJob(job);
						}
						else
						{
							System.out.println("Schedule repair mechanism found an erroneous job");
						}
					}
				}
			}

			NEW.setCPUS(newcpus);
			return NEW;
		}
	}

	private CPU[][] repairSize(CPU ca[][])
	{
		//code here
		//if the outer array is too short
		if (ca.length < 4)
		{
			CPU temp[][] = new CPU[4][10];
			for (int i = 0; i<4; i++)
			{
				if (i < ca.length)
				{
					temp[i] = ca[i];
				}
				else
				{
					temp[i] = new CPU[10];
					for (int j=0 ; j <10 ; j++)
					{
						temp[i][j] = new CPU(); //initalise new array of CPUs.
					}
				}
			}
			ca = temp;
		}
		//if the outer array is too large
		if (ca.length > 4)
		{
			CPU temp[][] = new CPU[4][10];
			for (int i = 0; i < 4; i++)
			{
				temp[i] = ca[i];
			}
			ca = temp;
		}

		//now we check the inner arrays

		for (int i=0; i<ca.length; i++)
		{
			//Starting with checking for arrays too short.
			if (ca[i].length < 10)
			{
				CPU temps[] = new CPU[10];
				for (int j=0; j<10; j++)
				{
					if (j < ca.length)
					{
						temps[j] = ca[i][j];
					}
					else
					{
						temps[j] = new CPU();
					}
				}
				ca[i] = temps;
			}

			//now check for an inner array too long, and repair it if necessary.
			if (ca[i].length > 10)
			{
				CPU temps[] = new CPU[10];
				for (int j = 0; j < 10; j++)
				{
					temps[j] = ca[i][j];
				}
				ca[i] = temps;
			}
		}
		//Finally, we will check to make sure there are no Null CPUs
		//If there are, we must initialise them
		for (int i = 0; i<ca.length; i++)
		{
			for (int j = 0; j < ca[i].length; j++)
			{
				if (ca[i][j] == null)
				{
					ca[i][j] = new CPU();
				}
			}
		}
		return ca;
	}
}