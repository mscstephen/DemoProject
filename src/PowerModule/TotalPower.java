
package PowerModule;

/**
 *
 * @author fiona
 */
public class TotalPower {
    public float totalPower;
    public float computing;
    public float nonComputing;

    public TotalPower(){

    }

    public void CalcTotalPower(){
        this.setTotalPower(computing + nonComputing);
        float total = Round(this.getTotalPower(),0);
        System.out.println("Total Power is "+ total);

    }
    public static float Round(float Rval, int Rpl) {
  float p = (float)Math.pow(10,Rpl);
  Rval = Rval * p;
  float tmp = Math.round(Rval);
  return (float)tmp/p;
  }
    public void setTotalPower(float total){
        totalPower  = total;
    }
    public float getTotalPower(){
        return totalPower;
    }
}
