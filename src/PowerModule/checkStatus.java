/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PowerModule;

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
        Query q = session.createQuery("From Teams as team where team.teamId = 3");
        for (Object o : q.list()){
            t = (Teams)o;
            if(t.getActive() == 1){
                truth = false;
                }
 else{
                truth = true;
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
