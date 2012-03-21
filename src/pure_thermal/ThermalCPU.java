package pure_thermal;

import pure_thermal.Air;

public class ThermalCPU {

    double currentTemp;
//probably best to work in Kelvin, but other teams might find it easier to work in Celsius.
//current figures computed in Kelvin, but printed in Celsius
//if unit time is 10 seconds, need to work with double, as very small increments in temperature in
//this timeframe
    double intensity;
//intensity should be between 5-100
//may not be “intensity", but some input to determine activity, and therefore heat production
//may need to change to float if intensity very variable. PLACEHOLDER: data type
    boolean isAlive;
//not yet used. this is probably not changed by us, but by the power team.
    String rack;
//rack should be an enum PLACEHOLDER: data type
    public int pos;
    public String name;
    double default_temp = 293.15;
    int default_intensity = 25;
//actual default intensity should be about 5, to reflect activity just by being on PLACEHOLDER: specifics
    double coolingPlan1 = 293.15;
    double coolingPlan2 = 283.15;
    double coolingPlan3 = 278.15;

    public ThermalCPU() {
        pos = 0;
        rack = "z";
        currentTemp = default_temp;
        name = "z" + "0";
        isAlive = true;
        intensity = default_intensity;

    }

    public ThermalCPU(int p, String r) {
        pos = p;
        rack = r;
        currentTemp = default_temp;
        isAlive = true;
        intensity = default_intensity;
        name = r + Integer.toString(p);
    }

    public ThermalCPU(int p, String r, double t, int i, boolean b) {
        currentTemp = t;
        intensity = i;
        isAlive = b;
        pos = p;
        rack = r;
        name = r + Integer.toString(p);
    }

    public int getPos() {
        return pos;
    }

    public String getRack(){
        return rack;
        
    }

    public String getName() {
        return name;
    }

    public double getTemp() {
        return currentTemp;
    }

    public void setTemp(double i) {
        currentTemp = i;
    }

    public double getIntensity() {
        return intensity;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public void setIntensity(double i) {
        intensity = i;
    }

    public double heatDiff(ThermalCPU cpu) {
        double ans, a, b;
        a = this.getTemp();
        b = cpu.getTemp();
        ans = a - b;
        return ans;
    }

    public void generateHeat() {
        double generated = (double) intensity / 363;
        currentTemp += generated;
//PLACEHOLDER: intensity is assumed to be 1:1 watts. Definately not going to be the case.
//generate heat should not be based on currentTemp but only intensity.
//Delta Q(intensity/energy)=m(mass)*c(specific heat capacity)*delta T(change in temp)
//ASSUMPTIONS: ThermalCPU weighs 3Kg, it is composed of 50/50 silicon and plastic. specific heat capacity is 1.12 kJ/Kg*K.
//Therefore, constants weight and specific heat capacity form constant 3.63.
//intensity is in watts, or J/s. epoch is 10 secs, so each watt goes to 10J.
//Dividing 3.63 by 10 compensates from having to calculate this. -> 0.363.
//0.363 is still in units kJ/K, as already multiplied kg. However, other side of equation is in J, not kJ.
//converting 0.363 to J gives 363J/K.
//Q/mc=T
//intensity(J)(K)/363(J)=T(K).
    }

    public void radiate(ThermalCPU other, Air air) //may need no input method for top CPUs in racks. not sure. PLACEHOLDER: stub
    {
        double diff = this.heatDiff(other);
        double thisTemp = this.getTemp();
        double otherTemp = other.getTemp();
        double airTemp = air.getTemp();

        if (diff < 0) {
//transfer some small amount of heat "down" to lower (this) ThermalCPU
//PLACEHOLDER: maths
            double trans = diff / 50;
            this.setTemp(thisTemp - trans);
            other.setTemp(otherTemp + trans / 1.2);
            air.setTemp(airTemp + trans / 240);
        } else if (diff > 0) {
//transfer proportionally more heat "up" from this ThermalCPU to other ThermalCPU
//PLACEHOLDER: maths
            double trans = diff / 20;
            this.setTemp(thisTemp - trans);
//this ThermalCPU loses more heat to surronding area than transferred up, meaning some
//heat is lost to surronding area, otherCPU recieves less than full amount of heat. PLACEHOLDER: maths
            other.setTemp(otherTemp + (trans / 1.2));
            air.setTemp(airTemp + trans / 6);
        } else {
//do nothing
        }
    }

    public void cool(int i) {
        double diff = 0;
        if (i == 0) {
            diff = 0;
        } else if (i == 1) {
            diff = currentTemp - coolingPlan1;
        } else if (i == 2) {
            diff = currentTemp - coolingPlan2;
        } else if (i == 3) {
            diff = currentTemp - coolingPlan3;
        }
        if (diff == 0) {
        } else if (diff < 0) {
//do nothing for now. may want to slightly heat CPUs, but this call is quite unlikely
        } else if (diff > 0) {
            currentTemp -= (diff / 200);
//PLACEHOLDER: maths
        }
    }

    public void run(int cooling, ThermalCPU other, Air air) {
        this.cool(cooling);
        this.radiate(other, air);
        this.generateHeat();

    }

    public void run(int cooling) {
        this.cool(cooling);
        this.generateHeat();

    }
}
