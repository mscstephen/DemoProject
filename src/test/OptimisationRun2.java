/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.util.TimerTask;
import module.Cooling;

/**
 *
 * @author kac4
 */
public class OptimisationRun2 extends TimerTask {
    public void run(){
        Cooling co = new Cooling();
        co.main();
    }

}
