 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package j.h.entities;

import H.utility.NewHibernateUtil;
import h.entities.Mode;
import h.entities.Row;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author kac4
 */
public class CoolingPlan {
int[] num = new int[4];
Row row;
Session session;
Mode mod;

    public int[] getNum() {
        return num;
    }

    public void setNum(int[] num) {
        this.num = num;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }


public void getCoolingPlan(){
    session = NewHibernateUtil.getSessionFactory().openSession();
    Transaction trans = session.beginTransaction();
    Query q = session.createQuery("From Row");
    int i = 0;
    for(Object o : q.list()){
        row = (Row)o;
        num[i] = row.getMode().getModeId();
        i++;
    }
    session.close();
}

public void updateCoolingPlan(){
    session = NewHibernateUtil.getSessionFactory().openSession();

    int count = 0;
    Query q = session.createQuery("From Row");
    for(Object o : q.list()){
        Transaction trans = session.beginTransaction();
        row = (Row)o;
        Query q1 = session.createQuery("From Mode as m where m.modeId = " + num[count] );
        Object o2 = q1.list().get(0);
        mod = (Mode)o2;
        row.setMode(mod);
        session.merge(row);
        trans.commit();
        count++;
    }
    session.close();
    }
}

