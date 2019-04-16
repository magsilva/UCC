package ucc.reports;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ucc.datatypes.Constants;
import ucc.datatypes.DataTypes.LanguagePropertiesType;
import ucc.main.ReleaseInfo;

/**
 * Abstract top-level Reporter class that contains common variables and methods
 * for report generation.
 * <p>
 * Subclasses should implement the abstract methods to generate reports for
 * specific operations - counter or differencer.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class Reporter
{
   /**
    * Suffix to append to report names to differentiate UCC-G reports from
    * legacy UCC reports. Provided for being able to compare the generated files
    * from UCC-G with those from legacy UCC because MS Excel will not open two
    * files with the same name simultaneously
    */
   private static final String REPORT_SUFFIX = "";

   /**
    * Enumerated type to hold report types and names
    */
   protected enum ReportType
   {
      LANG_COUNT_REPORT("_outfile" + REPORT_SUFFIX),
      SUMMARY_COUNT_REPORT("outfile_summary" + REPORT_SUFFIX),
      UNIFIED_COUNT_REPORT("TOTAL_outfile" + REPORT_SUFFIX),
      CMPLX_COUNT_REPORT("outfile_cplx" + REPORT_SUFFIX),
      CYCL_CMPLX_COUNT_REPORT("outfile_cyclomatic_cplx" + REPORT_SUFFIX),
      MI_COUNT_REPORT("outfile_maintainability_index" + REPORT_SUFFIX),
      DUP_PAIRS_REPORT("DuplicatePairs" + REPORT_SUFFIX),
      DUP_LANG_COUNT_REPORT("_outfile" + REPORT_SUFFIX),
      DUP_SUMMARY_COUNT_REPORT(DUP_REPORT_PREFIX + "outfile_summary" + REPORT_SUFFIX),
      DUP_UNIFIED_COUNT_REPORT(DUP_REPORT_PREFIX + "TOTAL_outfile" + REPORT_SUFFIX),
      DUP_CMPLX_COUNT_REPORT(DUP_REPORT_PREFIX + "outfile_cplx" + REPORT_SUFFIX),
      DUP_CYCL_CMPLX_COUNT_REPORT(DUP_REPORT_PREFIX + "outfile_cyclomatic_cplx" + REPORT_SUFFIX),
      MATCHED_PAIRS_REPORT("MatchedPairs" + REPORT_SUFFIX),
      DIFFERENCER_REPORT("outfile_diff_results" + REPORT_SUFFIX),
      DUP_MATCHED_PAIRS_REPORT(DUP_REPORT_PREFIX + "MatchedPairs" + REPORT_SUFFIX),
      DUP_DIFFERENCER_REPORT(DUP_REPORT_PREFIX + "outfile_diff_results" + REPORT_SUFFIX),
      UNCOUNTED_FILE_REPORT("outfile_uncounted_files" + REPORT_SUFFIX);

      /** Report name associated with enumerated value */
      private String RprtName;

      /**
       * Constructor
       * 
       * @param name
       *           Name of report
       */
      private ReportType(String name)
      {
         this.RprtName = name;
      }

      /**
       * Get Report name associated with the report type
       * 
       * @return Returns report name for the given type
       */
      public String GetReportName()
      {
         return this.RprtName;
      }
   }

   /** File extension associated with CSV format */
   protected static final String CSV_EXTENSION = ".csv";

   /** File extension associated with ASCII format */
   protected static final String ASCII_EXTENSION = ".txt";

   /** Prefix for duplicate files counter reports */
   protected static final String DUP_REPORT_PREFIX = "Duplicates-";

   /** Fixed file header with UCC-X information for all reports */
   protected static final String UCC_G_INFO_HEADER = "Unified Code Counter - U.S. Government Edition (UCC-G)"
            + Constants.NEW_LINE_SEPARATOR + "(c) Copyright 2015 - 2017 United States Government";

   /**
    * Stores location of default output directory if one is not specified by the
    * user
    */
   protected String OutputDir;

   /** Stores prefix for report file names (if any) */
   protected String OutFilePrefix;

   /** Stores extension for report file names */
   protected String OutFileExt;

   /** Total number of languages to generate reports */
   protected int NumLangs;

   /**
    * Default constructor to initialize class member variables with default
    * values
    */
   public Reporter()
   {
      SetOutputDir(System.getProperty("user.dir"));
      SetOutFilePrefix("");
      SetOutFileExt(CSV_EXTENSION);
      SetNumLangs(LanguagePropertiesType.values().length);
   }

   /**
    * Constructor to instantiate Reporter object with given parameters
    *
    * @param outFileExt
    *           Extension of output report file name
    * @param numLangs
    *           Total number of languages for report generation
    */
   public Reporter(String outFileExt, int numLangs)
   {
      SetOutFilePrefix("");
      SetOutFileExt(outFileExt);
      SetOutputDir(System.getProperty("user.dir"));
      SetNumLangs(numLangs);
   }

   /**
    * Constructor to instantiate Reporter object with given parameters
    *
    * @param outFileExt
    *           Extension of output report file name
    * @param outDir
    *           Directory location to write output reports
    * @param numLangs
    *           Total number of languages for report generation
    */
   public Reporter(String outFileExt, String outDir, int numLangs)
   {
      SetOutFilePrefix("");
      SetOutFileExt(outFileExt);
      SetOutputDir(outDir);
      SetNumLangs(numLangs);
   }

   /**
    * Constructor to instantiate Reporter object with given parameters
    *
    * @param outFilePrefix
    *           Prefix to prepend to output report file name
    * @param outFileExt
    *           Extension of output report file name
    * @param outDir
    *           Directory location to write output reports
    * @param numLangs
    *           Total number of languages for report generation
    */
   public Reporter(String outFilePrefix, String outFileExt, String outDir, int numLangs)
   {
      SetOutFilePrefix(outFilePrefix);
      SetOutFileExt(outFileExt);
      SetOutputDir(outDir);
      SetNumLangs(numLangs);
   }

   /**
    * Sets the location of output directory
    *
    * @param outDir
    *           A String that contains a directory path
    */
   protected void SetOutputDir(String outDir)
   {
      OutputDir = outDir;
   }

   /**
    * Sets the prefix for report file names
    *
    * @param prefix
    *           A string that contains prefix to prepend to report file names
    */
   protected void SetOutFilePrefix(String prefix)
   {
      OutFilePrefix = prefix;
   }

   /**
    * Sets the extension for report file names
    *
    * @param ext
    *           A string that contains extension to append to report file names
    */
   protected void SetOutFileExt(String ext)
   {
      OutFileExt = ext;
   }

   /**
    * Sets the total number of languages
    *
    * @param numLangs
    *           A integer that contains total number of languages to generate
    *           reports for
    */
   protected void SetNumLangs(int numLangs)
   {
      NumLangs = numLangs;
   }

   /**
    * Generates report file name for given report type
    *
    * @param type
    *           Type of the report
    * @return A String containing the report file name
    */
   protected String GetReportFileName(ReportType type)
   {
      return (OutputDir + OutFilePrefix + type.GetReportName() + OutFileExt);
   }

   /**
    * Generates report file name for given report type and language type
    *
    * @param type
    *           Type of the report
    * @param lang
    *           Programming language name associated with the report
    * @return A String containing the report file name
    */
   protected String GetReportFileName(ReportType type, String lang)
   {
      String fileName;

      if (type == ReportType.LANG_COUNT_REPORT)
      {
         fileName = OutputDir + OutFilePrefix + lang + type.GetReportName() + OutFileExt;
      }
      else if (type == ReportType.DUP_LANG_COUNT_REPORT)
      {
         fileName = OutputDir + OutFilePrefix + DUP_REPORT_PREFIX + lang + type.GetReportName() + OutFileExt;
      }
      else
      {
         fileName = GetReportFileName(type);
      }

      return fileName;
   }

   /**
    * Returns header information for all reports of any file format.
    *
    * @param title
    *           A String that contains name of the report
    * @param userCmd
    *           A String that contains user entered command
    * @return A string that contains file header
    */
   protected String GetFileHeader(String title, StringBuffer userCmd)
   {
      DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
      Date date = new Date();

      String header = UCC_G_INFO_HEADER + Constants.NEW_LINE_SEPARATOR + Constants.NEW_LINE_SEPARATOR + title
               + Constants.NEW_LINE_SEPARATOR + "Generated by " + ReleaseInfo.GetVersionInfo() + " on "
               + dateFormat.format(date) + Constants.NEW_LINE_SEPARATOR + userCmd.toString()
               + Constants.NEW_LINE_SEPARATOR;

      return header;
   }
}
