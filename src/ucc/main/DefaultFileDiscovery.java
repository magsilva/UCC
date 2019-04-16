package ucc.main;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.utils.FileUtils;

public class DefaultFileDiscovery
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(DefaultFileDiscovery.class);

   /** Get a handle to the single instance of RuntimeParameters object */
   private static RuntimeParameters RtParams = RuntimeParameters.GetInstance();

   /**
    * 
    */

   /**
    * This method seeks to find the file list in the same directory as the
    * executable.
    * 
    * @param argsList
    *           (List&lt;String&gt;) - list of arguments (command sequence)
    */
   public static void findFileList(List<String> argsList)
   {
      // final String p =
      // MainUCC.class.getProtectionDomain().getCodeSource().getLocation().getPath();
      File path = null;
      // This dead code is left here on the chance that file placement make be
      // changed.
      // This is one way to the handle it.
      // try
      // {
      // String decodedPath = URLDecoder.decode(p, "UTF-8");
      // int i = decodedPath.lastIndexOf("/");
      // if (i >= 0)
      // {
      // decodedPath = decodedPath.substring(0, i + 1);
      // }
      // path = new File(decodedPath);
      // }
      // catch (UnsupportedEncodingException e)
      // {
      // path = new File(System.getProperty("user.home"));
      // logger.error("Not able to parse the path to the executable. Defaulting
      // to the home directory: "
      // + path.getAbsolutePath(), e);
      // }
      path = new File(System.getProperty("user.dir"));

      // Set up all the files to try
      final File[] FILE_NAMES = { new File(path, "filelist.txt"), new File(path, "fileList.txt"),
               new File(path, "filelist.dat"), new File(path, "fileList.dat") };

      for (int i = 0; i < FILE_NAMES.length; i++)
      {
         if (FILE_NAMES[i].exists() == true)
         {
            if (FileUtils.VerifyFileList(FILE_NAMES[i].getAbsolutePath(), RtParams.FileListA) == true)
            {
               argsList.add("-i1");
               argsList.add(FILE_NAMES[i].getAbsolutePath());
               return;
            }
         }
      }

      System.out.println("No file list was found in " + RtParams.OutputDirPath);
      logger.error("No file list was found in " + RtParams.OutputDirPath);
   }

   /**
    * This method handles the case where only the differencing argument is
    * provided in the command line arguments.
    * 
    * @param argsList
    *           (List&lt;String&gt;) - list of argument (command sequence)
    */
   public static void HandleDifferencingDefault(List<String> argsList)
   {
      final String fileListNameA = "fileListA.txt";
      final String fileListNameB = "fileListB.txt";

      File fileA = new File(System.getProperty("user.dir"), fileListNameA);
      File fileB = new File(System.getProperty("user.dir"), fileListNameB);

      if (fileA.exists() == true)
      {
         if (fileB.exists() == true)
         {
            if (FileUtils.VerifyFileList(fileListNameA, RtParams.FileListA) == true)
            {
               if (FileUtils.VerifyFileList(fileListNameB, RtParams.FileListB) == true)
               {
                  argsList.add("-i1");
                  argsList.add(fileListNameA);
                  argsList.add("-i2");
                  argsList.add(fileListNameB);
               }
               else
               {
                  logger.error(fileListNameB + "is not a valid file list.");
               }
            }
            else
            {
               logger.error(fileListNameA + "is not a valid file list.");
            }
         }
         else
         {
            logger.error(fileListNameB + "does not exist in the current directory.");
         }
      }
      else
      {
         logger.error(fileListNameA + " does not exist in the current directory.");
      }
   }
}
