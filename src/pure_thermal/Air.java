package pure_thermal;
/**
*
* @author njps2
*/
public class Air {
        double coolingPlan1 = 293.15;
    double coolingPlan2 = 283.15;
    double coolingPlan3 = 278.15;
	double currentTemp;
	int position;
	double originalTemp = 299.15;
	public Air(int posInCpuArrayList, double temp) {
    	position=posInCpuArrayList;
   	currentTemp = temp;
	}

        public Air()
        {
            currentTemp=originalTemp;
        }

	public Air(int posInCpuArrayList) {
   	position=posInCpuArrayList;
        currentTemp=originalTemp;
	}
	public double getTemp() {
   	return currentTemp;
	}
	public void setTemp(double i) {
   	currentTemp = i;
	}

        public void airCool(int[] cooling)
    {
            double[] coolingArray=new double[4];
            for(int i=0; i<4; i++)
            {
                if(cooling[i]==0)
                    coolingArray[i]=currentTemp;
                  if(cooling[i]==1)
                      coolingArray[i]=coolingPlan1;
                if(cooling[i]==2)
                    coolingArray[i]=coolingPlan2;
                if(cooling[i]==3)
                    coolingArray[i]=coolingPlan3;
        }
           double averageCooling=(coolingArray[0]+coolingArray[1]+coolingArray[2]+coolingArray[3])/40;
           currentTemp=currentTemp-averageCooling;
        }
}

