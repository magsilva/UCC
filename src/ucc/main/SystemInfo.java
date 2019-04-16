package ucc.main;

import java.io.File;

/**
 * This class contains information on System resources. It provides the UCC-X
 * application number of available processing cores, total memory, free memory,
 * total hard drive space, and usable hard drive space.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class SystemInfo
{
   /** A constant to convert from Megabytes to bytes */
   public static final long BYTES_PER_MB = 1024 * 1024;

   /** A constant to convert from Gigabytes to bytes */
   public static final long BYTES_PER_GB = BYTES_PER_MB * 1024;

   /**
    * Provides number of processors (including virtual cores) available to the
    * JVM
    * 
    * @return Number of processors (including virtual cores) available to the
    *         JVM
    */
   public static int GetNumCores()
   {
      return Math.max(1, Runtime.getRuntime().availableProcessors());
   }

   /**
    * Provides the total amount of memory in the JVM
    * 
    * @return The total amount of memory in the JVM
    */
   public static long GetTotalMemory()
   {
      return Runtime.getRuntime().totalMemory();
   }

   /**
    * Provides the free amount of memory in the JVM
    * 
    * @return The free amount of memory in the JVM
    */
   public static long GetFreeMemory()
   {
      return Runtime.getRuntime().freeMemory();
   }

   /**
    * Provides the total hard disk storage space in Bytes
    * 
    * @return The total amount of hard disk storage space in Bytes
    */
   public static long GetTotalHardDiskSpace()
   {
      return new File(File.separator).getTotalSpace();
   }

   /**
    * Provides the available hard disk storage space in Bytes
    * 
    * @return The available amount of hard disk storage space in Bytes
    */
   public static long GetUsableHardDiskSpace()
   {
      return new File(File.separator).getUsableSpace();
   }

   /**
    * Displays system resources information when requested
    */
   public static void DisplaySysInfo()
   {
      System.out.println("Operating System: " + System.getProperty("os.name"));
      System.out.println("Available system resources:");
      System.out.println(" Num cores: " + SystemInfo.GetNumCores());
      System.out.println(" Total Memory: " + (double) (SystemInfo.GetTotalMemory() / SystemInfo.BYTES_PER_MB) + " MB");
      System.out.println(" Free Memory: " + (double) (SystemInfo.GetFreeMemory() / SystemInfo.BYTES_PER_MB) + " MB");
      System.out.println(
               " Total HD Space: " + (double) (SystemInfo.GetTotalHardDiskSpace() / SystemInfo.BYTES_PER_GB) + " GB");
      System.out.println(
               " Usable HD Space: " + (double) (SystemInfo.GetUsableHardDiskSpace() / SystemInfo.BYTES_PER_GB) + " GB");
      System.out.println();
   }
}