package PowerModule;

/**
 *
 * @author rcg2
 */
class ArraySizeException extends Exception {
    String msg;

    ArraySizeException()
    {
        msg = new String("Array is not of size 4");
    }

}
