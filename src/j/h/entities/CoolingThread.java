/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package j.h.entities;

/**
 *
 * @author kac4
 */
public class CoolingThread  extends Thread{
    @Override
    public void run(){
        Cooling1 col = new Cooling1();
        col.main();
    }

}
