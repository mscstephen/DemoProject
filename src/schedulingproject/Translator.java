package schedulingproject;

import H.utility.NewHibernateUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Date;

public class Translator {

    private Schedule schedule;

    private ArrayList<Job> DJobs;


   
    public Translator() {
      
    }



    //TODO Translate schedule save data into database
    public void translate() {

      
        // TODO Auto-generated method stub
        System.out.println("Scheduling Team - committing changes to DB START");

        Session session = NewHibernateUtil.getSessionFactory().openSession();

        
        CPU[][] cpus = schedule.getCPUS();
        Query q = session.createQuery("From Pschedule");
        for (Object o : q.list()) {
            h.entities.Pschedule tmpSchedule = (h.entities.Pschedule) o;

            tmpSchedule.setTotalRevenue(new BigDecimal(schedule.getRevenue()));
            tmpSchedule.setTime(new Date(schedule.getTimeStamp().getTime()));
            session.merge(tmpSchedule);
        }

        for (int i = 0; i < cpus.length; i++) {
            for (int j = 0; j < cpus[0].length; j++) {
                ArrayList<Job> jobs = cpus[i][j].getJobs();
                for (Job job : jobs) {
                    int jobId = job.getId();
                    Query q1 = session.createQuery("From Job as job where job.jid = :jobId and job.active = 0");
                    q1.setParameter("jobId", jobId);
                    Object o1 = (Object) q1.list().get(0);
                    h.entities.Job tmpJob = (h.entities.Job) o1;
                    int cpuId = i * 10 + (j + 1);
                    tmpJob.setCpuid(cpuId);
                    tmpJob.setProcessingrate(new BigDecimal(job.getProcessingRate()));
                    session.merge(tmpJob);
                }
            }
        }

        Transaction tran = session.beginTransaction();
        tran.commit();
        session.close();
        System.out.println("Scheduling Team - committing changes to DB END");
    }


    //TODO get dumpjobs and change its active
    public void ActiveChange() {
        ArrayList<Job> jobs = DJobs;
        System.out.println(jobs);



        if (jobs.isEmpty()){
            System.out.println("There is no DumpJobs!");
        }
        else{
        //  TODO Auto-generated method stub
            System.out.println("Scheduling Team - committing changes to DB START");
            Session session = NewHibernateUtil.getSessionFactory().openSession();


                for (Job job : jobs) {
                    int jobId = job.getId();
                    Query q1 = session.createQuery("From Job as job where job.jid = :jobId and job.active = 0");
                    q1.setParameter("jobId", jobId);
                    Object o1 = (Object) q1.list().get(0);
                    h.entities.Job tmpJob = (h.entities.Job) o1;

                    tmpJob.setActive(1);

                    session.merge(tmpJob);
                }



            Transaction tran = session.beginTransaction();
            tran.commit();
            session.close();
            System.out.println("Scheduling Team - committing changes to DB END");
        }
    }

    /**
     * @return the schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }
    /**
     * @param schedule the schedule to set
     */
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }



    public ArrayList<Job> getdumpJobs() {
        return DJobs;
    }

    public void setdumpJobs(ArrayList<Job> DJobs) {
        this.DJobs = DJobs;
        
 
    }

}
