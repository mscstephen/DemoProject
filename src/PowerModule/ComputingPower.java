package PowerModule;

/**
 *
 * @author fiona
 */
public class ComputingPower {

    protected double[][] schedule;
    protected int[][] healthMatrix;
    private int pMax;
    private int pMin;
    protected float sum;
    protected float compPower;
    private float calculatedPower;
    private int cols = 4;
    private int rows = 10;

    public ComputingPower() {
        schedule = new double[4][10];
        healthMatrix = new int[4][10];
        this.pMax = 400;
        this.pMin = 200;
    }

    public float computePower(double [][] job, int[][]health) {
        double result = 0 ;
        float resultConversion = 0;
        sum = 0;
            for (int colume = 0; colume < cols; colume++)
            {
                for (int row = 0; row < rows; row++) {
                try{
                    if (job[colume][row] > 0.8 || job[colume][row] < 0)
                    {
                        if(job[colume][row]>.8){
                        job[colume][row]=.8;
                        }
                        else{
                        job[colume][row]=0;
                        }
                    }
                    if(health[colume][row] == 1){
                        result = (((pMax - pMin) * (job[colume][row])) + pMin);
                        resultConversion = (float) result;
                        }
                        else{
                            resultConversion =0;
                       }
                        sum = sum + resultConversion;


                }catch(Exception e){
                    System.out.println("Exception "+e);
                }
                }
            }

        return sum;

    }

    public double[][] getJobProcessingRate() {

        return schedule;
    }

    public void computingInputs(double[][] job,int[][] health ){
        System.out.println(job[0][0] +" "+ health[0][0]+"++++++++++++++++++++++++++++++++++++++++");
        boolean jobRateMatrixSuccess = true;
          boolean healthMatrixSuccess = true;
          jobRateMatrixSuccess = this.setJobProcessingRate(job);
          healthMatrixSuccess = this.setHealth(health);
        if (!jobRateMatrixSuccess){
            //make call to pull t-1 processingRates
            //double [][]x = return value
            //this.setJobProcessingRate(x);
        }
        if(!healthMatrixSuccess){
            //make call to pull t-1 health
        }

            calculatedPower = this.computePower(this.getJobProcessingRate(), this.getHealth());
            this.setComputingPower(calculatedPower/1000);


    }
    public boolean setJobProcessingRate(double[][] job) {
        if(job.length == cols && job[0].length == rows){
                        schedule = job;
                        return true;
        }
           return false;
    }
    public int[][] getHealth() {
        return healthMatrix;
    }

    public boolean setHealth(int[][] health) {
        if(health.length == cols && health[0].length == rows){
            healthMatrix = health;
            return true;
        }
        return false;
    }
    public void setComputingPower(float power) {
        compPower = power;
    }
    public float getComputingPower() {
        return compPower;
    }
}
