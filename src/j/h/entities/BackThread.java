/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package j.h.entities;

import H.utility.NewHibernateUtil;
import PowerModule.Main;
import java.util.Timer;
import org.hibernate.Query;
import org.hibernate.Session;
import pure_thermal.StructuredRun;
import schedulingproject.SchedulingController;

/**
 *
 * @author kac4
 */
public class BackThread extends Thread {
Timer t = new Timer();
Timer t2 = new Timer();
Session session;
int phase;
long delay = 2*1000;
boolean active = true;
boolean run = false;
 StructuredRun sr = new StructuredRun();
    Main pow = new Main();
     SchedulingController sch = new SchedulingController();

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Timer getT2() {
        return t2;
    }

    public void setT2(Timer t2) {
        this.t2 = t2;
    }


    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public Timer getT() {
        return t;
    }

    public void setT(Timer t) {
        this.t = t;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }
@Override
public void run(){
   
int ph = 1;
int count = 0;
 this.setPhase(ph);
 session = NewHibernateUtil.getSessionFactory().openSession();
    while(active){
 count++;
   
 Query q = session.createQuery("From Teams as team where team.teamId = 2 and team.active = 1");

 // PHASE 1
if (ph == 1){
   
    System.out.println("Phase 1 :\n");
    while(!q.list().isEmpty()){
       
       
        
        sr.main();
         System.out.println("Phase 1 query run :\n");
        session = NewHibernateUtil.getSessionFactory().openSession();
    q = session.createQuery("From Teams as team where team.teamId = 3 and team.active = 0");
    }
      ph++;
}
  
        this.setPhase(ph);
        
//PHASE 2
   q = session.createQuery("From Teams as team where team.teamId = 3 and team.active = 1");
if ( ph == 2){
    System.out.println("Phase 2 :\n");
     while(!q.list().isEmpty()){
          System.out.println("Phase 2 query run :\n");
         
          pow.main();
  q = session.createQuery("From Teams as team where team.teamId = 4 and team.active = 0");
   
    }
 ph++;
}
 
      this.setPhase(ph);
      
  //PHASE 3

   q = session.createQuery("From Teams as team where team.teamId = 4 and team.active = 1");
  if(ph == 3 ){
      System.out.println("Phase 3 :\n");
       while(!q.list().isEmpty()){
            System.out.println("Phase 3 query run :\n");
        
   q = session.createQuery("From Teams as team where team.teamId = 1 and team.active = 0");
    }
      ph++;
  }

    this.setPhase(ph);
   
//PHASE 4
    q = session.createQuery("From Teams as team where team.teamId = 1 and team.active = 1");
   Query q2 = session.createQuery("From Teams as team where team.teamId = 1 and team.active = 2");
    if(ph == 4 ){
         System.out.println("Phase 4 :\n");
        while(q.list().isEmpty()){
            if(!q2.list().isEmpty()){
                ph = 1;
                // schdule object
               
                sch.run();
                this.setPhase(ph);
                break;
            }
            q = session.createQuery("From Teams as team where team.teamId = 1 and team.active = 1");
           q2 = session.createQuery("From Teams as team where team.teamId = 1 and team.active = 2");
            System.out.println("Phase 4 query run :\n");
        }

    }
 if(ph == 4){
     System.out.println("Finished cycle");
     ph = 0;
     this.setPhase(ph);
 }
 
if (count == 6){
    System.out.println("System Termination iteration maxed out");
    ph = 0;
    this.setPhase(ph);
}
    
    }
    }
}

