package schedulingproject;

import java.util.ArrayList;
import java.util.Date;

public class ParserController
{
	private HealthMapParser hMParser = new HealthMapParser();
	private ThermalMapParser tMParser = new ThermalMapParser();
	private JobParser jParser = new JobParser();
	private ScheduleParser sp = new ScheduleParser();
	private RuleParser rp = new RuleParser();
	private JobCarrier jc = new JobCarrier();

	public ParserController()
	{
	}
	public boolean[][] parseHealthMap(boolean hm[][])
	{
		return (hMParser.parseHealthMap(hm));
	}
	public double[][] parseThermalMap(double tm[][])
	{
		return (tMParser.parseThermalMap(tm));
	}
	public boolean parseJob(Job j)
	{
		//System.out.println("This really shouldn't be used, as I've set the Schedule Parser to go over all jobs in all CPUs");
		return (jParser.parseJob(j, true));
	}
	public ArrayList<Job> ParseJobList(ArrayList<Job> j)
	{
		return (jParser.ParseJobList(j));
	}
	public boolean parseSchedule(Schedule s)
	{
		return (sp.parseSchedule(s));
	}
	public Schedule checkRepairSched(Schedule old)
	{
		return (sp.checkAndRepairSchedule(old));
	}
	public double parseRule(double d)
	{
		return (rp.parseRule(d));
	}
	public Schedule provideOldJobs(Schedule old)
	{
		Schedule s = jc.provideOldJobs(old);
		return s;
	}
	public ArrayList<Job> getDumpedJobs()
	{
		return (jc.getOldJobs());
	}
}
