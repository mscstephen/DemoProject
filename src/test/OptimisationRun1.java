/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import module.Run;

/**
 *
 * @author kac4
 */
public class OptimisationRun1 extends Thread {
    String[] arg = new String[10];

    @Override
    public void run(){

        Run r = new Run();
        r.main(arg);

    }

}
