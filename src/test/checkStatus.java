/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import H.utility.NewHibernateUtil;
import h.entities.Teams;
import java.util.TimerTask;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author kac4
 */
public class checkStatus extends TimerTask {
    Session session;
    boolean truth;
    int team;
    int team2;

    public checkStatus(int t , int t2){
        team2 = t2;
        team = t;
    }

    public boolean isTruth() {
        return truth;
    }

    public void setTruth(boolean truth) {
        this.truth = truth;
    }

    public void run(){
        Teams t;
        session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction trans = session.getTransaction();
        Query q = session.createQuery("From Teams as team where team.teamId = " + team);
        for (Object o : q.list()){
            t = (Teams)o;
            Query q2 = session.createQuery("From Teams as team where team.teamId = " + team2);
           Object o2 = q.list().get(0);
           Teams tm = (Teams)o2;
  if(tm.getActive() == 1){
            if(t.getActive() == 1){
                truth = false;
                TimerTask n = new updateTask(team2);
                }
 else{
                truth = true;
 }
            }
    }
        printMS(this);
session.close();
}

     public static void printMS(checkStatus c){
        if (c.isTruth() == true){
            System.out.println("Status is unactive\n");
        }
 else{
            System.out.println("Status is active\n");
 }
    }

}
