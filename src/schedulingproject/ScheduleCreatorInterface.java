package schedulingproject;

import java.util.ArrayList;


public interface ScheduleCreatorInterface {
	
	public Schedule assignJobs(ArrayList<Job> jobs);
	
	public boolean allJobsAreAssigned(ArrayList<Job> jobs);

}
