import h.entities.Row;
import h.entities.Teams;
import j.h.entities.CoolingPlan;
import H.utility.NewHibernateUtil;
import j.h.entities.Schedule;
import j.h.entities.Thermal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Ignore;

public class ThermalModuleIntegrationTests {

    private Session session;
    private Transaction tran;
    private Teams chg;
    CoolingPlan obj = new CoolingPlan();
    Thermal thm = new Thermal();
    Schedule sch = new Schedule();
    private int[] testCoolingPlan = new int[4];
    private double[][] testThermalMap = new double[10][4];
    public ThermalModuleIntegrationTests() {

    }

 @Before
    public void setUp() {
        session = NewHibernateUtil.getSessionFactory().openSession();;
        tran = session.beginTransaction();
         chg = new Teams();
          for(int i = 0; i < 4; i++){
                    for(int y = 0; y < 10; y++ ){
                        testThermalMap[y][i] = 6.0;
                    }
                }
    }

    @After
    public void tearDown() {
        session.close();
    }
    
    @Test
    public void testSetCoolingPlan(){
        for(int i = 0; i < 4; i++){
        testCoolingPlan[i] = 2;
        }
        obj.getCoolingPlan();
        int[] numResult = obj.getNum();
        Row rowResult = obj.getRow();
        obj.setNum(testCoolingPlan);
        obj.updateCoolingPlan();
        obj.getCoolingPlan();
        assertNotSame(numResult, obj.getNum());
        //assertNotSame(rowResult, obj.getRow());
    }

    @Test
    public void testSetThermalPlan(){
       thm.Submit(testThermalMap, 7.0);
       thm.getThermal();
       //double[][] result = new double[10][4];
       assertArrayEquals(testThermalMap, thm.getMap());
    }

    @Test
	public void testGetCoolingPlanReturnsGoodData(){
		obj.getCoolingPlan();
		assertNotNull(obj.getRow());
		assertNotSame(obj.getRow().getRowId(), 0);
	}

    @Test
        public void testGettingSchedule(){
        sch.getSchedule();
        assertNotNull(sch.getPlant());
        double[][] arr = sch.getPlant();
        for(int i = 0; i < 4; i++){
            for(int y = 0; y < 10; y++){
                System.out.println(arr[y][i]);
            }
        }
        }
    @Test
	public void testGetThermalReturnsGoodData(){
                thm.getThermal();
		assertNotNull(thm.getMap());
		assertNotSame(thm.getMap().length, 0);
                //double[][] map = thm.getMap();
                //int count = 1;
               
	}
}