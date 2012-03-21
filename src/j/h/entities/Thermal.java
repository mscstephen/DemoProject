/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package j.h.entities;

import H.utility.NewHibernateUtil;
import h.entities.Pmap2;
import java.math.BigDecimal;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author kac4
 */
public class Thermal {

    double[][] map;
    double airIn;
    Session session;

    public void Submit(double[][] d, double a) {
      this.setMap(d);
       this.setAirIn(a);
        Pmap2 map2;
        int cpu = 0;
        int row = 0;

        session = NewHibernateUtil.getSessionFactory().openSession();
       
        Query q = session.createQuery("From Pmap2");

        for (Object o : q.list()) {
             Transaction trans = session.beginTransaction();
            map2 = (Pmap2) o;
            if (cpu < 10) {
                BigDecimal num = new BigDecimal(d[row][cpu]);
                map2.setCurrentTemp(num);
                map2.setFutureTemp(new BigDecimal(a));
                cpu++;
            } else {
                row++;
                cpu = 0;
                BigDecimal num = new BigDecimal(d[row][cpu]);
                map2.setCurrentTemp(num);
                 map2.setFutureTemp(new BigDecimal(a));
                  cpu++;
            }
            session.merge(map2);
            trans.commit();
           
        }
        session.close();
    }

    public void getThermal() {
        int cpu = 0;
        int row = 0;
        Pmap2 map2;
        double[][] map3 = new double[4][10];
        session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction Trans = session.beginTransaction();
        Query q = session.createQuery("From Pmap2");
        for (Object o : q.list()) {
            map2 = (Pmap2) o;
            if (cpu < 10) {
                map3[row][cpu] = map2.getCurrentTemp().doubleValue();
                cpu++;
            } else {
                row++;
                cpu = 0;
                map3[row][cpu] = map2.getCurrentTemp().doubleValue();
                cpu++;
            }

        }
        this.setMap(map3);
        session.close();
    }

    public double getAirIn() {
        return airIn;
    }

    public void setAirIn(double airIn) {
        this.airIn = airIn;
    }

    public double[][] getMap() {
        return map;
    }

    public void setMap(double[][] map) {
        this.map = map;
    }
}
