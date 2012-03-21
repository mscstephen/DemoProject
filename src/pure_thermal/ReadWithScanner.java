package thermalproject.pure_thermal;

import java.io.*;
import java.util.Scanner;

public class ReadWithScanner {

  public static void main(String... aArgs) throws FileNotFoundException {
    ReadWithScanner parser = new ReadWithScanner("C:\\Temp\\test.txt");
    parser.processLineByLine();
    log("Done.");
  }

  /**
   Constructor.
   @param aFileName full name of an existing, readable file.
  */
  public ReadWithScanner(String aFileName){
    fFile = new File(aFileName);
  }

  /** Template method that calls {@link #processLine(String)}.  */
  public  int[] processLineByLine() throws FileNotFoundException {
      int[] array=null;
    //Note that FileReader is used, not File, since File is not Closeable
    Scanner scanner = new Scanner(new FileReader(fFile));
    try {

      //first use a Scanner to get each line
      for(int i=0; i<4; i++){
       array[i]= getInt( scanner.nextLine() );
      }
    }

    finally {
      //ensure the underlying stream is always closed
      //this only has any effect if the item passed to the Scanner
      //constructor implements Closeable (which it does in this case).
      scanner.close();
          return array;
    }
  }

  /**
   Overridable method for processing lines in different ways.

   <P>This simple default implementation expects simple name-value pairs, separated by an
   '=' sign. Examples of valid input :
   <tt>height = 167cm</tt>
   <tt>mass =  65kg</tt>
   <tt>disposition =  "grumpy"</tt>
   <tt>this is the name = this is the value</tt>
  */
  protected void processLine(String aLine){
    //use a second Scanner to parse the content of each line
    Scanner scanner = new Scanner(aLine);
    if ( scanner.hasNext() ){
      String value = scanner.next();

    }
    else {
      log("Empty or invalid line. Unable to process.");
    }
    //no need to call scanner.close(), since the source is a String
  }

  public int getInt(String aLine)
    {
      int array;
      Scanner scanner=new Scanner(aLine);

          String a=scanner.next();
          array=Integer.parseInt(a);
      return array;
  }

  // PRIVATE
  private final File fFile;

  private static void log(Object aObject){
    System.out.println(String.valueOf(aObject));
  }

  private String quote(String aText){
    String QUOTE = "'";
    return QUOTE + aText + QUOTE;
  }
}