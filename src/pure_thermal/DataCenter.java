package pure_thermal;

/**
 *
 * @author fsmm2
 */
import java.util.*;
import pure_thermal.ThermalCPU;
import pure_thermal.Air;

public class DataCenter {

    int count = 0;
    private int[] coolingPlan = {0, 0, 0, 0};
    //cooling plan from optimization. Not sure of exact format
    double ambientRoomTemp = 299.15;
    double aggregateTemp;
    int[] futureCooling;
    //int array for intensityPlan is most definately PLACEHOLDER
    double[] intensityPlan;
    String[] racks = {"a", "b", "c", "d"};
    //enum char[] better suited for racks. don't know how.
    private ArrayList<ThermalCPU> list;

    //double[] s;
    Air air = new Air();

    public DataCenter() {
    list = new ArrayList<ThermalCPU>();
        int j = 0;
        while (j < 4) {
            int i = 1;
            String current = racks[j];
            while (i < 11) {
                //TESTING purposes: one ThermalCPU with excessive heat PLACEHOLDER: testing methods
                if (i == 3 && j == 1) {
                    ThermalCPU name = new ThermalCPU(i, current, 327.15, 0, true);
                    i++;
                    list.add(name);
                } else {
                    ThermalCPU name = new ThermalCPU(i, current);
                    i++;
                    list.add(name);
                }
            }
            j++;
        }
    }

    public void showTempArray() //need to add useful getTempArray() PLACEHOLDER: stub
    {
        for (ThermalCPU a : getList()) {
            System.out.println(a.getName() + ": " + (a.getTemp() - 273.15));
        }
    }

    public double[][] getTempArray() {
        double[][] output=new double[4][10];
        ThermalCPU c;

        for(int j=0; j<4; j++)
        {
            for(int i=0; i<10; i++)
            {
                c=getList().get(i+1);
            output[j][i]= c.getTemp() - 273.15;
            }
        }
        
        return output;
    }

    public void showIntensityPlan() //need to add useful getIntensityArray() PLACEHOLDER: stub
    {
        for (ThermalCPU a : getList()) {
            System.out.println(a.getName() + ": " + a.getIntensity());
        }
    }

    public void setIntensityPlan(double[] plan) {
        intensityPlan = plan;
    }

    public ArrayList<String> getHotspots() //should also timestamp array, as may runs will be taken
    {
        ArrayList<String> stringlist = new ArrayList<String>();
        for (ThermalCPU a : getList()) {
            double temp = a.getTemp();
            //temp 343.15 is a PLACEHOLDER: group decision
            if (temp > 343.15) {
                String hot = "!" + a.getName() + " : " + (a.getTemp() - 273.15) + "!";
                stringlist.add(hot);
            }

        }
        return stringlist;

    }

    public void showHotspots() {
        ArrayList<String> secondstring = new ArrayList<String>();
        for (ThermalCPU a : getList()) {
            double temp = a.getTemp();
            if (temp > 343.15) {
                String hot = "!" + a.getName() + " : " + (a.getTemp() - 273.15) + "!";
                secondstring.add(hot);
            }

        }
        for (String a : secondstring) {
            System.out.println(a);
        }

    }

    public float getRoomTemp() {
        for (int p = 0; p + 1 < getList().size(); p++) {
            Air airAtCpu = new Air(p);
            double temp = airAtCpu.getTemp();
            aggregateTemp = aggregateTemp + temp;
        }
        double cpuAirTemp = aggregateTemp / 40.0;
        ambientRoomTemp = (cpuAirTemp + ambientRoomTemp) / 2.0;
        ambientRoomTemp = ambientRoomTemp - 273.16;
        float roomTemp = (float) ambientRoomTemp;
        return roomTemp;
    }

     public void updateArray(int[] cooling) {
        ThermalCPU current = new ThermalCPU();
        ThermalCPU next = new ThermalCPU();

        for (int p = 0; p + 1 < list.size(); p++)
        {
            //Air airAroundNext = airArray[p];
            current = list.get(p);
            current.setIntensity(intensityPlan[p]);
           // current.setIntensity(25);
            int index;
            index = p/10;
                if (current.getPos() != (index+1)*10) {
                    //we will need middleware to get schedule's intensity plan from the database
                    //and calculate it into an average intensity. INTEGRATION PLACEHOLDER
                    //get the intensity from the intensityPlan[p].
                    next = list.get(p + 1);
                    current.run(cooling[index], next, air);
                    list.set(p, current);
                    list.set(p + 1, next);
                }
                else {
                    current.run(cooling[index]);
                    list.set(p, current);
                }

          }
    }

    public void showArrayLength() {
        System.out.println(getList().size());
    }

    public void runArray(int i) {
        //i=100;
        //DataCenter d = new DataCenter();
        for (int j = 1; j <= i; j++) {
               updateArray(getCoolingPlan());
        }
    }

    public void predictArray(int i) {
        DataCenter d= new DataCenter();
        for (int j = 0; j < i; j++) {
            d.updateArray(getCoolingPlan());
        }
    }

    public void HotspotDisplay() {
        ArrayList<String> hotspots = this.getHotspots();

        for (String a : hotspots) {
            System.out.println(a);
        }
    }
//testing PLACEHOLDER. updateCooling should update futureCooling

//    public void setCoolingPlan(int[] a) {
//        setCoolingPlan(a);
//    }

    public void showCooling() {
        System.out.println(this.getCoolingPlan()[0]);
        System.out.println(this.getCoolingPlan()[1]);
        System.out.println(this.getCoolingPlan()[2]);
        System.out.println(this.getCoolingPlan()[3]);
    }

//count defines the time in the datacenter. at 30, i.e. 5 minute mark, new cooling plan is implemented and count is reset.
    public int getCount() {
        return count;
    }

    public double getAirTemp() {

     Double airTemp= air.getTemp();
        return airTemp;
    }

    public void resetAirTemp() {

            air.setTemp(ambientRoomTemp);
        }

    public double[] flattenArray(double[][] input)
    {
        double[] output= new double[40];
        int k=0;
        for(int i=0; i<input.length; i++)
        {
            for(int j=0; j<input[i].length; j++)
            {
                output[k]=input[i][j];
                k++;
            }
        }
        return output;
    }
 public int[] flattenArray(int[][] input)
    {
        int[] output=null;
        int k=0;
        for(int i=0; i<input.length; i++)
        {
            for(int j=0; j<input[i].length; j++)
            {
                output[k]=input[i][j];
                k++;
            }
        }
        return output;
    }
    public ThermalCPU getThermalCPU(String l, int pos)
    {
        ThermalCPU placeholder=new ThermalCPU();
        for(ThermalCPU a:getList())
        {
            if(a.getPos()==pos&&a.getRack().compareTo(l)==0)
                placeholder=a;
        }

        return placeholder;

    }

    /**
     * @return the list
     */
    public ArrayList<ThermalCPU> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(ArrayList<ThermalCPU> list) {
        this.list = list;
    }

    /**
     * @return the coolingPlan
     */
    public int[] getCoolingPlan() {
        return coolingPlan;
    }

    /**
     * @param coolingPlan the coolingPlan to set
     */
    public void setCoolingPlan(int[] coolingPlan) {
        this.coolingPlan = coolingPlan;
    }
    }
