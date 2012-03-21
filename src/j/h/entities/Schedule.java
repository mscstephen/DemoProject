/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package j.h.entities;

import H.utility.NewHibernateUtil;
import h.entities.Job;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author kac4
 */
public class Schedule {
    Session session;
    double[][] plant = new double[4][10];
    double intensity;
    Job j;

    public void getSchedule(){
        double[] name = new double[40];
        session = NewHibernateUtil.getSessionFactory().openSession();
        double member = 0;
        for(int cpu = 1; cpu <=40; cpu++){
        Query q = session.createQuery("FROM Job as job Where job.cpuid = "+ cpu + "and job.active = 0");
        if (!q.list().isEmpty()){
        for(Object o : q.list()){
          j = (Job)o;
          member = member + j.getProcessingrate().doubleValue();
        }
       name[cpu - 1] = member;
        member = 0;
        }
 else{
            name[cpu - 1] = 0;
 }
        }
        this.createArray(name);
        session.close();
    }


    public void createArray(double[] d){
      int count = 0;
      if (d == null){
         System.out.println("array empty");
      }
 else{
    for(int i = 0; i < 4; i ++){
        for(int y = 0; y < 10; y++){
         plant[i][y] = d[count];
         count++;
        }
        }
        }
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    public double[][] getPlant() {
        return plant;
    }

    public void setPlant(double[][] plant) {
        this.plant = plant;
    }

}
