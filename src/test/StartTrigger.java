/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import H.utility.NewHibernateUtil;
import h.entities.Job;
import java.util.TimerTask;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author kac4
 */
public class StartTrigger extends TimerTask{
    Session session;
    boolean active;
    public StartTrigger(boolean act){
        active = act;
    }

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
    public void run(){
        Job j = new Job();
        session  = NewHibernateUtil.getSessionFactory().openSession();
        Query q = session.createQuery("From Job as job where job.active = 0");
        if (q.list().isEmpty() == false){
            active = true;
        }
 else
     System.out.println("Nothing to process");

        session.close();
    }

}
