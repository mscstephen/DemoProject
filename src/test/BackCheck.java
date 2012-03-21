/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import H.utility.NewHibernateUtil;
import h.entities.Teams;
import j.h.entities.BackThread;
import j.h.entities.RunThread;
import java.util.TimerTask;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author kac4
 */
public class BackCheck extends TimerTask{
    Session session;
    int team;
    RunThread d;
    Teams t;
    public BackCheck(int t , RunThread b ){
        team = t;
        d = b;
    }
    public void run(){
        session = NewHibernateUtil.getSessionFactory().openSession();
        Query q = session.createQuery("From Teams as t where t.teamId = "+ team);
        for (Object o : q.list()){
            t = (Teams)o;
            if (t.getActive() == 1){
                d.setActive("");
            }
        }
    }

}
