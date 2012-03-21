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
public class updateTask extends TimerTask{
    Session session;
    boolean truth;
    int team;

    public updateTask(int t){
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
        trans.begin();
        Query q = session.createQuery("From Teams as team where team.teamId = " + team);
        for (Object o : q.list()){
            t = (Teams)o;
            if( t.getActive() == 1){
                t.setActive(0);
            }
 else{
     t.setActive(1);
            }
            session.merge(t);
            trans.commit();
        }
        session.close();
    }

}
