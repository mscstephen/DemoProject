/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.util.TimerTask;
import pure_thermal.StructuredRun;

/**
 *
 * @author kac4
 */
public class ThermalRun extends TimerTask {

    public void run(){
        StructuredRun sr = new StructuredRun();
        sr.main();
    }

}
