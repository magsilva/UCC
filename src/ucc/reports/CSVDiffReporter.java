package ucc.reports;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ucc.datatypes.Constants;
import ucc.datatypes.DiffResultType;

/**
 * CSVDiffReporter class is derived from DiffReporter class. This class contains
 * methods to print several types of reports for users in CSV format. Reports
 * contain differencer data.
 *
 * @author Integrity Applications Incorporated
 *
 */
public class CSVDiffReporter extends DiffReporter
{
   /** Instantiate the Log4j2 logger for this class */
   private static final Logger logger = LogManager.getLogger(CSVDiffReporter.class);

   /**
    * Default constructor
    * 
    * @param numLangs
    *           Total number of languages for report generation
    */
   public CSVDiffReporter(int numLangs)
   {
      // Call super class's default constructor
      super(CSV_EXTENSION, numLangs);
   }

   /**
    * Constructor to instantiate CSVCounterReporter object with given parameters
    *
    * @param outDir
    *           Directory location to write output reports
    * @param numLangs
    *           Total number of languages for report generation
    */
   public CSVDiffReporter(String outDir, int numLangs)
   {
      super(CSV_EXTENSION, outDir, numLangs);
   }

   /**
    * Constructor to instantiate CSVDiffReporter object with given parameters
    *
    * @param outFilePrefix
    *           Prefix to prepend to output report file name
    * @param outDir
    *           Directory location to write output reports
    * @param numLangs
    *           Total number of languages for report generation
    */
   public CSVDiffReporter(String outFilePrefix, String outDir, int numLangs)
   {
      super(outFilePrefix, CSV_EXTENSION, outDir, numLangs);
   }

   @Override
   public void GenDiffReports(ArrayList<DiffResultType> diffResults, boolean dupResults)
   {
      // Generate matched file pairs report
      GenMatchedPairsReport(diffResults, dupResults);

      // Generate report for differencing results of matched file pairs
      GenMainDiffReport(diffResults, dupResults);
   }

   /**
    * Returns column header for matched pairs differencer report
    * 
    * @return A String that contains the column header
    */
   private String GetMatchedPairColHeader()
   {
      String hdr = "Baseline A, Baseline B" + Constants.NEW_LINE_SEPARATOR;

      return hdr;
   }

   @Override
   protected void GenMatchedPairsReport(ArrayList<DiffResultType> diffResults, boolean dupResults)
   {
      // Check if any differencer data is available for reporting
      if (!diffResults.isEmpty())
      {
         String fileName;

         // Get report file name
         if (dupResults)
         {
            fileName = GetReportFileName(ReportType.DUP_MATCHED_PAIRS_REPORT);
         }
         else
         {
            fileName = GetReportFileName(ReportType.MATCHED_PAIRS_REPORT);
         }

         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Write the file header
            bufWriter.write(GetFileHeader("MATCHED FILE PAIRS", RtParams.UserInputStr));
            bufWriter.newLine();

            // Write column headers for data
            bufWriter.write(GetMatchedPairColHeader());

            String row;
            for (int i = 0; i < diffResults.size(); i++)
            {
               row = diffResults.get(i).FileNameA + "," + diffResults.get(i).FileNameB + Constants.NEW_LINE_SEPARATOR;
               bufWriter.write(row);
            }

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing matched pairs report to file " + fileName);
         }
         catch (IOException ex)
         {
            logger.error("Error writing matched pairs report to file " + fileName);
            logger.debug(ex);
         }
      }
   }

   /**
    * Returns column header for main differencer report
    * 
    * @return A String that contains the column header
    */
   private String GetMainDiffColHeader()
   {
      String hdr = "New Lines,Deleted Lines,Modified Lines,Unmodified Lines,"
               + "Modification Type,Language,Module A,Module B" + Constants.NEW_LINE_SEPARATOR;

      return hdr;
   }

   /**
    * Returns summary column header for main differencer report
    * 
    * @return A String that contains the column header
    */
   private String GetSummaryMainDiffColHeader()
   {
      String hdr = "Total New Lines,Total Deleted Lines,Total Modified Lines," + "Total Unmodified Lines"
               + Constants.NEW_LINE_SEPARATOR;

      return hdr;
   }

   @Override
   protected void GenMainDiffReport(ArrayList<DiffResultType> diffResults, boolean dupResults)
   {
      // Check if any differencer data is available for reporting
      if (!diffResults.isEmpty())
      {
         String fileName;

         // Get report file name
         if (dupResults)
         {
            fileName = GetReportFileName(ReportType.DUP_DIFFERENCER_REPORT);
         }
         else
         {
            fileName = GetReportFileName(ReportType.DIFFERENCER_REPORT);
         }

         try
         {
            // Open output streams for writing
            OutputStreamWriter outStream =
                     new OutputStreamWriter(new FileOutputStream(fileName), Constants.CHARSET_NAME);
            BufferedWriter bufWriter = new BufferedWriter(outStream);

            // Write the file header
            bufWriter.write(GetFileHeader("SOURCE CODE DIFFERENTIAL RESULTS", RtParams.UserInputStr));
            bufWriter.newLine();

            // Write column headers for data
            bufWriter.write(GetMainDiffColHeader());

            String row;
            int totalNewLines = 0;
            int totalDelLines = 0;
            int totalModLines = 0;
            int totalUnmodLines = 0;
            for (int i = 0; i < diffResults.size(); i++)
            {
               row = diffResults.get(i).NewLines + "," + diffResults.get(i).DeletedLines + ","
                        + diffResults.get(i).ModLines + "," + diffResults.get(i).UnmodLines + ","
                        + diffResults.get(i).ModType.toString() + "," + diffResults.get(i).LangProperty.name() + ","
                        + diffResults.get(i).FileNameA + "," + diffResults.get(i).FileNameB
                        + Constants.NEW_LINE_SEPARATOR;
               bufWriter.write(row);

               totalNewLines += diffResults.get(i).NewLines;
               totalDelLines += diffResults.get(i).DeletedLines;
               totalModLines += diffResults.get(i).ModLines;
               totalUnmodLines += diffResults.get(i).UnmodLines;
            }

            // Write a blank line
            bufWriter.newLine();

            // Write column header for summary data
            bufWriter.write(GetSummaryMainDiffColHeader());

            // Write summary data
            row = totalNewLines + "," + totalDelLines + "," + totalModLines + "," + totalUnmodLines
                     + Constants.NEW_LINE_SEPARATOR;
            bufWriter.write(row);

            // Close the output streams
            bufWriter.close();
            logger.debug("Finished writing main differencer report to file " + fileName);
         }
         catch (IOException ex)
         {
            logger.error("Error writing to file " + fileName);
            logger.debug(ex);
         }
      }
   }
}
