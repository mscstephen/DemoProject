/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package PowerModule;

/**
 *
 * @author rcg2
 */
 class NumberRangeException extends Exception {

    String msg;

    public NumberRangeException()
    {
        msg = "One of the numbers in the Cooling Plan Array is outside acceptable range.";
    }

}
