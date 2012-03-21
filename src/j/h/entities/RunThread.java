package j.h.entities;

import H.utility.NewHibernateUtil;
import h.entities.Pmap2;
import h.entities.Thermalmap;
import java.math.BigDecimal;
import java.util.Timer;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import schedulingproject.SchedulingController;
import test.OptimisationRun1;
import test.StartTrigger;

/**
 *
 * @author kac4
 */
public class RunThread {
   
    long delay = 1*1000;
        String active = "";
       
        Timer time2 = new Timer();
         Timer time3 = new Timer();
           Timer time4 = new Timer();
        StartTrigger start = new StartTrigger(false);


    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }


    public void run(int i){
         start.cancel();
     
        while(start.isActive() == true){
            time2.schedule(start = new StartTrigger(false), delay);
            start.cancel();
        }
        BackThread bk = new BackThread();
        //Enter Schedule module call
         SchedulingController sch = new SchedulingController();
         OptimisationRun1 opt = new OptimisationRun1();
         sch.run();
         
        bk.start();
        opt.start();
         System.out.println("System start");
       
        CoolingThread col = new CoolingThread();
   
boolean run = false;
        while(bk.isActive()){
           
            if (bk.getPhase() == 0){
                bk.setActive(false);


                System.out.println("System finished");
                this.mergeThermal();
            }
       if(bk.getPhase() == 3 && run == false){
           col.start();
           run = true;
       }

    }
         opt.stop();
         col.stop();
                bk.stop();
        //Execute reset
    }
public void mergeThermal(){
    Session session = NewHibernateUtil.getSessionFactory().openSession();

    Query q = session.createQuery("From Pmap2");
    Pmap2 map1 = new Pmap2();
    Thermalmap tmap = new Thermalmap();
    for (Object o : q.list()){
         Transaction trans = session.beginTransaction();
        map1 = (Pmap2)o;
        tmap.setTmapId(map1.getPmap2id());
        tmap.setCpu(map1.getCpu());
        tmap.setCurrentTemp(map1.getCurrentTemp());
        tmap.setFutureTemp(map1.getFutureTemp());
        tmap.setRow(map1.getRow());
        tmap.setPlants(map1.getPlants());
        tmap.setTime(map1.getTime());
        session.merge(tmap);
        trans.commit();
    }
    session.close();
}
}
