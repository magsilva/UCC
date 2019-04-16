package ucc.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

import ucc.counters.handlers.CommentHandler;
import ucc.datatypes.Constants;
import ucc.logging.Log4j2ConfigurationFactory;
import ucc.utils.ArgumentHandler;
import ucc.utils.FileUtils;
import ucc.utils.StringUtils;

/**
 * MainUCC class is the entry point into the UCC-G application.
 * <p>
 * This class receives user request via command line switch options, it parses
 * the switches, validates them, and calls appropriate methods to perform user
 * requested operations.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class MainUCC
{
   /***
    * Log4j2 Configuration. The following entry is required to disable the error
    * message that is output to the console by log4j when an external log4j2
    * properties file cannot be found.
    * 
    * For UCC-G, the log4j configuration is programmatic and does not require an
    * external properties file, so the notification must be disabled.
    */
   static
   {
      StatusLogger.getLogger().setLevel(Level.OFF);
   }

   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(MainUCC.class);

   /** Get a handle to the single instance of RuntimeParameters object */
   private static RuntimeParameters RtParams = RuntimeParameters.GetInstance();

   /**
    * Defines a flag that indicates whether the user wishes to enter a file list
    * to be parsed. In the current instance this flag is: -i1
    */
   private static boolean fileListAFlagPresent = false;

   /**
    * Starts the UCC-G application
    * 
    * @param args
    *           A String that contains command line switches entered by a user
    */
   public static void main(String[] args)
   {
      System.out.println("Welcome to " + ReleaseInfo.GetVersionInfo() + System.lineSeparator());

      // Convert the arguments into a list for easier parsing
      List<String> argsList = new ArrayList<String>(Arrays.asList(args));

      // Check if the arguments contain help or version information request
      if (argsList.contains("-h") == false && argsList.contains("-v") == false)
      {
         // Iterate first through all user entered switches options searching
         // for the optional "-outdir" parameter. The "-outdir" argument should
         // be identified immediately, in case any subsequent functionality is
         // performing logging.
         HandleOutputDirectory(argsList, "-outdir");

         // Iterate first through all user entered switches options searching
         // for the optional "-debug" parameter. The "-debug" argument should
         // be identified immediately, in case any subsequent functionality is
         // performing logging.
         HandleLogger(argsList, "-debug"); // Setup logging

         // Iterate through all user entered switches options searching for
         // optional "-nolinks" parameter. The "-nolinks" argument should be
         // identified immediately to ensure that file discovery is performed
         // correctly.
         HandleNoSymLinks(argsList, "-nolinks");

         // capture OS name in the log in case we need to trouble-shoot
         logger.info("Operating System: " + System.getProperty("os.name"));

         int index = ArgumentHandler.verifySoloArguments((ArrayList<String>) argsList, "-d", "-i1");

         if (index == ArgumentHandler.NO_ARGS)
         {
            System.out.println("No arguments provided. Seeking out the file list by default.");
            logger.info("No arguments provided. Seeking out the file list by default.");

            DefaultFileDiscovery.findFileList(argsList);
         }
         else if (index >= 0)
         {
            // Handle the case where only -i1 or -d is provided
            String soloOption = argsList.get(index);

            if (soloOption.equals("-d") == true)
            {
               DefaultFileDiscovery.HandleDifferencingDefault(argsList);
            }
            else if (soloOption.equals("-i1") == true)
            {
               argsList.remove(index);
               DefaultFileDiscovery.findFileList(argsList);
            }
         }
         else
         {
            // Handle additional arguments provided for the default case
            if (argsList.contains("-d") == false && argsList.contains("-i1") == false
                     && argsList.contains("-dir") == false && argsList.contains("-export") == false)
            {
               DefaultFileDiscovery.findFileList(argsList);
            }
            // ELSE DO NOTHING
         }
      }

      // Save user entered commands for use in reports
      StringUtils.StringArrayToStringBuffer(args, RtParams.UserInputStr);

      // Parse user entered command line arguments
      ParseUserRequest(argsList);

      // Start ProcessController if user requested a counter or a differencer
      // operation and
      // the needed data is available
      if ((RtParams.CountSLOC == true && RtParams.FileListA.size() > 0)
               || (RtParams.DiffCode == true && RtParams.FileListA.size() > 0 && RtParams.FileListB.size() > 0))
      {
         ProcessController procCntrl = new ProcessController();
         procCntrl.StartProcessController();
      }

      // Delete log file if empty
      DeleteLogFile();
   }

   /**
    * This method sets up the logger for the rest of the program.
    * 
    * @param loggingArgument
    *           User input argument
    * 
    * @param loggingFlag
    *           Logging flag that is expected
    */
   private static void SetupLogger(String loggingArgument, String loggingFlag)
   {
      loggingArgument = loggingArgument.toUpperCase();

      Level level = Level.WARN;
      String message = "UCC-G logging enabled: level set to ";

      switch (loggingArgument)
      {
         case "FATAL":
            level = Level.FATAL;
            break;
         case "ERROR":
            level = Level.ERROR;
            break;
         case "WARN":
            level = Level.WARN;
            break;
         case "INFO":
            level = Level.INFO;
            break;
         case "DEBUG":
            level = Level.DEBUG;
            break;
         case "TRACE":
            level = Level.TRACE;
            break;
         default:
            System.err.println("The " + loggingFlag + " argument must be followed by the desired log4j logging level"
                     + "(FATAL, ERROR, WARN, INFO, DEBUG or TRACE)."
                     + "By default, log4j is initialized at the WARN level.");
            System.err.println("The argument " + loggingArgument + " following " + loggingFlag + " is invalid.");
            System.exit(1);
            break; // We will not get here.
      }

      Log4j2ConfigurationFactory.Initialize(level, RtParams.OutputDirPath);
      logger.info(message + level);
   }

   /**
    * This method handles locating and operating over the existence of the
    * logging flag.
    * 
    * @param argsList
    *           (List&lt;String&gt;)
    * @param loggingFlag
    *           (String)
    */
   private static void HandleLogger(List<String> argsList, String loggingFlag)
   {
      int length = argsList.size();
      for (int i = 0; i < length; i++)
      {
         if (argsList.get(i).equalsIgnoreCase(loggingFlag) == true)
         {
            String loggingArgument = "";
            if (i + 1 < length)
            {
               loggingArgument = argsList.get(i + 1);
               argsList.remove(i + 1); // remove the argument
               SetupLogger(loggingArgument, loggingFlag); // handle the
                                                          // argument
            }

            argsList.remove(i); // remove the flag
            return;
         }
      }

      // The user did not specify a logger level; initialize and set it to
      // the Error level by default
      Log4j2ConfigurationFactory.Initialize(Level.WARN, RtParams.OutputDirPath);
   }

   /**
    * This method checks remaining arguments for file filters up until it sees a
    * new command denoted by "-" or until it runs out of arguments to parse.
    * 
    * @param argItr
    *           (ListIterator&lt;String&gt;)
    */
   private static void CheckForFileFilters(ListIterator<String> argItr)
   {
      while (argItr.hasNext() == true)
      {
         String argument = argItr.next();

         // "-" indicates a new argument and end of file filters
         if (argument.startsWith("-"))
         {
            // So that we do not ignore the next command
            argItr.previous();
            break;
         }
         else
         {
            // Get all the filters that are separated by white space
            String filters[] = argument.split("\\s+");

            // Add the filters to the list
            for (String filter : filters)
            {
               RtParams.FileSpecs.add(filter);
               logger.info("arg = " + argument + " added file spec = " + filter);
            }
         }
      }
   }

   /**
    * This method locates and handles the output directory command argument.
    * 
    * @param argsList
    *           (List&lt;String&gt;) A list of command-line arguments
    * @param outputDirectoryFlag
    *           (String)
    */
   private static void HandleOutputDirectory(List<String> argsList, String outputDirectoryFlag)
   {
      // Locate the outputDirectoryFlag within the argsList.
      int length = argsList.size();
      for (int i = 0; i < length; i++)
      {
         if (argsList.get(i).equalsIgnoreCase("-export") == true)
         {
            i += 3; // only +3 because i++ at end makes this +4
            continue;
         }
         else
         {
            if (argsList.get(i).equalsIgnoreCase(outputDirectoryFlag) == true)
            {
               String argument = "";
               if (i + 1 < length)
               {
                  argument = argsList.get(i + 1);
                  argsList.remove(i + 1); // remove the argument
                  SetOutputDir(argument); // handle the argument
               }
               argsList.remove(i); // remove the flag
               return;
            }
         }
      }
   }

   /**
    * This method locates and handles the nolinks command argument. If the OS is
    * a Windows variant, then -nolinks is ignored if specified.
    * 
    * @param argsList
    *           (List&lt;String&gt;) A list of command-line arguments
    * @param nolinksFlag
    *           (String)
    */
   private static void HandleNoSymLinks(List<String> argsList, String noLinksFlag)
   {
      String OS = System.getProperty("os.name").toLowerCase();

      // Locate the noLinksFlag within the argsList.
      int length = argsList.size();
      for (int i = 0; i < length; i++)
      {
         if (argsList.get(i).equalsIgnoreCase(noLinksFlag) == true)
         {
            if (OS.contains("windows"))
               RtParams.SkipSymbLinks = false;
            else
               RtParams.SkipSymbLinks = true;

            argsList.remove(i); // remove the flag
            return;
         }
      }
   }

   /**
    * Parses user provided options, validates them, and stores them in a
    * {@link RuntimeParameters} object to be used on-the-fly by other classes.
    * Simple operations such as display of version, usage, and help information
    * is handled by the function. Complicated operations such as code count and
    * code differencer are handed off to methods in {@link ProcessController}
    * class.
    * <p>
    * To add additional user switches, first add a data field to the
    * {@link RuntimeParameters} class if the data associated with the switch
    * needs to be saved for future usage. Then, add the parsing of the switch to
    * this function. Data stored in the single instance of
    * {@link RuntimeParameters} object will be available to any object in the
    * UCC-G code baseline.
    * 
    * @param argsList
    *           A String that contains command line switches entered by a user
    */
   private static void ParseUserRequest(List<String> argsList)
   {
      // With the logger set up, we can now iterate through the arguments
      ListIterator<String> argItr = argsList.listIterator();

      // Parse and handle all additional arguments
      while (argItr.hasNext() == true)
      {
         String arg = argItr.next().toLowerCase();

         switch (arg)
         {
            case "-h":
               if (argItr.hasNext() == true)
               {
                  ReleaseInfo.PrintHelpInfo(argItr.next());
               }
               else
               {
                  // User did not provide a specific switch to get help
                  // information, print default help information
                  ReleaseInfo.PrintHelpInfo(" ");
               }

               // Exit after handling user's help request
               System.exit(0);
               break;
            case "-v":
               System.out.println("Version = " + ReleaseInfo.GetVersionInfo());

               // Exit after handling user's version information request
               System.exit(0);
               break;
            case "-dir":
               if (RtParams.DiffCode == true)
               {
                  // Clearer logic than <index> + 2 < args.length
                  int remainingArguments = argsList.size() - argItr.nextIndex();
                  if (remainingArguments >= 2)
                  {
                     RtParams.DirPathA = argItr.next();

                     RtParams.DirPathB = argItr.next();

                     // Check if user specified any file filters
                     CheckForFileFilters(argItr);

                     // Generate list of files at the user provided paths with
                     // the given filters
                     FileUtils.GenerateFilteredFileList(RtParams.DirPathA, RtParams.FileSpecs, RtParams.FileListA,
                              RtParams.SkipSymbLinks);
                     FileUtils.GenerateFilteredFileList(RtParams.DirPathB, RtParams.FileSpecs, RtParams.FileListB,
                              RtParams.SkipSymbLinks);
                  }
                  else
                  {
                     System.err.println("Please specify two directory paths for differencer operation");
                     System.out.println(ReleaseInfo.GetUsageInfo());
                     System.exit(1);
                  }
               }
               else
               {
                  // Clearer logic than <index> + 1 < args.length
                  int remainingArguments = argsList.size() - argItr.nextIndex();
                  if (remainingArguments >= 1)
                  {
                     RtParams.DirPathA = argItr.next();

                     // Check if user specified any file filters
                     CheckForFileFilters(argItr);

                     // Generate a list of files at the user provided path with
                     // the
                     // given filters
                     FileUtils.GenerateFilteredFileList(RtParams.DirPathA, RtParams.FileSpecs, RtParams.FileListA,
                              RtParams.SkipSymbLinks);
                  }
                  else
                  {
                     System.err.println("Please specify valid arguments for -dir option");
                     System.out.println(ReleaseInfo.GetUsageInfo());
                     System.exit(1);
                  }
               }
               break;
            case "-d":
               RtParams.DiffCode = true;
               RtParams.CountSLOC = false;
               break;
            case "-i1":
               fileListAFlagPresent = true;
               // Check if -i1 option was specified along with -d option for
               // differencing
               if (RtParams.DiffCode == true)
               {
                  // Clearer logic than <index> + 3 < args.length
                  int remainingArguments = argsList.size() - argItr.nextIndex();
                  if (remainingArguments >= 3)
                  {
                     RtParams.FileListNameA = argItr.next();
                     if (FileUtils.VerifyFileList(RtParams.FileListNameA, RtParams.FileListA) == false)
                     {
                        System.err
                                 .println("Invalid file list: " + RtParams.FileListNameA + ".  Exiting the program...");
                        System.exit(1);
                     }
                     RtParams.CountSLOC = false;
                  }
                  else
                  {
                     System.err.println("Please specify two file lists for differencing");
                     System.out.println(ReleaseInfo.GetUsageInfo());
                     System.exit(1);
                  }
               }
               else
               {
                  // Clearer logic than <index> + 1 < args.length
                  int remainingArguments = argsList.size() - argItr.nextIndex();
                  if (remainingArguments >= 1)
                  {
                     RtParams.FileListNameA = argItr.next();
                     if (FileUtils.VerifyFileList(RtParams.FileListNameA, RtParams.FileListA) == false)
                     {
                        System.err
                                 .println("Invalid file list: " + RtParams.FileListNameA + ".  Exiting the program...");
                        System.exit(1);
                     }
                  }
                  else
                  {
                     System.err.println("Please specify a file list with -i1 option");
                     System.out.println(ReleaseInfo.GetUsageInfo());
                     System.exit(1);
                  }
               }
               break;
            case "-i2":
               if (RtParams.DiffCode == true)
               {
                  if (fileListAFlagPresent == true)
                  {
                     RtParams.FileListNameB = argItr.next();
                     if (FileUtils.VerifyFileList(RtParams.FileListNameB, RtParams.FileListB) == false)
                     {
                        System.err
                                 .println("Invalid file list: " + RtParams.FileListNameB + ".  Exiting the program...");
                        System.exit(1);
                     }
                  }
                  else
                  {
                     System.err.println("Please specify a second file list with -i2 option for differencing");
                     System.out.println(ReleaseInfo.GetUsageInfo());
                     System.exit(1);
                  }
               }
               else
               {
                  System.err.println("Please specify valid arguments: -i2 option can only be used with -d option");
                  System.out.println(ReleaseInfo.GetUsageInfo());
                  System.exit(1);
               }
               break;
            case "-extfile":
               if (argItr.hasNext() == true)
               {
                  RtParams.ExtFileName = argItr.next();
                  ReadFileExtMap(RtParams.ExtFileName);
               }
               else
               {
                  System.err.println("Please specify a file name containing file extension mappings");
                  System.out.println(ReleaseInfo.GetUsageInfo());
                  System.exit(1);
               }
               break;
            case "-t":
               if (argItr.hasNext() == true)
               {
                  RtParams.ModThreshold = StringToDouble(argItr.next());

                  // Check bounds of modification threshold
                  if (RtParams.ModThreshold == -1.0)
                  {
                     logger.error("Invalid modificatin threshold value provided. "
                              + "Setting modification threshold to default value of "
                              + Constants.DEFAULT_MOD_THRESHOLD);

                     RtParams.ModThreshold = Constants.DEFAULT_MOD_THRESHOLD;
                  }
                  else if (RtParams.ModThreshold < Constants.MIN_MOD_THRESHOLD)
                  {
                     logger.error("Modification threshold value of " + RtParams.ModThreshold + " is not allowed. "
                              + "Modification threshold must be between " + Constants.MIN_MOD_THRESHOLD + " and "
                              + Constants.MAX_MOD_THRESHOLD + ". " + "Setting modification threshold to "
                              + Constants.MIN_MOD_THRESHOLD);

                     RtParams.ModThreshold = Constants.MIN_MOD_THRESHOLD;
                  }
                  else if (RtParams.ModThreshold > Constants.MAX_MOD_THRESHOLD)
                  {
                     logger.error("Modification threshold value of " + RtParams.ModThreshold + " is not allowed. "
                              + "Modification threshold must be between " + Constants.MIN_MOD_THRESHOLD + " and "
                              + Constants.MAX_MOD_THRESHOLD + ". " + "Setting modification threshold to "
                              + Constants.MAX_MOD_THRESHOLD);

                     RtParams.ModThreshold = Constants.MAX_MOD_THRESHOLD;
                  }
               }
               else
               {
                  System.err.println("Please provide a value for modifications threshold");
                  System.out.println(ReleaseInfo.GetUsageInfo());
                  System.exit(1);
               }
               break;
            case "-trunc":
               if (argItr.hasNext() == true)
               {
                  RtParams.TruncThreshold = StringToInteger(argItr.next());

                  // Check bounds of truncation threshold
                  if (RtParams.TruncThreshold == -1.0)
                  {
                     logger.error("Invalid truncation threshold value provided. "
                              + "Setting truncation threshold to default value of "
                              + Constants.DEFAULT_TRUNC_THRESHOLD);

                     RtParams.TruncThreshold = Constants.DEFAULT_TRUNC_THRESHOLD;
                  }
                  else if (RtParams.TruncThreshold < Constants.MIN_TRUNC_THRESHOLD)
                  {
                     logger.error("Truncation threshold value of " + RtParams.TruncThreshold + " is not allowed. "
                              + "Truncation threshold must be between " + Constants.MIN_TRUNC_THRESHOLD + " and "
                              + Constants.MAX_TRUNC_THRESHOLD + ". " + "Setting truncation threshold to "
                              + Constants.MIN_TRUNC_THRESHOLD);

                     RtParams.TruncThreshold = Constants.MIN_TRUNC_THRESHOLD;
                  }
                  else if (RtParams.TruncThreshold > Constants.MAX_TRUNC_THRESHOLD)
                  {
                     logger.error("Truncation threshold value of " + RtParams.TruncThreshold + " is not allowed. "
                              + "Truncation threshold must be between " + Constants.MIN_TRUNC_THRESHOLD + " and "
                              + Constants.MAX_TRUNC_THRESHOLD + ". " + "Setting truncation threshold to "
                              + Constants.MAX_TRUNC_THRESHOLD);

                     RtParams.TruncThreshold = Constants.MAX_TRUNC_THRESHOLD;
                  }
                  else
                  {
                     logger.info("Set truncation threshold to " + RtParams.TruncThreshold);
                  }
               }
               else
               {
                  System.err.println("Please provide a value for truncation threshold");
                  System.out.println(ReleaseInfo.GetUsageInfo());
                  System.exit(1);
               }
               break;
            case "-tdup":
               if (argItr.hasNext() == true)
               {
                  RtParams.DupThreshold = StringToDouble(argItr.next());

                  // Check bounds of duplication threshold
                  if (RtParams.DupThreshold == -1.0)
                  {
                     logger.error("Invalid duplication threshold value provided. "
                              + "Setting duplication threshold to default value of " + Constants.DEFAULT_DUP_THRESHOLD);

                     RtParams.DupThreshold = Constants.DEFAULT_DUP_THRESHOLD;
                  }
                  else if (RtParams.DupThreshold < Constants.MIN_DUP_THRESHOLD)
                  {
                     logger.error("Duplication threshold value of " + RtParams.DupThreshold + " is not allowed. "
                              + "Duplication threshold must be between " + Constants.MIN_DUP_THRESHOLD + " and "
                              + Constants.MAX_DUP_THRESHOLD + ". " + "Setting duplication threshold to "
                              + Constants.MIN_DUP_THRESHOLD);

                     RtParams.DupThreshold = Constants.MIN_DUP_THRESHOLD;
                  }
                  else if (RtParams.DupThreshold > Constants.MAX_DUP_THRESHOLD)
                  {
                     logger.error("Duplication threshold value of " + RtParams.DupThreshold + " is not allowed. "
                              + "Duplication threshold must be between " + Constants.MIN_DUP_THRESHOLD + " and "
                              + Constants.MAX_DUP_THRESHOLD + ". " + "Setting duplication threshold to "
                              + Constants.MAX_DUP_THRESHOLD);

                     RtParams.DupThreshold = Constants.MAX_DUP_THRESHOLD;
                  }
                  else
                  {
                     logger.info("Set duplication threshold to " + RtParams.DupThreshold);
                  }
               }
               else
               {
                  System.err.println("Please provide a value for duplicate threshold");
                  System.out.println(ReleaseInfo.GetUsageInfo());
                  System.exit(1);
               }
               break;
            case "-cf":
               RtParams.HndlClearCaseFiles = true;
               break;
            case "-unified":
               RtParams.UnifyResults = true;
               break;
            case "-ascii":
               RtParams.OutputFileFmt = RuntimeParameters.OutputFormat.ASCII;
               break;
            case "-nodup":
               RtParams.SearchForDups = false;
               break;
            case "-nocomplex":
               RtParams.CountCmplxMetrics = false;
               break;
            case "-nolinks":
               RtParams.SkipSymbLinks = true;
               break;
            case "-import":
               if (argItr.hasNext() == true)
               {
                  String argument = argItr.next();

                  if (PropertiesReader.Import(argument))
                  {
                     logger.info("Import of " + argument + " was successful\n");
                     PropertiesReader.PrintProperties();
                     System.out.println();
                     RtParams.UseCustomLang = true;
                  }
                  else
                  {
                     System.err.println("Error uploading " + argument + " language properties file");
                     System.out.println(ReleaseInfo.GetUsageInfo());
                     System.exit(1);
                  }
               }
               else
               {
                  System.err.println("Error: Please specifiy the path of a file to import");
                  System.out.println(ReleaseInfo.GetUsageInfo());
                  System.exit(1);
               }
               break;
            case "-export":
               String languageName = "";

               int remainingArguments = argsList.size() - argItr.nextIndex();

               // Handles both cases: >= 1 and >= 3
               if (remainingArguments >= 1)
               {
                  languageName = argItr.next();
               }

               if (remainingArguments >= 3)
               {
                  // Peak at next argument
                  int index = argItr.nextIndex();
                  String argument = argsList.get(index);
                  if (argument.equals("-outdir") == true)
                  {
                     argItr.next(); // Ignore the argument we just peaked at
                     SetOutputDir(argItr.next());
                  }
               }

               // Write language property of given language to a file
               if (languageName != null && PropertiesWriter.Export(languageName, RtParams.OutputDirPath))
               {
                  System.out.println(
                           languageName + "LanguageProperties.txt successfully written in " + RtParams.OutputDirPath);
                  logger.info(
                           languageName + "LanguageProperties.txt successfully written in " + RtParams.OutputDirPath);
               }
               else
               {
                  System.err.println("Error exporting language properties to a file. "
                           + "Please verify that your inputs are correct");
                  System.out.println(ReleaseInfo.GetUsageInfo());
                  System.exit(1);
               }

               RtParams.CountSLOC = false;
               break;
            default:
               System.err.println("\nArgument '" + arg + "' is not supported by UCC-G. For help, use '-h'");
               System.out.println(ReleaseInfo.GetUsageInfo());
               System.exit(1);
               break; // Will not get here
         }
      }
   }

   /**
    * Converts a String value to a value of type {@link double}
    * 
    * @param str
    *           A String value to be converted
    * @return A double value of input String
    */
   private static double StringToDouble(String str)
   {
      double dblVal = -1.0;
      try
      {
         dblVal = (Double.valueOf(str)).doubleValue();
      }
      catch (NumberFormatException e)
      {
         logger.error("Invalid Argument - " + str + "." + " Cannot convert it to a "
                  + "double precision floating point. Please check your inputs.");
      }
      return dblVal;
   }

   /**
    * Converts a String value to a value of type {@link int}
    * 
    * @param str
    *           A String value to be converted
    * @return An integer representation of input String
    */
   private static int StringToInteger(String str)
   {
      int intVal = -1;

      try
      {
         intVal = (Integer.valueOf(str)).intValue();
      }
      catch (NumberFormatException e)
      {
         logger.error("Invalid Argument - " + str + "." + " Cannot convert it to an integer. "
                  + "Please check your inputs.");
      }
      return intVal;
   }

   /**
    * Sets output directory to provided directory after validating the path
    * 
    * @param outDir
    *           Directory path to store output reports
    */
   private static void SetOutputDir(String outDir)
   {
      // Validate the user provided directory path before setting it as the
      // output directory
      // If path is invalid, use the default output directory
      if (FileUtils.ValidateDir(outDir))
      {
         if (!outDir.endsWith(File.separator))
         {
            outDir += File.separator;
         }
         RtParams.OutputDirPath = outDir;
         logger.info("Set output directory to " + RtParams.OutputDirPath);
      }
      else
      {
         logger.error(
                  outDir + "is not a valid directory.  " + "Defaulting output directory to " + RtParams.OutputDirPath);
      }
   }

   /**
    * Read/parse user provided file that contains language(s) to file extensions
    * mappings.
    * 
    * Once parsed, language to file extension map is stored in a HashMap for
    * easy accessibility.
    * 
    * This HashMap will be used later in the processing chain to override
    * built-in language to file extension mappings.
    * 
    * @param fileName
    *           A name of a file that contains language to file extension(s)
    *           mappings
    */
   private static void ReadFileExtMap(String fileName)
   {
      // Create new line reader
      LineNumberReader reader;

      // Array lists to store comment characters
      ArrayList<String> singleLineComment = new ArrayList<String>();
      ArrayList<String> multiLineCommentStart = new ArrayList<String>(Arrays.asList("["));
      ArrayList<String> multiLineCommentEnd = new ArrayList<String>(Arrays.asList("]"));

      // Comment handler object to find and remove comments from the file
      CommentHandler cmntHndlr = new CommentHandler(singleLineComment, multiLineCommentStart, multiLineCommentEnd);

      String line;
      String separator = ",";

      logger.debug("Parsing " + fileName + " to get user defined language to file extension mappings...");

      try
      {
         // Open the file for reading, use default CHARSET
         reader = new LineNumberReader(new InputStreamReader(new FileInputStream(fileName), Constants.CHARSET_NAME));

         // Read lines till the end of the stream
         while ((line = reader.readLine()) != null)
         {
            // Trim white spaces
            line = line.trim();

            // Proceed if line is not empty
            if (!line.isEmpty())
            {
               // Find and remove comments
               line = cmntHndlr.HandleComments(line, null);

               // Split the line at "=" occurrence
               int idx = line.indexOf("=");

               if (idx != -1)
               {
                  // Get language name, which is to the left of "="
                  String lang = line.substring(0, idx).trim();

                  if (idx + 1 < line.length())
                  {
                     // Get file extension(s) that map to the language
                     String[] extensions = (line.substring(idx + 1)).split(separator);

                     // Only keep extensions that are not empty after trimming
                     ArrayList<String> trimmedExtensions = new ArrayList<String>();

                     for (String ext : extensions)
                     {
                        // Trim white spaces
                        ext = ext.trim();

                        if (!ext.isEmpty())
                        {
                           trimmedExtensions.add(ext);
                        }
                     }

                     // Store language/file extensions as key/value pairs in a
                     // HashMap
                     RtParams.FileExtToLangMap.put(lang.toUpperCase(), trimmedExtensions);
                  }
               }
            }
         }

         // Iterate and display values
         for (HashMap.Entry<String, ArrayList<String>> entry : RtParams.FileExtToLangMap.entrySet())
         {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            logger.debug("Language = " + key + " and File Extensions = " + values);
         }

         // Close stream object
         if (reader != null)
         {
            reader.close();
         }
      }
      catch (IOException e)
      {
         logger.error("I/O error: unable to read file " + fileName);
      }
   }

   /**
    * Deletes log files of 0.0 MB
    */
   private static void DeleteLogFile()
   {
      if (Log4j2ConfigurationFactory.ctx != null)
      {
         Log4j2ConfigurationFactory.ctx.stop();
      }

      if (Log4j2ConfigurationFactory.GetFileName() != null)
      {
         File file = new File(Log4j2ConfigurationFactory.GetFileName());

         if (file.exists() == true)
         {
            double bytes = file.length();
            if (bytes == 0.0)
            {
               FileUtils.DeleteFile(file.getAbsolutePath());
            }
         }
      }
   }
}