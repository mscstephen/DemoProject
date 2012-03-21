/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package j.h.entities;

import H.utility.NewHibernateUtil;
import h.entities.Teams;
import java.util.Timer;
import java.util.TimerTask;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author kac4
 */
public class PowerTrigger extends TimerTask{
    long delay;
Teams t;
Timer time = new Timer();
boolean active = true;
Session session;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Teams getT() {
        return t;
    }

    public void setT(Teams t) {
        this.t = t;
    }

    public Timer getTime() {
        return time;
    }

    public void setTime(Timer time) {
        this.time = time;
    }

    public void run(){
        while(active){
        session = NewHibernateUtil.getSessionFactory().openSession();
        Query q = session.createQuery("From Teams as team where team.teamId = 3");
        for (Object o : q.list()){
            t = (Teams)o;
            if(t.getActive() == 1){
               this.setActive(false);
            }
        }
    }
    session.close();
    }

}
