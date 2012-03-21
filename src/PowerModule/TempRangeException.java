/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PowerModule;

/**
 *
 * @author rcg2
 */
class TempRangeException extends Exception {

    String msg;

    public TempRangeException()
    {
        msg = "The supplied Room Temperature value is outside the allowable values for Temperature.";
    }
}
