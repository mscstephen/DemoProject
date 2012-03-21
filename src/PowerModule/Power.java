/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PowerModule;

/**
 *
 * @author noc3
 */
import h.entities.Cpu;
import h.entities.Health;
import h.entities.Pmap2;
import h.entities.Processingrate;
import h.entities.Row;
import h.entities.Totalpower;
import H.utility.NewHibernateUtil;
import java.math.BigDecimal;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Power {

    int[] cool = new int[4];
    double[][] process = new double[4][10];
    Processingrate rate;
    Row row;
    Cpu cpu2;
    Session session;
    double[][] map = new double[4][10];
    int[][] otherMap = new int[4][10];
    int[] coolingMap = new int[4];
    int[] health = new int[40];

    public double[][] getProcessingRate() {
        int cpu = 0;
        int row = 0;
        Processingrate pro;
        session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        Query q = session.createQuery("From Processingrate as name");
        //+ "where pro Id = 1");

        for (Object o : q.list()) {

            pro = (Processingrate) o;

            //System.out.print(pro.getProcessingPower() + "\n");
            if (cpu < 10) {
                double a = pro.getProcessingPower().doubleValue();
                map[row][cpu] = a;
            } else {
                row++;
                cpu = 0;
                map[row][cpu] = pro.getProcessingPower().doubleValue();
            }
            cpu++;
        }
        //System.out.print(map + "\n");
        session.close();
        return map;
    }

    public double[][] getThermal() {
        int cpu = 0;
        int row = 0;
        Pmap2 map2;
        session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction Trans = session.beginTransaction();
        Query q = session.createQuery("From Pmap2");
        for (Object o : q.list()) {
            map2 = (Pmap2) o;
            if (cpu < 10) {
                map[row][cpu] = map2.getCurrentTemp().doubleValue();
            } else {
                row++;
                cpu = 0;
                map[row][cpu] = map2.getCurrentTemp().doubleValue();
            }
            cpu++;
        }
        session.close();
        return map;
    }

    public int[] getCooling() {
        session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction trans = session.beginTransaction();
        Query q = session.createQuery("From Row");

        for (Object o : q.list()) {
            int i = 0;
            row = (Row) o;
            cool[i] = row.getMode().getModeId();
            i++;
        }

        System.out.println(map);
        session.close();
        return coolingMap;
    }

    public int[][] getHealth() {

        session = NewHibernateUtil.getSessionFactory().openSession();
        Transaction Trans = session.beginTransaction();
        Query q = session.createQuery("From Cpu");
        int cpu = 0;
        int row = 0;
        for (Object o : q.list()) {

            cpu2 = (Cpu) o;
            if (cpu < 10) {
                otherMap[row][cpu] = cpu2.getHealth().getHid();
            } else {
                row++;
                cpu = 0;
                otherMap[row][cpu] = cpu2.getHealth().getHid();
            }
            cpu++;
        }

        session.close();
        return otherMap;
    }

    public void updateHealth(int[] array) {
        Cpu c;
        Health h;
        session = NewHibernateUtil.getSessionFactory().openSession();
        Query q2 = session.createQuery("From Health as h where h.hid = 2");
        Object o2 = q2.list().get(0);
        h = (Health)o2;
        Query q = session.createQuery("From Cpu");
        int i = 0;
        for(Object o : q.list()){
            c = (Cpu)o;
                if(array[i] == 2){
                    Transaction trans = session.beginTransaction();
                    c.setHealth(h);
                    session.merge(c);
                    trans.commit();
                }
            i++;
            }

        session.close();
    }

    public void updatePower(float tp , double pue){
        Totalpower pow;

       session = NewHibernateUtil.getSessionFactory().openSession();
       Query q = session.createQuery("From Totalpower as t where t.tpid = 1");
       for(Object o : q.list()){
           pow = (Totalpower)o;
           BigDecimal tol = new BigDecimal(tp);
           BigDecimal p = new BigDecimal(pue);
           pow.setPue(p);
           pow.setTotalPower(tol);
           Transaction trans = session.beginTransaction();
           session.merge(pow);
           trans.commit();
       }
       session.close();
    }
}
