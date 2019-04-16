package ucc.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import ucc.datatypes.Constants;

/**
 * This class contains parameters that will be used during running of UCC-G.
 * This parameters are set based on user inputs and default values. This class
 * follows Singleton pattern, which means that there is only one static
 * reference to the single instance of this class. This single instance will be
 * maintained by this class and can be used by any other class by calling the
 * GetInstance() method.
 * 
 * @author Integrity Applications Incorporated
 *
 */
public class RuntimeParameters
{
   /** Enumerated type to hold output file format options */
   public enum OutputFormat
   {
      CSV, ASCII
   }

   /** User provided input arguments */
   public StringBuffer UserInputStr;

   /** Flag for code count operation */
   public boolean CountSLOC;

   /** Flag for code differencer */
   public boolean DiffCode;

   /** Flag for print all output results in one file */
   public boolean UnifyResults;

   /** Flag for code complexity count */
   public boolean CountCmplxMetrics;

   /** Flag for duplicate file search */
   public boolean SearchForDups;

   /** Flag for use of custom language properties */
   public boolean UseCustomLang;

   /** Flag for skipping symbolic links */
   public boolean SkipSymbLinks;

   /** Flag for handling clear case files */
   public boolean HndlClearCaseFiles;

   /** First filename of user input file list */
   public String FileListNameA;

   /** Second filename of user input file list */
   public String FileListNameB;

   /** A list of file names from filelist.txt or filelist.dat file */
   public String[] DefaultFileListNames;

   /** File list of the first filelist/directory */
   public ArrayList<String> FileListA;

   /** File list of the second filelist/directory */
   public ArrayList<String> FileListB;

   /** File list of the first filelist/directory */
   public String DirPathA;

   /** File list of the second filelist/directory */
   public String DirPathB;

   /** Specification for which files to process in a given directory */
   public ArrayList<String> FileSpecs;

   /** Modification threshold */
   public double ModThreshold;

   /** Duplication threshold */
   public double DupThreshold;

   /** Line truncation threshold */
   public int TruncThreshold;

   /** User specified output directory path */
   public String OutputDirPath;

   /** User specified output file format */
   public OutputFormat OutputFileFmt;

   /** User specified file for mapping file extensions to languages */
   public String ExtFileName;

   /** A hash map of file extension to language mapping specified by the user */
   public HashMap<String, ArrayList<String>> FileExtToLangMap;

   /** The Single Static instance of this class */
   private static RuntimeParameters RtParams = new RuntimeParameters();

   /**
    * Private constructor to prevent any other class from instantiating an
    * object.
    */
   private RuntimeParameters()
   {
      /** Set default runtime parameters */
      UserInputStr = new StringBuffer("UCC-G ");
      CountSLOC = true; // By default, count SLOC
      CountCmplxMetrics = true; // By default, count complex metrics
      DiffCode = false; // By default, turn off differencing
      SearchForDups = true;
      UseCustomLang = false;
      SkipSymbLinks = false;
      HndlClearCaseFiles = false;

      FileListNameA = "";
      FileListNameB = "";
      FileListA = new ArrayList<String>();
      FileListB = new ArrayList<String>();
      DirPathA = "";
      DirPathB = "";
      FileSpecs = new ArrayList<String>();

      ModThreshold = Constants.DEFAULT_MOD_THRESHOLD;
      DupThreshold = Constants.DEFAULT_DUP_THRESHOLD;
      TruncThreshold = Constants.DEFAULT_TRUNC_THRESHOLD;

      OutputDirPath = System.getProperty("user.dir");

      // Setup the output directory to include a trailing slash character.
      // This is done so that when the application is writing files to the
      // directory,
      // the trailing slash character does not have to be written in front of
      // the filename
      // which can generate inconsistencies throughout the code-base
      if (!OutputDirPath.endsWith(File.separator))
      {
         OutputDirPath += File.separator;
      }

      OutputFileFmt = OutputFormat.CSV;

      ExtFileName = "";
      FileExtToLangMap = new HashMap<String, ArrayList<String>>();
      ;
   }

   /**
    * Provides a handle to the single instance of this class
    * 
    * @return - Returns the lone static instance of this class
    */
   public static RuntimeParameters GetInstance()
   {
      return RtParams;
   }
}