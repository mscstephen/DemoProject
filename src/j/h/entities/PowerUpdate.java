/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package j.h.entities;

import H.utility.NewHibernateUtil;
import h.entities.Teams;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author kac4
 */
public class PowerUpdate  {
    Session session;

    public void ChangeStatus(boolean b){
        Teams t;
    if (b){
        session = NewHibernateUtil.getSessionFactory().openSession();
        Query q = session.createQuery("From Teams as team where team.teamId = 4");
        Transaction trans = session.beginTransaction();
        for(Object o : q.list()){
            t = (Teams)o;
            t.setActive(0);
            session.merge(t);
            trans.commit();
        }
        session.close();
    }
 else{
            session = NewHibernateUtil.getSessionFactory().openSession();
        Query q = session.createQuery("From Teams as team where team.teamId = 4");
        Transaction trans = session.beginTransaction();
        for(Object o : q.list()){
            t = (Teams)o;
            t.setActive(1);
            session.merge(t);
            trans.commit();
        }
        session.close();
 }

    }


}
