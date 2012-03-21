/*
 * Change Log
 * =============================================================================
 * Time:	14th/Mar.
 * Changes:	Added try catch statement for hibernate session in case the
 *                  connection failed.
 *              Added funcion checkAction(), which reads in the active status
 *                  of optimisation team, and returns it as an integer.
 * =============================================================================
 */
package schedulingproject;

import h.entities.Teams;
import H.utility.NewHibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author hg1
 */
public class Activator {

    Session session;
    Transaction tran;

    /*
     * For Optimization:
     * 0 active
     * 1 inactive
     * 2 re-schedule
     */
    public void setActive(boolean active) {
        // TODO Auto-generated method stub
        System.out.println("Scheduling Team - set active START");
        session = NewHibernateUtil.getSessionFactory().openSession();
        tran = null;
        try {
            tran = session.beginTransaction();
            Teams chg = new Teams();
            Query q = session.createQuery("From Teams");
            Object o = (Object) q.list().get(1);
            Teams team = (Teams) o;
            if (active) {
                team.setActive(0);
                System.out.println("    active");
            } else {
                team.setActive(1);
                System.out.println("    inactive");
            }
            chg = team;
            session.merge(chg);
            tran.commit();
        } catch (Exception e) {
            if (tran != null) {
                tran.rollback();
            }
            System.out.println("Hibernate session exception caught: " + e.toString());
//            throw e;
        } finally {
            session.close();
        }
        System.out.println("Scheduling Team - set active END");
    }


    public int checkAction() {
        // TODO Auto-generated method stub
        System.out.println("Scheduling Team - check trigger START");
        session = NewHibernateUtil.getSessionFactory().openSession();
        int action = 0;
        try {
            Query q = session.createQuery("From Teams");
            Object o = (Object) q.list().get(0);
            Teams team = (Teams) o;
            action = team.getActive();
        } catch (Exception e) {
            System.out.println("Hibernate session exception caught: " + e.toString());
//            throw e;
        } finally {
            session.close();
        }
        System.out.println("Scheduling Team - check trigger END");
        return action;
    }
}
