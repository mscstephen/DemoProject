
package PowerModule;

/**
 *
 * @author fiona
 */
public class PUE {

    //protected float PUE;
    protected double PUE;

    public PUE(){

    }

    public void CalcPUE(float computingPower, float totalPower){

       double x = totalPower/computingPower;
       System.out.println("PUE:" + x);
       double pue = Math.round(x);
       this.setPUE(pue);
      // double pue = Math.round(this.getPUE());
       System.out.println("Total PUE is "+ pue);

    }
 /* public static double Round(double Rval, int Rpl) {
  double p = (double)Math.pow(10,Rpl);
  Rval = Rval * p;
  double tmp = Math.round(Rval);
  return (double)tmp/p;
  }*/
   /*  public float getPUE(){
    return PUE;
   }

     public void setPUE(float pue){
        PUE = pue;
   }*/
     public double getPUE(){
    return PUE;
   }

     public void setPUE(double pue){
        PUE = pue;
   }


}
