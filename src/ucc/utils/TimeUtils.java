package ucc.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class contains all the common timing operations. Functions in this class
 * are static so they can be called directly without instantiating an object of
 * this class.
 * 
 * @author Integrity Applications Incorporated
 * 
 */
public class TimeUtils
{
   /** Instantiate the Log4j logger for this class */
   private static final Logger logger = LogManager.getLogger(TimeUtils.class);

   /** A constant to convert from Nanoseconds to seconds */
   public static final double NANOSEC_PER_SEC = 1000000000.0;

   public static long GetTime()
   {
      return System.nanoTime();
   }

   public static void PrintElapsedTime(long start, long end, String function)
   {
      long duration = (end - start);
      String output = String.format("%s execution time: %.5f (s)", function, duration / NANOSEC_PER_SEC);
      System.out.println(output);
      logger.info(output);
   }
}
