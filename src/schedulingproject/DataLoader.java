/*
 * Change Log
 * =============================================================================
 * Time:	14th/Mar.
 * Changes:	Added try catch statement for hibernate session in case the
 *                  connection failed.
 *              Only need to load jobs, healthmap, thermalmap and previous
 *                  schedule if the action == 1. If it's a re-schedule, then
 *                  just load rule is enough.
 * =============================================================================
 * Time:	05th/Mar.
 * Changes:	Modified loadHealthMap(). HID 1 means CPU alive and the boolean
 *                  should be false. HID 2 means CPU dead and the boolean should
 *                  be true.
 * =============================================================================
 * Time:	01st/Mar.
 * Changes:	Load and set the processing rate for each Job after the DB
 *                  has been updated yesterday.
 * =============================================================================
 * Time:	29th/Feb.
 * Changes:	Modified the loadSchedule() method after the DB has been
 *                  modified today. Now it reads the schedule and reads all the
 *                  jobs that is active and the CPU id is the same as the one
 *                  from the schedule, and add each of these jobs to the CPU.
 * =============================================================================
 * Time:	28th/Feb.
 * Changes:	Start the implementation.
 *              Implemented loadData(), loadRule(), loadJobs(), loadHealthMap(),
 *                  loadThermalMap(), loadSchedule().
 * =============================================================================
 */
package schedulingproject;

import H.utility.NewHibernateUtil;
import h.entities.Cpu;
import h.entities.Rules;
import h.entities.Thermalmap;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author hg1
 */
public class DataLoader {

    private Session session;
    private Rules rules;
    private ArrayList<Job> jobs;
    private boolean[][] healthMap;
    private double[][] thermalMap;
    private int rule;
    private Schedule schedule;

//    public static void main(String[] args) {
//        loadData();
//        session = NewHibernateUtil.getSessionFactory().openSession();
//        Transaction trans = session.beginTransaction();
//        Query q = session.createQuery("From Cpu as rule where rule.cpuid = 1");
//        Query q2 = session.createQuery("From Row as rule where rule.rowId = 1");
//        Object o = (Object) q.list().get(0);
//        test = (Cpu) o;
//        Object t = (Object) q2.list().get(0);
//        test2 = (Row) t;
//        cus = new CpuCustom(test, test2);
//        System.out.println(cus.getX());
//    }
    public void loadData(int action) {
        System.out.println("Scheduling Team - reading DB START");
        session = NewHibernateUtil.getSessionFactory().openSession();

        try {
            loadRule();
            if (action == 1) {
                loadJobs();
                loadHealthMap();
                loadThermalMap();
                loadSchedule();
            }
        } catch (Exception e) {
            System.out.println("Hibernate session exception caught: " + e.toString());
//            throw e;
        } finally {
            session.close();
        }

        System.out.println("Scheduling Team - reading DB END");
    }

    /*
     * this metod creates a Hibernate query, then reads the rules table and
     * store the rule into an int variable for later use.
     */
    public void loadRule() {
        // getting the rule
        System.out.println("    reading Rule START");
        Query q = session.createQuery("From Rules");
        Object o = (Object) q.list().get(0);
        rules = (Rules) o;
        rule = (int) rules.getRid();
        System.out.println("    reading Rule END");
    }

    /*
     * this metod creates a Hibernate query, then reads from the job table to
     * get all jobs that are still active, convert them into local job objects
     * and adds them one by one into an arraylist for later use.
     */
    public void loadJobs() {
        // TODO need to change since the DB has changed
        // getting all the jobs
        System.out.println("    reading Jobs START");
        jobs = new ArrayList<Job>();
        Query q = session.createQuery("From Job as job where job.active = 0");
        for (Object o : q.list()) {
            h.entities.Job tmpJob = (h.entities.Job) o;

            int jobId = tmpJob.getJid();
            Date timeStamp = tmpJob.getTime();
            int duration = tmpJob.getDuration().intValue();
            Double revenue = tmpJob.getRevenue().doubleValue();
            int priority = tmpJob.getPriority();
            Double processingRate = tmpJob.getProcessingrate().doubleValue();

            Job job = new Job();
            job.setId(jobId);
            job.setTimeStamp(timeStamp);
            job.setDuration(duration);
            job.setRevenue(revenue);
            job.setPriority(priority);
            job.setProcessingRate(processingRate);
            jobs.add(job);
        }
        System.out.println("    reading Jobs END");
    }

    /*
     * this metod creates a Hibernate query, then reads from the cpu table to
     * create 40 cpu objects. for each cpu, it checks the HID and set the health
     * condition to an 2d array of boolean values.
     */
    public void loadHealthMap() {
        // for the healthmap, hid 1 is alive, 2 is dead.
        // getting the health map
        System.out.println("    reading HealthMap START");
        healthMap = new boolean[4][10];
        Query q = session.createQuery("From Cpu");
        int row = 1;
        int col = 1;
        for (Object o : q.list()) {
            Cpu cpu = (Cpu) o;
            // get the cpu health
            int health = cpu.getHealth().getHid();
            if (health == 1) {
                healthMap[col - 1][row - 1] = false;
            } else if (health == 2) {
                healthMap[col - 1][row - 1] = true;
            }
//            System.out.println("cpu: "+cpu.getCpuid()+" "+healthMap[col - 1][row - 1]+" "+health);
            // change counters for rows and columns
            if (row >= 10) {
                row = 1;
                col++;
            } else {
                row++;
            }
        }
        System.out.println("    reading HealthMap END");
    }

    /*
     * this metod creates a Hibernate query, then reads from the thermalmap
     * table. for each of them, it reads the currenttemp field for the current
     * temperature as a double value and stores them into a 2d array of double
     * values.
     */
    public void loadThermalMap() {
        // getting the thermal map
        System.out.println("    reading ThermalMap START");
        thermalMap = new double[4][10];
        Query q = session.createQuery("From Thermalmap");
        int row = 1;
        int col = 1;
        for (Object o : q.list()) {
            Thermalmap thermal = (Thermalmap) o;
            double currTemp = thermal.getCurrentTemp().doubleValue();
            thermalMap[col - 1][row - 1] = currTemp;
            // change counters for rows and columns
            if (row >= 10) {
                row = 1;
                col++;
            } else {
                row++;
            }
        }
        System.out.println("    reading ThermalMap END");
    }

    /*
     * this metod creates a Hibernate query, then reads from the schedule table.
     * for each row of the schedule table, it creates a cpu object, then it
     * creates another Hibernate query and reads from the job table to get all
     * jobs that still active and the assigned cpuid is the same as the one
     * loaded previously from the schedule table. for each cpu, it set these
     * jobs to that cpu as an arraylist, also set the cpuid and processingpower.
     * after all, set all these cpus as a 2d array to the schedule.
     */
    public void loadSchedule() {
        // TODO need to change since the DB has changed
        // getting the thermal map
        System.out.println("    reading Schedule START");
        schedule = new Schedule();
        CPU[][] cpus = new CPU[4][10];
        int row = 1;
        int col = 1;
        Query q = session.createQuery("From Schedule");
        for (Object o : q.list()) {
            h.entities.Schedule tmpSchedule = (h.entities.Schedule) o;
            Cpu tmpCpu = tmpSchedule.getCpu();

            int cpuId = tmpCpu.getCpuid();

//            System.out.println("++++++++++For CPU: " + cpuId);
            ArrayList<Job> tmpJobs = new ArrayList<Job>();
            Query q1 = session.createQuery("From Job as job where job.cpuid = :cpuId and job.active = 0");
            q1.setParameter("cpuId", cpuId);
            for (Object o1 : q1.list()) {
                h.entities.Job tmpJob = (h.entities.Job) o1;

                int jobId = tmpJob.getJid();
                Date timeStamp = tmpJob.getTime();
                int duration = tmpJob.getDuration().intValue();
                Double revenue = tmpJob.getRevenue().doubleValue();
                int priority = tmpJob.getPriority();
                double processingRate = tmpJob.getProcessingrate().doubleValue();
                int active = tmpJob.getActive().intValue();
//                System.out.println("cpuId: " + tmpJob.getCpuid());
//                System.out.println("jobId: " + jobId);
//                System.out.println("timeStamp: " + timeStamp);
//                System.out.println("duration: " + duration);
//                System.out.println("revenue: " + revenue);
//                System.out.println("priority: " + priority);
//                System.out.println("processingRate: " + processingRate);
//                System.out.println("active: " + active);
                Job job = new Job();
                job.setId(jobId);
                job.setTimeStamp(timeStamp);
                job.setDuration(duration);
                job.setRevenue(revenue);
                job.setPriority(priority);
                job.setProcessingRate(processingRate);

                tmpJobs.add(job);
            }

            CPU cpu = new CPU();
            int pPower = tmpCpu.getProcessingrate().getProcessingPower().intValue();
            cpu.setId(cpuId);
            cpu.setProcessingPower(pPower);
            cpu.setJobs(tmpJobs);
            cpus[col - 1][row - 1] = cpu;

            // change counters for rows and columns
            if (row >= 10) {
                row = 1;
                col++;
            } else {
                row++;
            }
        }
        schedule.setCPUS(cpus);
        System.out.println("    reading Schedule END");
    }

    /**
     * @return the session
     */
    public Session getSession() {
        return session;
    }

    /**
     * @return the rules
     */
    public Rules getRules() {
        return rules;
    }

    /**
     * @return the jobs
     */
    public ArrayList<Job> getJobs() {
        return jobs;
    }

    /**
     * @return the healthMap
     */
    public boolean[][] getHealthMap() {
        return healthMap;
    }

    /**
     * @return the thermalMap
     */
    public double[][] getThermalMap() {
        return thermalMap;
    }

    /**
     * @return the rule
     */
    public int getRule() {
        return rule;
    }

    /**
     * @return the schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }
}
